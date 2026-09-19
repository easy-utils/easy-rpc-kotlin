#!/usr/bin/env bash
# Regenerate the conformance messages (pbandk) + client (easyrpc) for the KMP
# module. Usage: tool/gen-conformance.sh <proto-root> <proto-rel-path>
set -euo pipefail
ROOT="${1:-../easy-rpc-spec/proto}"
REL="${2:-easyrpc/conformance/v1/conformance.proto}"
HERE="$(cd "$(dirname "$0")/.." && pwd)"
PB_OUT="$HERE/src/commonMain/kotlin"
KT_OUT="$HERE/src/commonMain/kotlin/easyrpc"

protoc -I "$ROOT" \
  --plugin=protoc-gen-pbandk="$HERE/tool/protoc-gen-pbandk" \
  --pbandk_out="$PB_OUT" \
  "$REL"

EASYRPC_KT_PKG=easyrpc protoc -I "$ROOT" \
  --plugin=protoc-gen-easyrpc-kotlin="$HERE/tool/gen.py" \
  --easyrpc-kotlin_out="/tmp/opencode/kgen-out" \
  "$REL"
cp "/tmp/opencode/kgen-out/${REL%.proto}_easyrpc.kt" "$KT_OUT/"
echo "regenerated messages -> $PB_OUT, client -> $KT_OUT"
