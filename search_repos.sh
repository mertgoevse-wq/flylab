#!/bin/bash
queries=("mcp server" "android design system" "compose ui" "kotlin testing" "flylab" "drosophila" "scientific visualization")
for q in "${queries[@]}"; do
  gh search repos "$q" --limit 20 --json fullName,description,stargazersCount --jq '.[] | [ .fullName, .description, .stargazersCount ] | @tsv' >> repos_raw.tsv
done
sort -t$'\t' -k3 -nr repos_raw.tsv | uniq > repos_sorted.tsv
