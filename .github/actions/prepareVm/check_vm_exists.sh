#!/usr/bin/env bash
set -e

echo "test2"

source "$(dirname "$0")/common.sh"

echo "test3"

VM=$(fetch_vm "$LABEL")

echo "test4"

if [[ -n "$VM" ]]; then
  echo "VM found: $VM"
  echo "vm_exists=true" >> "$GITHUB_OUTPUT"
  echo "vm_details=$VM" >> "$GITHUB_OUTPUT"
else
  echo "No VM found with label: $LABEL"
  echo "vm_exists=false" >> "$GITHUB_OUTPUT"
fi
