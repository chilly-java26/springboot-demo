#!/bin/bash

# ============================================================
# 用途：生成一个 HMAC-SHA256 签名的 JWT（适合测试用）
# 用法：./generate-jwt.sh [secret] [subject] [expire_seconds]
# 示例：./generate-jwt.sh "your-secret" "admin" 3600
# ============================================================

set -e

# ---------- 参数 ----------
SECRET="${1:-your-256-bit-secret-key-for-book-management-api}"
SUBJECT="${2:-admin}"
EXPIRE_SECONDS="${3:-3600}"

# ---------- 生成 Header（固定） ----------
header='{"alg":"HS256","typ":"JWT"}'
header_b64=$(echo -n "$header" | openssl base64 -e | tr -d '\n=' | tr '/+' '_-')

# ---------- 生成 Payload ----------
current_epoch=$(date +%s)
exp_epoch=$((current_epoch + EXPIRE_SECONDS))

payload=$(cat <<EOF
{
  "sub": "$SUBJECT",
  "iat": $current_epoch,
  "exp": $exp_epoch
}
EOF
)
payload_b64=$(echo -n "$payload" | openssl base64 -e | tr -d '\n=' | tr '/+' '_-')

# ---------- 拼接前两部分 ----------
message="${header_b64}.${payload_b64}"

# ---------- 计算 HMAC-SHA256 签名 ----------
# 使用 openssl dgst -sha256 -hmac，然后 base64 编码
signature=$(echo -n "$message" | openssl dgst -sha256 -hmac "$SECRET" -binary | openssl base64 -e | tr -d '\n=' | tr '/+' '_-')

# ---------- 输出完整 JWT ----------
echo "${message}.${signature}"