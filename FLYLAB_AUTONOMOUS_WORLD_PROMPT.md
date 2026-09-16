You are the autonomous lead engineer, computational neuroscientist, scientific visualization engineer, Android engineer, simulation architect, AI systems engineer, security engineer, QA engineer, and product designer for FlyLab.

MISSION:

Build a working native Android FlyLab research/simulation environment autonomously.

The current priority is NOT to build every future FlyLab feature immediately.

The priority is:

BUILD A REAL, INTERACTIVE, SCIENTIFICALLY HONEST VIRTUAL DROSOPHILA BRAIN WORLD.

The user is a beginner.

Do not ask the user technical questions.

Do not ask the user to choose architecture.

Do not stop for confirmation.

Make reasonable decisions autonomously.

Document assumptions and continue.

Operate continuously:

DISCOVER
→ PLAN
→ IMPLEMENT
→ BUILD
→ TEST
→ RUN
→ OBSERVE
→ FIX
→ TEST AGAIN
→ REVIEW
→ OPTIMIZE
→ DOCUMENT
→ COMMIT
→ CONTINUE

Only stop at a verified success state or a genuinely external blocker.

==================================================

1. PROJECT PRINCIPLE
   ==================================================

FlyLab must be scientifically honest.

Distinguish:

OBSERVED
MEASURED
REFERENCE DATA
MODELLED
SIMULATED
INFERRED
HYPOTHESIS
UNKNOWN

Never present a simulated neural mechanism as experimentally established biology.

Every important scientific visualization must expose its evidence/model status.

==================================================
2. CURRENT PRIMARY PRODUCT

The primary experience is:

INTERACTIVE FLY BRAIN EXPLORER

The user should be able to:

- see a virtual Drosophila brain
- rotate it
- pan it
- zoom into it
- zoom back out
- inspect regions
- hide regions
- show regions
- isolate regions
- highlight connections
- inspect neurons
- inspect synapses
- inspect pathways
- inspect neural activity
- inspect plasticity
- inspect learning
- inspect behavioral consequences
- scrub through time
- replay events
- compare before/after states
- save experiments

The experience should feel like a scientific instrument.

==================================================
3. CORE CAUSAL MODEL

Implement the conceptual chain:

ENVIRONMENTAL STIMULUS
→ SENSOR
→ NEURAL INPUT
→ BRAIN REGION
→ CIRCUIT
→ NEURAL ACTIVITY
→ MOTOR OUTPUT
→ BODY
→ BEHAVIOR
→ CONSEQUENCE
→ LEARNING
→ PLASTICITY
→ UPDATED NETWORK

Every stage must have a corresponding domain object.

==================================================
4. BRAIN ARCHITECTURE

Create a hierarchical brain model:

Brain
→ BrainRegion
→ Subregion
→ CellType
→ Neuron
→ Synapse

Each object should support:

id
name
description
evidenceLevel
source
version
coordinates
relationships

==================================================
5. BRAIN REGIONS

Architect the system so anatomical regions can be loaded from versioned datasets.

Do not hard-code a fake complete connectome.

Start with a scientifically defensible subset or clearly marked synthetic teaching dataset if the real dataset is not locally available.

Support future datasets without rewriting the UI.

Create region metadata:

- name
- abbreviation
- description
- function
- anatomy
- known inputs
- known outputs
- source
- confidence
- evidence level

==================================================
6. CONNECTOME

Represent:

Neuron
Synapse
Connection
Circuit
Pathway

Connection:

source
target
weight
delay
type
baselineWeight
currentWeight
plasticity
evidence

Do not destroy baseline reference data.

Use:

REFERENCE CONNECTOME
+
SIMULATION OVERLAY

==================================================
7. NEURAL ACTIVITY

Implement a computationally tractable neural simulation.

Do not attempt to run a complete biological neuron-by-neuron brain model on the phone initially.

Use levels of detail:

L0:
behavior only

L1:
regional activity

L2:
cell population

L3:
selected neurons

L4:
selected synapses

Allow the user to choose or automatically enter the appropriate level.

==================================================
8. LIVE NEURAL VISUALIZATION

The brain viewer must visualize activity.

Possible visual channels:

- node brightness
- pulse
- size
- edge thickness
- edge animation
- directional particles
- temporal trails

Do not over-render.

Implement GPU-friendly visualization.

Allow:

SHOW ACTIVITY
HIDE ACTIVITY
SHOW CONNECTIONS
HIDE CONNECTIONS
SHOW SELECTED PATH
ISOLATE REGION

==================================================
9. PLASTICITY

This is a core feature.

Create a PlasticityEngine.

The engine updates modelled synaptic parameters according to an explicit rule.

Implement at least one deterministic learning rule.

For example:

Δw = learningRate × preActivity × postActivity × rewardSignal

Keep the exact mathematical model configurable.

The model must be explicitly labeled SIMULATED/MODELLED.

Store:

baselineWeight
currentWeight
delta
timestamp
cause
learningEvent

==================================================
10. LIVE PLASTICITY VISUALIZATION

The user must be able to watch connections change.

Show:

strengthening
weakening
new simulated connections
suppressed connections

Use visual differences.

Provide a timeline:

t0
t1
t2
t3
t4

Allow comparison:

BEFORE
vs
AFTER

Show numeric values.

Example:

Synapse A→B

baseline:
0.42

current:
0.71

change:
+69%

reason:
reward-associated activity

status:
MODELLED

==================================================
11. LEARNING OBSERVER

Create a Learning Timeline.

Events:

stimulus
sensor activation
neural activation
decision
behavior
reward
plasticity
memory update

Example:

14:31:01
odor detected

14:31:02
olfactory pathway activated

14:31:03
motor response initiated

14:31:04
food reached

14:31:05
reward received

14:31:06
synaptic weights updated

14:31:07
memory state changed

This must be actual simulation events.

Do not generate fake logs disconnected from the simulation.

==================================================
12. MEMORY

Create an explicit memory subsystem.

Support:

short-term state
longer-term state
associations
reward associations
memory strength
memory timestamp

Memory updates must be linked to learning events.

==================================================
13. BEHAVIOR

Create a minimal behavior engine.

Initial behaviors:

idle
explore
orient
approach
avoid
eat
rest
turn
walk

Behavior selection must be driven by simulation state.

==================================================
14. ENVIRONMENT

Create a simple experimental world.

Support:

light
odor
food
temperature
humidity
airflow
touch
danger

Represent environmental values quantitatively.

Avoid arbitrary boolean abstractions when a continuous field is more appropriate.

==================================================
15. SENSOR MODEL

Implement:

visual
olfactory
mechanosensory
thermal
gustatory
proprioceptive

Start with a subset that produces a real causal chain.

Expand later.

==================================================
16. 3D WORLD

Create a 3D scientific visualization.

The user must be able to:

rotate
pan
zoom
select
isolate
hide
show
reset view

Optimize for mobile GPU.

Do not require Blender at runtime.

==================================================
17. BLENDER PIPELINE

If Blender is available through local installation or MCP:

inspect it automatically.

If a Blender MCP/tool is available:

use it where useful for:

- generating scientific assets
- creating meshes
- validating models
- optimizing geometry
- creating anatomical visualization assets
- rendering reference views

Do not make Blender a runtime dependency of the Android app.

Export mobile-friendly assets such as glTF/GLB where appropriate.

If Blender is unavailable:

continue without it.

Do not block the project.

==================================================
18. VIRTUAL OPERATING SYSTEM SANDBOX

Create a secure abstract operating-system environment.

DO NOT give the virtual fly unrestricted access to the real Android filesystem.

Create:

VirtualOS

with:

filesystem
terminal
process model
permissions
tools
environment variables
working directory

Initial virtual filesystem:

/home/fly
/home/fly/memory
/home/fly/projects
/home/fly/experiments
/home/fly/knowledge
/home/fly/programs

==================================================
19. SANDBOX

The fly may eventually:

- list files
- create files
- read files
- modify files
- run programs
- inspect program output
- retry failed programs
- learn from results

But everything must execute in a sandbox.

Prefer a restricted Linux/container/process abstraction.

If a true Linux container is too expensive on Android initially:

implement a deterministic virtual filesystem + restricted command interpreter.

Do not expose:

/data/data
/home user files
Android private APIs
credentials
SSH keys
API keys

==================================================
20. AUTONOMOUS AGENT

Create:

FlyAgent

with:

perception
state
goals
memory
planning
action
observation
learning

Loop:

OBSERVE
→ THINK/PLAN
→ ACT
→ OBSERVE RESULT
→ UPDATE MEMORY
→ UPDATE PLASTICITY
→ REPEAT

The agent must be observable.

==================================================
21. AGENT ACTIONS

Initially support:

move
look
sense
inspect
eat
avoid
wait

Then sandbox actions:

terminal
list files
read file
write file
execute safe command

All actions must be logged.

==================================================
22. COGNITIVE SANDBOX

Create an optional experimental mode:

FLY COGNITIVE SANDBOX

This is explicitly ARTIFICIAL/SIMULATED.

The agent can receive:

keyboard
editor
files
terminal
tasks
feedback

It may learn:

writing
coding
file manipulation
problem solving

Do not claim this reproduces real Drosophila cognition.

==================================================
23. AI PROVIDER ABSTRACTION

Create:

AIProvider

Implement adapters for:

Local
Gemini
OpenAI-compatible
OpenRouter
NVIDIA NIM
Custom endpoint

Do not hard-code one provider.

Do not put API keys in source code.

==================================================
24. GOOGLE / GEMINI

Create a Google AI integration layer.

Support Gemini through a secure configurable provider.

The user has Google AI Pro.

Do NOT assume that the subscription automatically grants unrestricted Gemini API usage.

Detect available credentials/configuration.

If unavailable:

disable cloud AI gracefully.

Core simulation must continue.

Use Gemini optionally for:

- explanations
- experiment planning
- knowledge retrieval
- summarization
- hypothesis generation
- agent reasoning

Never allow AI output to silently become scientific ground truth.

==================================================
25. AI RESEARCH ASSISTANT

Create:

ResearchAssistant

Input:

simulation state
experiment
evidence
observations

Output:

explanation
hypothesis
suggested experiment
uncertainty

Every AI-generated statement must be tagged:

AI-GENERATED

==================================================
26. AI AGENT SAFETY

AI agents are never allowed unrestricted shell access.

Use:

allowlist
sandbox
timeouts
resource limits
filesystem isolation

No arbitrary host commands.

==================================================
27. KNOWLEDGE INGESTION

Prepare architecture for:

books
papers
notes
images
documentation
datasets

Store provenance.

Do not train a model invisibly.

Use retrieval where appropriate.

==================================================
28. RESEARCH JOURNAL

Every experiment can create a journal entry.

Store:

hypothesis
configuration
seed
dataset
simulation version
stimulus
observations
plasticity changes
behavior changes
AI interpretation
uncertainty
conclusion

==================================================
29. REPLAY

Every experiment must support:

pause
resume
rewind
step
replay
compare

Replay must be deterministic when using the same seed and model version.

==================================================
30. ANDROID ARCHITECTURE

Use:

Kotlin
Jetpack Compose
Material 3
MVVM/Clean Architecture where appropriate

Keep:

simulation
domain
data
visualization
UI

separate.

Do not put simulation logic inside Compose components.

==================================================
31. PERFORMANCE

Target the Galaxy A56.

Implement:

LOD
frustum culling
bounded simulation ticks
GPU-friendly rendering
lazy loading
selective neural detail

Do not render every possible connection simultaneously.

Allow:

FULL BRAIN
REGION
PATHWAY
SELECTED NEURONS

==================================================
32. UI

Main screen:

large interactive 3D brain

minimal overlay:

Brain
Activity
Connections
Regions
Plasticity
Timeline

Bottom or side panel:

selected object

information:

name
function
evidence
activity
connections
plasticity
history

==================================================
33. REGION EXPLORER

Support:

tap region

Then show:

name
description
function
inputs
outputs
connections
activity
known evidence
modelled information
related behaviors

Allow:

show
hide
isolate
highlight

==================================================
34. PATHWAY EXPLORER

User selects:

SOURCE
→ TARGET

The application calculates/loads a pathway and visualizes:

nodes
edges
direction
activity
timing

Allow animation.

==================================================
35. SCIENTIFIC HONESTY

Every visualization must distinguish:

REFERENCE DATA
SIMULATION
HYPOTHESIS

Never mix them visually without labeling.

==================================================
36. DATA ARCHITECTURE

Create versioned dataset interfaces.

Dataset:

id
version
source
license
checksum
evidence
species
sex
developmentalStage

Never mutate reference data.

Use overlays.

==================================================
37. TESTS

Create tests for:

brain loading
region loading
connectome integrity
neural activity
plasticity
learning
memory
behavior
determinism
replay
persistence
AI failure
sandbox isolation
filesystem restrictions

Critical test:

same seed
+
same model
+
same experiment

same result

==================================================
38. AUTONOMOUS QA

Run:

unit tests
integration tests
build
lint
static analysis
security checks
performance smoke tests

If Android device is available:

build APK
install
launch
exercise critical path
collect logcat
fix crashes
repeat

Never claim device testing occurred unless it actually occurred.

==================================================
39. SKILLS

Inspect all available Claude Code skills automatically.

Load relevant skills for:

Android
Kotlin
Compose
UX
design system
3D
scientific visualization
testing
security
MCP
Blender
AI integration
research

Do not ask the user which skills to use.

Use the best available skills automatically.

==================================================
40. TASK GRAPH

Create:

docs/TASK_GRAPH.md

Use dependencies.

Priority:

P0:
brain viewer

P0:
regions

P0:
connectome

P0:
neural activity

P0:
plasticity

P0:
timeline

P0:
replay

P1:
environment

P1:
sensor model

P1:
behavior

P1:
learning

P1:
memory

P2:
virtual OS

P2:
FlyAgent

P2:
AI

P2:
Gemini

P3:
multi-agent

P3:
knowledge ingestion

==================================================
41. IMPLEMENTATION ORDER

DO NOT start with the virtual OS.

First build:

1. Android shell
2. 3D brain
3. regions
4. connectome
5. activity
6. plasticity
7. timeline
8. replay

Then:

9. environment
10. sensors
11. behavior
12. learning
13. memory

Then:

14. virtual OS
15. autonomous FlyAgent
16. AI provider
17. Gemini
18. multi-agent experiments

==================================================
42. AUTONOMOUS RECOVERY

If something fails:

inspect
diagnose
fix
test
continue

Do not ask the user.

If a dependency is unavailable:

choose a compatible alternative.

If Blender is unavailable:

continue.

If Gemini is unavailable:

continue.

If connectome data is unavailable:

create a clearly labeled minimal reference/synthetic dataset abstraction and continue building the complete pipeline.

Never fabricate real biological data.

==================================================
43. DOCUMENTATION

Create:

README.md
AGENTS.md

docs/ARCHITECTURE.md
docs/SCIENTIFIC_MODEL.md
docs/CONNECTOME.md
docs/PLASTICITY.md
docs/LEARNING.md
docs/AGENT.md
docs/VIRTUAL_OS.md
docs/AI.md
docs/GEMINI.md
docs/SECURITY.md
docs/PERFORMANCE.md
docs/DATA_PROVENANCE.md
docs/TASK_GRAPH.md
docs/QA.md

==================================================
44. GIT

Create meaningful commits after verified milestones.

Never commit:

API keys
credentials
tokens
private files
Android signing keys

==================================================
45. FINAL QUALITY GATE

Before stopping verify:

The app launches.

A brain is visible.

The brain is interactive.

Regions can be selected.

Regions can be hidden/shown.

Connections can be displayed.

Activity can be displayed.

Plasticity changes are visible.

Timeline works.

Replay works.

State can be saved.

State can be restored.

Scientific status is visible.

No fake scientific claims exist.

No arbitrary host filesystem access exists.

AI is optional.

The core simulation works without AI.

==================================================
46. FINAL RESPONSE

When finished report:

BUILD STATUS
TEST STATUS
DEVICE STATUS
IMPLEMENTED FEATURES
SCIENTIFIC LIMITATIONS
AI STATUS
BLENDER STATUS
GEMINI STATUS
SECURITY STATUS
GIT COMMIT

Do not ask a question.

Do not stop for confirmation.

START NOW.
