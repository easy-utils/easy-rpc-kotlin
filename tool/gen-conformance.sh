#!/usr/bin/env bash
# Regenerate the conformance messages (pbandk) + client (easyrpc) for the KMP
# module. Usage: tool/gen-conformance.sh <proto-root> <proto-rel-path>
#
# The easy-rpc client comes from the unified plugin package
# (github.com/easy-utils/protoc-gen-easyrpc, installed as
# protoc-gen-easyrpc-kotlin on PATH). Messages come from protoc-gen-pbandk.
set -euo pipefail
ROOT="${1:-../easy-rpc-spec/proto}"
REL="${2:-easyrpc/conformance/v1/conformance.proto}"
HERE="$(cd "$(dirname "$0")/.." && pwd)"
PB_OUT="$HERE/src/commonMain/kotlin"
KT_OUT="$HERE/src/commonMain/kotlin/easyrpc"
OUT="$(mktemp -d)"
trap 'rm -rf "$OUT"' EXIT

protoc -I "$ROOT" \
  --plugin=protoc-gen-pbandk="$HERE/tool/protoc-gen-pbandk" \
  --pbandk_out="$PB_OUT" \
  "$REL"

protoc -I "$ROOT" \
  --easyrpc-kotlin_opt=pkg=easyrpc \
  --easyrpc-kotlin_out="$OUT" \
  "$REL"
cp "$OUT/${REL%.proto}_easyrpc.kt" "$KT_OUT/"
echo "regenerated messages -> $PB_OUT, client -> $KT_OUT"
