# FlyLab Design System

## Core Philosophy
Scientific, restrained, clear, premium, and functional.

- **Objective over Decorative:** Data must be clear. No superfluous styling (glassmorphism, neon pop).
- **Legibility under Load:** Information architecture scales down. When loading a dense connectome representation, the UI should stay crisp and neutral.
- **Evidence Labeling:** Visual encoding must allow a researcher to trace simulation components to biology or model abstractions.

## Typography
- **Primary Font:** Inter or Roboto (Android default sans-serif).
- **Numbers/Data:** Tabular numerals for timeline labels, counts, metrics, or statistical overlays.
- **Headings:** Bold and balanced; readable hierarchies using size instead of intense color.

## Color System
- **Accent:** Emerald/Teal scale, reflecting scientific neutrality with biological undertones.
- **Backgrounds:** Slate (light theme: #F8FAFC, dark theme: #0F172A).
- **Data Encodings:**
  - Categorical distinct hues for different fly modalities (sensory vs motor vs associative).
  - Status logic (Good/Warning/Critical) mapped specifically to experiment integrity and errors.

## Components (Compose)
### `FlyLabRootScreen`
Top-level scaffold housing lateral navigation between:
- Brain Explorer
- Live Behavioral Sim
- Experiment Configuration
- Fly Setup (Genotype/Agent ID)

### `EmptyStates`
A standardized empty state component conveying a scientific message (e.g., "Connectome subset unselected", "No experiment recording found").

### `ExperimentTimeline` (To be implemented)
A bottom or side panel charting simulation steps, logging stimuli delivery securely and deterministically against temporal progress.

## Accessibility
- Contrast ratios rigorously managed (min 4.5:1 text/bg).
- Touch target minimum 48dp on actionable icons/buttons.
- Semantic `contentDescription` mappings across visual widgets.
