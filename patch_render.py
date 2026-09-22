import re

content = open("app/src/main/java/com/flylab/ui/components/Viewport3DCanvas.kt").read()

old_code = """                val sortedPolys = anatomyPolygons.mapNotNull { poly ->
                    val projVerts = poly.vertices.map { camera.project(it, width, height) }
                    if (projVerts.any { !it.isVisible }) null
                    else {
                        val meanDepth = projVerts.map { it.depthZ }.average().toFloat()
                        Triple(poly, projVerts, meanDepth)
                    }
                }.sortedByDescending { it.third }

                for ((poly, verts, _) in sortedPolys) {"""

new_code = """                // Allocation-reduced Polygon projection and sorting
                val renderContext = mutableListOf<RenderPoly>()
                
                for (i in anatomyPolygons.indices) {
                    val poly = anatomyPolygons[i]
                    var allVisible = true
                    var depthSum = 0f
                    val projVerts = ArrayList<ProjectedPoint>(poly.vertices.size)
                    
                    for (j in poly.vertices.indices) {
                        val proj = camera.project(poly.vertices[j], width, height)
                        if (!proj.isVisible) {
                            allVisible = false
                            break
                        }
                        projVerts.add(proj)
                        depthSum += proj.depthZ
                    }
                    
                    if (allVisible && projVerts.isNotEmpty()) {
                        renderContext.add(RenderPoly(poly, projVerts, depthSum / projVerts.size))
                    }
                }
                
                renderContext.sortByDescending { it.meanDepth }

                for (item in renderContext) {
                    val poly = item.poly
                    val verts = item.verts"""

content = content.replace(old_code, new_code)
content = content.replace("package com.flylab.ui.components\n", "package com.flylab.ui.components\n\nprivate data class RenderPoly(val poly: com.flylab.render3d.Polygon3D, val verts: List<ProjectedPoint>, val meanDepth: Float)\n")

open("app/src/main/java/com/flylab/ui/components/Viewport3DCanvas.kt", "w").write(content)
