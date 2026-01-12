#!/usr/bin/env bash
set -e

source "$(dirname "$0")/common.sh"

login "$MAAS_USER" "$MAAS_PASSWORD"
