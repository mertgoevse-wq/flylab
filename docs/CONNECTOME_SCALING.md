# Scalable Connectome Architecture

## Overview
Drosophila melanogaster adult female brain connectome (FlyWire) consists of **139,255 neurons** and **~54.5 million synapses**. Storing or instantiating this complete graph concurrently within Android's JVM memory boundaries exceeds standard heap thresholds on devices like the Samsung Galaxy A56 or OnePlus 6T.

## Architecture Strategy

1. **Separation of Concerns:**
   - **Metadata & Bounding Boxes:** Lightweight metadata is embedded in the APK or synced fast.
   - **Regional Partitioning:** Neurons are partitioned by Neuropil (e.g., Mushroom Body, Antennal Lobe).
   - **Sparse Synaptic Streaming:** Synapse links are fetched asynchronously per active view frustum or ROI.

2. **Data Streaming Providers (`ConnectomeDataProvider`):**
   - **Local Embedded Provider:** Ships basic circuits (e.g. Chemotaxis pathway, ~200-500 nodes).
   - **Remote/Supabase Provider:** Streams large subgraphs with chunked pagination to protect against memory churn.

3. **Rendering & Simulation Decoupling:**
   - **LOD (Level of Detail):** Neurons far from camera are aggregated to neuropil centroid fields.
   - **Dynamic Unloading:** Regions outside simulation influence or sensory pathway are uninstantiated from memory.

4. **Security & Data Integrity:**
   - Public data access with strict read-only guarantees on reference graphs.
