#!/usr/bin/env bash
set -e

source "$(dirname "$0")/common.sh"

echo "Updating DDNS record for obelix.liuuner.ch to IP: $DNS_IP"
update_ddns "$DNS_USER" "$DNS_PASSWORD" "$DNS_IP"