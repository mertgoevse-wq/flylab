import re

with open("app/src/main/java/com/flylab/ui/components/ProvenanceBadge.kt", "r") as f:
    text = f.read()

# Replace the first `when` block
old_when_1 = """    val bgColor = when (evidence.level) {
        ProvenanceLevel.MEASURED -> Color(0xFF059669)   // Emerald
        ProvenanceLevel.PUBLISHED -> Color(0xFF0284C7)  // Sky Blue
        ProvenanceLevel.DERIVED -> Color(0xFF6366F1)    // Indigo
        ProvenanceLevel.MODELLED -> Color(0xFFD97706)    // Amber
        ProvenanceLevel.SIMULATED -> Color(0xFF0D9488)  // Teal
        ProvenanceLevel.HYPOTHETICAL -> Color(0xFFDC2626) // Red
    }"""

new_when_1 = """    val bgColor = when (evidence.level) {
        ProvenanceLevel.OBSERVED -> Color(0xFF059669)
        ProvenanceLevel.MEASURED -> Color(0xFF059669)   // Emerald
        ProvenanceLevel.PUBLISHED -> Color(0xFF0284C7)  // Sky Blue
        ProvenanceLevel.EXPERIMENTAL -> Color(0xFF0284C7)
        ProvenanceLevel.CURATED -> Color(0xFF0284C7)
        ProvenanceLevel.IMPORTED -> Color(0xFF6366F1)
        ProvenanceLevel.DERIVED -> Color(0xFF6366F1)    // Indigo
        ProvenanceLevel.MODELLED -> Color(0xFFD97706)    // Amber
        ProvenanceLevel.SIMULATED -> Color(0xFF0D9488)  // Teal
        ProvenanceLevel.INFERRED -> Color(0xFFD97706)
        ProvenanceLevel.PREDICTED -> Color(0xFFD97706)
        ProvenanceLevel.HYPOTHETICAL -> Color(0xFFDC2626) // Red
        ProvenanceLevel.UNKNOWN -> Color(0xFF6B7280)    // Gray
    }"""
text = text.replace(old_when_1, new_when_1)

old_when_2 = """                            text = when (evidence.level) {
                                ProvenanceLevel.MEASURED -> "Dieser Wert wurde im echten Labor an einer echten Taufliege unter dem Elektronenmikroskop direkt gezählt."
                                ProvenanceLevel.PUBLISHED -> "Dieser Mechanismus wurde von Wissenschaftlern in Fachartikeln untersucht und genau beschrieben."
                                ProvenanceLevel.DERIVED -> "Dieser Wert wurde mathematisch aus echten Messungen des Fliegengehirns ausgerechnet."
                                ProvenanceLevel.MODELLED -> "Das ist ein vereinfachtes Computermodell, das sich an echten biologischen Prinzipien orientiert."
                                ProvenanceLevel.SIMULATED -> "Dieser Wert wird dynamisch während des laufenden Simulationsflugs in Echtzeit berechnet."
                                ProvenanceLevel.HYPOTHETICAL -> "Das ist eine Annahme oder Vermutung, für die es noch keine sicheren Messdaten gibt."
                            },"""

new_when_2 = """                            text = when (evidence.level) {
                                ProvenanceLevel.OBSERVED -> "Dieser Wert wurde beobachtet, aber noch nicht quantifiziert."
                                ProvenanceLevel.MEASURED -> "Dieser Wert wurde im echten Labor an einer echten Taufliege unter dem Elektronenmikroskop direkt gezählt."
                                ProvenanceLevel.PUBLISHED -> "Dieser Mechanismus wurde von Wissenschaftlern in Fachartikeln untersucht und genau beschrieben."
                                ProvenanceLevel.EXPERIMENTAL -> "Dieser Zusammenhang ist experimentell belegt."
                                ProvenanceLevel.CURATED -> "Dieser Wert wurde aus der Fachliteratur kuratiert."
                                ProvenanceLevel.IMPORTED -> "Dieser Wert stammt aus einem externen Datensatz."
                                ProvenanceLevel.DERIVED -> "Dieser Wert wurde mathematisch aus echten Messungen des Fliegengehirns ausgerechnet."
                                ProvenanceLevel.MODELLED -> "Das ist ein vereinfachtes Computermodell, das sich an echten biologischen Prinzipien orientiert."
                                ProvenanceLevel.SIMULATED -> "Dieser Wert wird dynamisch während des laufenden Simulationsflugs in Echtzeit berechnet."
                                ProvenanceLevel.INFERRED -> "Dieser Wert ist logisch oder indirekt abgeleitet."
                                ProvenanceLevel.PREDICTED -> "Dieser Wert wird durch ein Modell vorhergesagt."
                                ProvenanceLevel.HYPOTHETICAL -> "Das ist eine Annahme oder Vermutung, für die es noch keine sicheren Messdaten gibt."
                                ProvenanceLevel.UNKNOWN -> "Herkunft unbekannt."
                            },"""
text = text.replace(old_when_2, new_when_2)

with open("app/src/main/java/com/flylab/ui/components/ProvenanceBadge.kt", "w") as f:
    f.write(text)
