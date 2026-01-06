#!/usr/bin/env bash
set -e

source "$(dirname "$0")/common.sh"

VM=$(fetch_vm "$LABEL")

if [[ -n "$VM" ]]; then
  STATUS=$(echo "$VM" | jq -r '.status')
  MAX_RETRIES=${MAX_RETRIES:-60}
  RETRY_COUNT=0
  while [[ "$STATUS" != "Deployed" ]]; do
    echo "VM status is '$STATUS'. Waiting for it to be 'Deployed'..."
    sleep 10
    VM=$(fetch_vm "$LABEL")
    STATUS=$(echo "$VM" | jq -r '.status')
    RETRY_COUNT=$((RETRY_COUNT + 1))
    if (( RETRY_COUNT >= MAX_RETRIES )); then
      echo "Timed out waiting for VM with label '$LABEL' to reach status 'Deployed' after $RETRY_COUNT attempts."
      exit 1
    fi
    # TODO additionally check for ssh connectivity here (wireguard)
  done
else
  echo "No VM found with label: $LABEL"
  exit 1
fi
