#!/bin/bash

BASE_URL="http://localhost:8080/api/books"
TOKEN="admin-token"

echo "========== 0. 查询所有图书 =========="
curl -s -X GET "$BASE_URL" -H "token: $TOKEN" | jq '.'
echo "✅ List success"
echo ""

echo "========== 1. 创建图书（自动生成ID） =========="
RESP=$(curl -s -X POST "$BASE_URL" \
  -H "token: $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "动态ID书", "author": "Auto", "price": 45.0}')
echo "$RESP" | jq '.'

# 提取 data.id（假设返回格式为 {"code":200,"data":{"id":...} }）
BOOK_ID=$(echo "$RESP" | jq -r '.data.id')
if [ "$BOOK_ID" == "null" ] || [ -z "$BOOK_ID" ]; then
  echo "❌ 创建失败或无法解析ID"
  exit 1
fi
echo "✅ 获取到图书ID: $BOOK_ID"

echo ""
echo "========== 2. 查询该图书 =========="
curl -s -X GET "$BASE_URL/$BOOK_ID" -H "token: $TOKEN" | jq '.'

echo ""
echo "========== 3. 更新该图书 =========="
curl -s -X PUT "$BASE_URL/$BOOK_ID" \
  -H "token: $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "动态ID书（已改）", "author": "Auto2", "price": 55.0}' \
  | jq '.'

echo ""
echo "========== 4. 删除该图书 =========="
curl -s -X DELETE "$BASE_URL/$BOOK_ID" -H "token: $TOKEN" | jq '.'

echo ""
echo "========== 5. 再次查询（应返回错误） =========="
curl -s -X GET "$BASE_URL/$BOOK_ID" -H "token: $TOKEN" | jq '.'