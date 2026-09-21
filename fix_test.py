import re

with open("app/src/test/java/com/flylab/domain/ScientificProvenanceTest.kt", "r") as f:
    text = f.read()

text = text.replace('assertTrue(canonicalNames.contains("MODELED"))', 'assertTrue(canonicalNames.contains("MODELLED"))')
text = text.replace('assertTrue(canonicalNames.contains("HYPOTHESIS"))', 'assertTrue(canonicalNames.contains("HYPOTHETICAL"))')

# Let's add the other required terms to the test
extras = """
        assertTrue(canonicalNames.contains("OBSERVED"))
        assertTrue(canonicalNames.contains("EXPERIMENTAL"))
        assertTrue(canonicalNames.contains("CURATED"))
        assertTrue(canonicalNames.contains("IMPORTED"))
        assertTrue(canonicalNames.contains("INFERRED"))
        assertTrue(canonicalNames.contains("PREDICTED"))
        assertTrue(canonicalNames.contains("UNKNOWN"))
"""
text = text.replace('assertTrue(canonicalNames.contains("HYPOTHETICAL"))\n    }', 'assertTrue(canonicalNames.contains("HYPOTHETICAL"))' + extras + '    }')

with open("app/src/test/java/com/flylab/domain/ScientificProvenanceTest.kt", "w") as f:
    f.write(text)

