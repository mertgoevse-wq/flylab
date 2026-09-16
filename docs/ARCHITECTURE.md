# FlyLab Architecture

## Organism

Fly
- identity
- sex
- age
- genotype
- phenotype
- anatomy
- physiology
- nervousSystem
- brain
- memory
- learning
- motivation
- behavior
- environmentState

## Brain

Brain
- neurons
- synapses
- regions
- neuropils
- neurotransmitters
- activity
- modulation

## Neural activity

A neuron should support at minimum:

- membrane/activity state
- firing/activity level
- excitatory/inhibitory classification
- neurotransmitter metadata
- region
- outgoing connections
- incoming connections

The first implementation may use abstract population/state dynamics rather
than biophysically detailed Hodgkin-Huxley models.

## Neuromodulation

Create generic:

Neuromodulator
- id
- name
- concentration/state
- source region
- target regions
- release event
- decay model
- reward association

Initial implementation should support dopamine and other experimentally
supported Drosophila neuromodulators.

## Task

Task
- id
- environment
- objective
- observations
- actions
- reward
- termination
- score
- experiment metadata

## Experiment

Experiment
- organism
- genotype
- environment
- task
- parameters
- timestamps
- neural activity
- behavior
- rewards
- events
- outcome

## Visualization

The renderer must support:

- organism mode
- anatomy mode
- nervous-system mode
- brain mode
- neuron mode
- connectome mode
- activity mode

Activity should be visualizable both:

1. inside the brain viewer
2. spatially projected onto the fly model

## Data provenance

Every scientific dataset gets:

- source
- version
- license
- retrieval date
- transformation steps
- local representation

Never silently modify source scientific datasets.
