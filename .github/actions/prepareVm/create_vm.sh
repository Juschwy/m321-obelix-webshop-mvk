#!/usr/bin/env bash

set -e

source "$(dirname "$0")/common.sh"

create_vm "$LABEL" "../../../cicd/cloud-init.yml"
