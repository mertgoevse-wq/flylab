#!/usr/bin/env python3
"""
Generates the FlyLab neural kernel as a hand-encoded, fully deterministic
TFLite flatbuffer:

    x[1,KC] --FULLY_CONNECTED(W[MBON,KC]+b, act=NONE)--> h[1,MBON] --TANH--> y[1,MBON]

KC=32 Kenyon cells, MBON=8 readouts — mirrors the KC->MBON weighted
propagation step of the plasticity model (MODELED, scientific badge).
Only NNAPI-supported ops are used, so LiteRT can dispatch it to the
Samsung Exynos ENN NPU (Galaxy A56: /vendor/lib64/libenn_*.so, ENN 2.3.28)
via the NNAPI delegate, to the Xclipse 540 GPU via the GPU delegate, or run
a plain CPU fallback.

Verification: parse-back with the official TFLite schema bindings
(pip: tflite flatbuffers) plus CPU reference outputs stored in a manifest
for the in-app accelerator self-test.
"""
import json
import hashlib
import os
import sys

import numpy as np
import flatbuffers
from flatbuffers.builder import Builder
from tflite.Buffer import BufferStart, BufferAddData, BufferEnd, Buffer as BufferCls
from tflite.Tensor import (TensorStart, TensorAddShape, TensorAddType, TensorAddBuffer,
                           TensorAddName, TensorEnd, TensorStartShapeVector, Tensor as TensorCls)
from tflite.Operator import (OperatorStart, OperatorAddOpcodeIndex, OperatorAddInputs,
                             OperatorAddOutputs, OperatorAddBuiltinOptionsType,
                             OperatorAddBuiltinOptions, OperatorEnd,
                             OperatorStartInputsVector, OperatorStartOutputsVector,
                             Operator as OperatorCls)
from tflite.OperatorCode import (OperatorCodeStart, OperatorCodeAddBuiltinCode,
                                 OperatorCodeAddVersion, OperatorCodeEnd,
                                 OperatorCodeAddDeprecatedBuiltinCode,
                                 OperatorCode as OperatorCodeCls)
from tflite.FullyConnectedOptions import (FullyConnectedOptionsStart,
                                          FullyConnectedOptionsAddFusedActivationFunction,
                                          FullyConnectedOptionsEnd)
from tflite.SubGraph import (SubGraphStart, SubGraphAddName, SubGraphAddTensors,
                             SubGraphAddInputs, SubGraphAddOutputs, SubGraphAddOperators,
                             SubGraphEnd, SubGraphStartTensorsVector,
                             SubGraphStartInputsVector, SubGraphStartOutputsVector,
                             SubGraphStartOperatorsVector, SubGraph as SubGraphCls)
from tflite.Model import (ModelStart, ModelAddVersion, ModelAddOperatorCodes,
                          ModelAddSubgraphs, ModelAddBuffers, ModelAddDescription,
                          ModelEnd, ModelStartOperatorCodesVector,
                          ModelStartSubgraphsVector, ModelStartBuffersVector,
                          Model as ModelCls)
from tflite import BuiltinOperator, BuiltinOptions, TensorType, ActivationFunctionType

FULLY_CONNECTED = int(BuiltinOperator.FULLY_CONNECTED)
TANH = int(BuiltinOperator.TANH)
FC_OPT_ID = int(BuiltinOptions.FullyConnectedOptions)
FLOAT32 = int(TensorType.FLOAT32)
NONE = int(ActivationFunctionType.NONE)

KC, MBON = 32, 8

rng = np.random.RandomState(1337)
W = (rng.randn(MBON, KC) * 0.1).astype(np.float32)   # [MBON x KC]
b = np.zeros(MBON, np.float32)
x0 = (rng.rand(KC) * 0.4 + 0.3).astype(np.float32)   # deterministic probe input
Y0 = np.tanh(W @ x0 + b).astype(np.float32)          # CPU reference output

bld = Builder(1024)

# ---------------- buffers ----------------
def create_buffer(payload):
    """Create one TFLite buffer; payload None = empty buffer."""
    data_off = bld.CreateByteVector(payload) if payload is not None else 0
    BufferStart(bld)
    if payload is not None:
        BufferAddData(bld, data_off)
    return BufferEnd(bld)

buf_w = create_buffer(W.tobytes())
buf_b = create_buffer(b.tobytes())
buf_nil = create_buffer(None)  # empty buffer (runtime tensors point here)

# ---------------- tensors ----------------
names = [bld.CreateString(n) for n in
         ("kernel_weights", "kernel_bias", "kc_input", "mbon_preact", "mbon_output")]

def make_tensor(name_off, shape, buf_idx):
    TensorStartShapeVector(bld, len(shape))
    for d in reversed(shape):
        bld.PrependInt32(d)
    off_shape = bld.EndVector()
    TensorStart(bld)
    TensorAddShape(bld, off_shape)
    TensorAddType(bld, FLOAT32)
    TensorAddBuffer(bld, buf_idx)
    TensorAddName(bld, name_off)
    return TensorEnd(bld)

t_w = make_tensor(names[0], [MBON, KC], 1)   # buffer 1 = weights
t_b = make_tensor(names[1], [MBON], 2)       # buffer 2 = bias
t_x = make_tensor(names[2], [1, KC], 0)      # buffer 0 = empty (runtime)
t_h = make_tensor(names[3], [1, MBON], 0)
t_y = make_tensor(names[4], [1, MBON], 0)

# ---------------- opcodes ----------------
def make_opcode(op_id):
    OperatorCodeStart(bld)
    # legacy int8 slot so legacy readers (<128) resolve identically
    OperatorCodeAddDeprecatedBuiltinCode(bld, op_id)
    OperatorCodeAddVersion(bld, 1)
    return OperatorCodeEnd(bld)

oc_fc = make_opcode(FULLY_CONNECTED)
oc_tanh = make_opcode(TANH)

# ---------------- operators ----------------
# op 0: FULLY_CONNECTED(x, w, b) -> h
FullyConnectedOptionsStart(bld)
FullyConnectedOptionsAddFusedActivationFunction(bld, NONE)
fc_opts = FullyConnectedOptionsEnd(bld)

OperatorStartInputsVector(bld, 3)
bld.PrependUint32(2)
bld.PrependUint32(1)
bld.PrependUint32(0)
fci = bld.EndVector()

OperatorStartOutputsVector(bld, 1)
bld.PrependUint32(3)
fco = bld.EndVector()

OperatorStart(bld)
OperatorAddOpcodeIndex(bld, 0)
OperatorAddInputs(bld, fci)
OperatorAddOutputs(bld, fco)
OperatorAddBuiltinOptionsType(bld, FC_OPT_ID)
OperatorAddBuiltinOptions(bld, fc_opts)
op0 = OperatorEnd(bld)

# op 1: TANH(h) -> y  (no options)
OperatorStartInputsVector(bld, 1)
bld.PrependUint32(3)
ti = bld.EndVector()
OperatorStartOutputsVector(bld, 1)
bld.PrependUint32(4)
to = bld.EndVector()

OperatorStart(bld)
OperatorAddOpcodeIndex(bld, 1)
OperatorAddInputs(bld, ti)
OperatorAddOutputs(bld, to)
op1 = OperatorEnd(bld)

SubGraphStartOperatorsVector(bld, 2)
bld.PrependUOffsetTRelative(op1)
bld.PrependUOffsetTRelative(op0)
ops = bld.EndVector()

# ---------------- subgraph ----------------
SubGraphStartTensorsVector(bld, 5)
for t in reversed([t_x, t_w, t_b, t_h, t_y]):
    bld.PrependUOffsetTRelative(t)
tensors_vec = bld.EndVector()

SubGraphStartInputsVector(bld, 1)
bld.PrependUint32(0)
gin = bld.EndVector()
SubGraphStartOutputsVector(bld, 1)
bld.PrependUint32(4)
gout = bld.EndVector()

sub_name = bld.CreateString("flylab_neural_kernel")
SubGraphStart(bld)
SubGraphAddName(bld, sub_name)
SubGraphAddTensors(bld, tensors_vec)
SubGraphAddInputs(bld, gin)
SubGraphAddOutputs(bld, gout)
SubGraphAddOperators(bld, ops)
subgraph = SubGraphEnd(bld)

# ---------------- model ----------------
ModelStartSubgraphsVector(bld, 1)
bld.PrependUOffsetTRelative(subgraph)
subgraphs = bld.EndVector()

ModelStartOperatorCodesVector(bld, 2)
bld.PrependUOffsetTRelative(oc_tanh)
bld.PrependUOffsetTRelative(oc_fc)
ocodes = bld.EndVector()

ModelStartBuffersVector(bld, 3)
bld.PrependUOffsetTRelative(buf_b)
bld.PrependUOffsetTRelative(buf_w)
bld.PrependUOffsetTRelative(buf_nil)
bufs = bld.EndVector()

desc = bld.CreateString("FlyLab KC->MBON neural kernel (MODELED)")

ModelStart(bld)
ModelAddVersion(bld, 3)
ModelAddDescription(bld, desc)
ModelAddOperatorCodes(bld, ocodes)
ModelAddSubgraphs(bld, subgraphs)
ModelAddBuffers(bld, bufs)
model = ModelEnd(bld)
bld.Finish(model)

data = bytes(bld.Output())

# ---------------- parse-back validation ----------------
m = ModelCls.GetRootAs(data, 0)
assert m.Version() == 3, m.Version()
assert m.SubgraphsLength() == 1
sg = m.Subgraphs(0)
assert sg.TensorsLength() == 5, sg.TensorsLength()
assert sg.InputsLength() == 1 and sg.OutputsLength() == 1
assert sg.Inputs(0) == 0 and sg.Outputs(0) == 4
assert sg.OperatorsLength() == 2, sg.OperatorsLength()
codes = m.OperatorCodes(0)
assert codes.BuiltinCode() == FULLY_CONNECTED or codes.DeprecatedBuiltinCode() == FULLY_CONNECTED, \
    (codes.BuiltinCode(), codes.DeprecatedBuiltinCode())
assert FULLY_CONNECTED == 9, FULLY_CONNECTED
t0 = sg.Tensors(0)
assert t0.ShapeLength() == 2 and t0.Type() == FLOAT32, (t0.ShapeLength(), t0.Type())
wb = sg.Tensors(1)
assert wb.ShapeLength() == 2 and wb.Shape(0) == MBON and wb.Shape(1) == KC

# outputs dir + manifest
out_dir = os.path.normpath(os.path.join(os.path.dirname(__file__), "..", "app", "src", "main", "assets"))
os.makedirs(out_dir, exist_ok=True)
model_path = os.path.join(out_dir, "neural_kernel.tflite")
with open(model_path, "wb") as f:
    f.write(data)

manifest = {
    "kernel": "KC->MBON FULLY_CONNECTED+TANH",
    "kc_count": KC,
    "mbon_count": MBON,
    "model_file": "assets/neural_kernel.tflite",
    "weights_sha256": hashlib.sha256(W.tobytes()).hexdigest(),
    "probe_input_x0": [round(float(v), 6) for v in x0],
    "expected_output_y0": [round(float(v), 6) for v in Y0],
    "generator": "tools/generate_npu_kernel.py (deterministic seed 1337)",
    "evidence_level": "MODELED",
}
man_path = os.path.join(out_dir, "neural_kernel_manifest.json")
with open(man_path, "w") as f:
    json.dump(manifest, f, indent=2)

print("OK model:", model_path, len(data), "bytes")
print("OK manifest:", man_path)
print("expected_output_y0:", [round(float(v), 5) for v in Y0.tolist()])
sys.exit(0)
