import re

with open("app/src/main/java/com/flylab/domain/model/ScientificProvenance.kt", "r") as f:
    text = f.read()

# Replace references in the companion object
text = text.replace('ProvenanceLevel.PUBLISHED', 'ProvenanceLevel.PUBLISHED')
text = text.replace('ProvenanceLevel.DERIVED', 'ProvenanceLevel.DERIVED')
text = text.replace('ProvenanceLevel.MODELED', 'ProvenanceLevel.MODELLED')
text = text.replace('ProvenanceLevel.HYPOTHESIS', 'ProvenanceLevel.HYPOTHETICAL')

replacement = """enum class ProvenanceLevel(
    val canonicalName: String,
    val germanName: String,
    val description: String,
    val isEmpiricallyVerified: Boolean
) {
    OBSERVED("OBSERVED", "BEOBACHTET", "Beobachtet, aber nicht quantifiziert", true),
    MEASURED("MEASURED", "GEMESSEN", "Experimentell gemessen", true),
    PUBLISHED("PUBLISHED", "PUBLIZIERT", "In Fachliteratur publiziert", true),
    EXPERIMENTAL("EXPERIMENTAL", "EXPERIMENTELL", "Experimentell belegt", true),
    CURATED("CURATED", "KURATIERT", "Kuratiert aus Literatur", true),
    IMPORTED("IMPORTED", "IMPORTIERT", "Aus externem Datensatz importiert", true),
    DERIVED("DERIVED", "ABGELEITET", "Aus Messdaten mathematisch berechnet", true),
    MODELLED("MODELLED", "MODELLIERT", "Berechnetes Modell", false),
    SIMULATED("SIMULATED", "SIMULIERT", "Während der Simulation berechnet", false),
    INFERRED("INFERRED", "GESCHLUSSFOLGERT", "Logisch oder indirekt abgeleitet", false),
    PREDICTED("PREDICTED", "VORHERGESAGT", "Vorhergesagt durch Modell", false),
    HYPOTHETICAL("HYPOTHETICAL", "HYPOTHETISCH", "Wissenschaftliche Annahme ohne Daten", false),
    UNKNOWN("UNKNOWN", "UNBEKANNT", "Herkunft unbekannt", false);

    companion object {
        fun fromString(value: String): ProvenanceLevel {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                it.canonicalName.equals(value, ignoreCase = true) ||
                it.germanName.contains(value, ignoreCase = true)
            } ?: UNKNOWN
        }
    }
}

/**"""

pattern = re.compile(r'enum class ProvenanceLevel.*?}\n\n/\*\*', re.DOTALL)
new_text = pattern.sub(replacement, text)

with open("app/src/main/java/com/flylab/domain/model/ScientificProvenance.kt", "w") as f:
    f.write(new_text)

