#!/bin/bash

echo "🚀 Hackathon Video - Startup Script"
echo "===================================="
echo ""

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. Subir Docker Compose
echo -e "${BLUE}📦 Iniciando Docker Compose...${NC}"
docker-compose up -d

echo -e "${YELLOW}⏳ Aguardando 10 segundos para containers iniciarem...${NC}"
sleep 10

# 2. Verificar status dos containers
echo -e "${BLUE}📊 Status dos containers:${NC}"
docker-compose ps

# 3. Criar bucket no LocalStack
echo ""
echo -e "${BLUE}🪣 Criando bucket S3 no LocalStack...${NC}"
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1

# Aguardar LocalStack estar pronto
sleep 5

# Criar bucket
aws --endpoint-url=http://localhost:4566 s3 mb s3://hackathon-videos-bucket 2>/dev/null && \
  echo -e "${GREEN}✅ Bucket criado com sucesso!${NC}" || \
  echo -e "${YELLOW}ℹ️  Bucket já existe${NC}"

# Listar buckets
echo ""
echo -e "${BLUE}📋 Buckets disponíveis:${NC}"
aws --endpoint-url=http://localhost:4566 s3 ls

echo ""
echo -e "${GREEN}✅ Infraestrutura pronta!${NC}"
echo ""
echo -e "${BLUE}🌐 Abrindo LocalStack Resource Browser...${NC}"
sleep 2
open "https://app.localstack.cloud/inst/default/resources" 2>/dev/null || \
  echo "Acesse manualmente: https://app.localstack.cloud/inst/default/resources"
echo ""
echo -e "Próximos passos:"
echo -e "  1. Rode a aplicação: ${BLUE}make run-app${NC} ou ${BLUE}./mvnw spring-boot:run -Dspring-boot.run.profiles=local${NC}"
echo -e "  2. Acesse o Swagger: ${BLUE}http://localhost:8081/swagger-ui.html${NC}"
echo ""
echo -e "Interfaces Web Disponíveis:"
echo -e "  - LocalStack Resource Browser: ${BLUE}https://app.localstack.cloud/inst/default/resources${NC}"
echo -e "  - LocalStack Health: ${BLUE}http://localhost:4566/_localstack/health${NC}"
echo -e "  - Swagger UI: ${BLUE}http://localhost:8081/swagger-ui.html${NC} (após rodar a app)"
echo ""
echo -e "Comandos úteis:"
echo -e "  - Ver logs: ${BLUE}docker-compose logs -f${NC}"
echo -e "  - Parar tudo: ${BLUE}docker-compose down${NC}"
echo -e "  - Listar buckets: ${BLUE}aws --endpoint-url=http://localhost:4566 s3 ls${NC}"
echo ""

