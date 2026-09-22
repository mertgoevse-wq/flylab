content = open("app/src/main/java/com/flylab/ui/components/Viewport3DCanvas.kt").read()
content = content.replace("private data class RenderPoly", "// tmp")
content = content.replace("import com.flylab.sim.SimulationSnapshot\n", "import com.flylab.sim.SimulationSnapshot\n\nprivate data class RenderPoly(val poly: com.flylab.render3d.Polygon3D, val verts: List<ProjectedPoint>, val meanDepth: Float)\n")
open("app/src/main/java/com/flylab/ui/components/Viewport3DCanvas.kt", "w").write(content)
