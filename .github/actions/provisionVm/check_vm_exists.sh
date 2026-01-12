#!/usr/bin/env bash
set -e

source "$(dirname "$0")/common.sh"

VM=$(fetch_vm "$LABEL")

if [[ -n "$VM" ]]; then
  echo "VM found: $VM"
  echo "vm_exists=true" >> "$GITHUB_OUTPUT"
  echo "vm_details=$VM" >> "$GITHUB_OUTPUT"
else
  echo "No VM found with label: $LABEL"
  echo "vm_exists=false" >> "$GITHUB_OUTPUT"
fi
