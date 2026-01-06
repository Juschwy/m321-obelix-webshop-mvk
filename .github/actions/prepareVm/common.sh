#!/usr/bin/env bash

# Common constants
LABEL="obelix-vm"
MAAS_URL="https://maas.bbw-it.ch/maas-user.php"
COOKIES_FILE="cookies.txt"

# Login function
login() {
  local user="$1"
  local password="$2"
  curl -k -c "$COOKIES_FILE" -X POST "$MAAS_URL" \
    --data-raw "login=$user&passwd=$password"
  while IFS= read -r line; do
    echo "::add-mask::$line"
  done < "$COOKIES_FILE"
}

# Fetch VM function
fetch_vm() {
  local label="$1"
  local json
  json=$(curl -sk -b "$COOKIES_FILE" "$MAAS_URL?list")
  echo "$json" | jq -c --arg label "$label" '.[] | select(.label == $label)'
}

# Create VM function
create_vm() {
  local label="$1"
  local script_path="$2"
  local script_encoded
  script_encoded=$(jq -sRr @uri < "$script_path")
  curl -k -b "$COOKIES_FILE" "$MAAS_URL?create" \
    --data "class=5IA22c&teacher=prutschmann&label=${label}&lifetime=3&image=ubuntu%2Fnoble&script=${script_encoded}"
}

# Delete VM function
delete_vm() {
  local systemid="$1"
  curl -k -b "$COOKIES_FILE" "$MAAS_URL?action=release&systemid=${systemid}"
}

# Extend lifetime for 2 hours (max 8 hrs from now)
extend_vm_lifetime() {
  local systemid="$1"
  curl -k -b "$COOKIES_FILE" "$MAAS_URL?action=extend&systemid=${systemid}"
}

