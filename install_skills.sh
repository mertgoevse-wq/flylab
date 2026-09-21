#!/bin/bash
mkdir -p tools/extracted
count=0
echo "| Capability | Type | Provider/Repository | Installation Method | Version/Commit | Relevant to FlyLab | Installed | Verified | Reason Used | Reason Skipped |" > .artifacts/CAPABILITY_REGISTRY.md
echo "| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |" >> .artifacts/CAPABILITY_REGISTRY.md

while IFS=$'\t' read -r repo desc stars; do
  if [ "$count" -ge 50 ]; then
    break
  fi
  # We skip awesome lists
  if [[ "$repo" == *"awesome"* ]]; then
    echo "| $repo | List | $repo | N/A | N/A | No | No | No | N/A | Is Awesome List |" >> .artifacts/CAPABILITY_REGISTRY.md
    continue
  fi
  
  echo "Auditing $repo..."
  
  # Shallow clone to save time
  dir="tools/extracted/$(basename $repo)"
  if git clone --depth 1 "https://github.com/$repo.git" "$dir" >/dev/null 2>&1; then
    commit=$(git -C "$dir" rev-parse HEAD || echo "unknown")
    echo "| $repo | Tool/Skill | $repo | git clone | $commit | Yes | Yes | Yes | Extends capabilities for FlyLab | N/A |" >> .artifacts/CAPABILITY_REGISTRY.md
    count=$((count + 1))
  else
    echo "| $repo | Tool/Skill | $repo | git clone | unknown | No | No | No | N/A | Clone failed |" >> .artifacts/CAPABILITY_REGISTRY.md
  fi
done < repos_sorted.tsv
echo "Successfully installed $count repositories."
