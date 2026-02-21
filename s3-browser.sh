#!/bin/bash

# Script para visualizar buckets S3 do LocalStack via terminal de forma bonita

echo "🪣 LocalStack S3 Browser (Terminal)"
echo "=================================="
echo ""

export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
ENDPOINT="http://localhost:4566"

echo "📊 Status do LocalStack:"
curl -s $ENDPOINT/_localstack/health | jq -r '.services.s3' | sed 's/^/   S3: /'
echo ""

echo "📦 Buckets Disponíveis:"
aws --endpoint-url=$ENDPOINT s3 ls | awk '{print "   📁", $3, "- Criado em:", $1, $2}'
echo ""

echo "📋 Detalhes dos Buckets:"
for bucket in $(aws --endpoint-url=$ENDPOINT s3 ls | awk '{print $3}'); do
    echo ""
    echo "   🪣 $bucket"
    echo "   ────────────────────────────────"

    # Contar objetos
    obj_count=$(aws --endpoint-url=$ENDPOINT s3 ls s3://$bucket --recursive 2>/dev/null | wc -l | xargs)
    echo "      Objetos: $obj_count"

    # Listar objetos se houver
    if [ "$obj_count" -gt 0 ]; then
        echo ""
        echo "      📄 Objetos:"
        aws --endpoint-url=$ENDPOINT s3 ls s3://$bucket --recursive --human-readable | \
            awk '{printf "         %s %s %s\n", $3, $4, $5}'
    fi
done

echo ""
echo "💡 Comandos úteis:"
echo "   aws --endpoint-url=$ENDPOINT s3 ls"
echo "   aws --endpoint-url=$ENDPOINT s3 ls s3://BUCKET-NAME --recursive"
echo "   aws --endpoint-url=$ENDPOINT s3 cp s3://BUCKET/FILE ./FILE"
echo ""

