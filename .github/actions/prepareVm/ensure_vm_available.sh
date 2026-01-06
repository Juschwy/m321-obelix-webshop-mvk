#!/usr/bin/env bash
set -e

source "$(dirname "$0")/common.sh"

VM=$(fetch_vm "$LABEL")

if [[ -n "$VM" ]]; then
  STATUS=$(echo "$VM" | jq -r '.status')
  while [[ "$STATUS" != "Deployed" ]]; do
    echo "VM status is '$STATUS'. Waiting for it to be 'Deployed'..."
    sleep 10
    VM=$(fetch_vm "$LABEL")
    STATUS=$(echo "$VM" | jq -r '.status')
    # TODO additionally check for ssh connectivity here (wireguard)
  done
  echo "VM is ready."
else
  echo "No VM found with label: $LABEL"
  exit 1
fi
