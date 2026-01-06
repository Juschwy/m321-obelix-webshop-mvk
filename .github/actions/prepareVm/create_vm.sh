#!/usr/bin/env bash

set -e

source "$(dirname "$0")/common.sh"

create_vm "$LABEL" "${GITHUB_WORKSPACE:-.}/cicd/cloud-init.yml"
