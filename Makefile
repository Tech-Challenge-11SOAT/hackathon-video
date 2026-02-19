.PHONY: help setup start stop restart logs create-bucket run-app clean

help: ## Mostra esta mensagem de ajuda
	@echo "Comandos disponíveis:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

setup: ## Setup completo: sobe containers e cria bucket
	@echo "🚀 Iniciando setup completo..."
	docker-compose up -d
	@echo "⏳ Aguardando LocalStack estar pronto..."
	@sleep 12
	@echo "📦 Criando bucket S3..."
	@AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws --endpoint-url=http://localhost:4566 s3 mb s3://hackathon-videos-bucket 2>/dev/null || echo "✅ Bucket já existe"
	@echo "📋 Listando buckets..."
	@AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws --endpoint-url=http://localhost:4566 s3 ls
	@echo "✅ Setup completo!"

start: ## Inicia os containers
	docker-compose up -d
	@echo "⏳ Aguardando serviços ficarem prontos..."
	@sleep 5
	docker-compose ps

stop: ## Para os containers
	docker-compose down

restart: stop start ## Reinicia os containers

logs: ## Mostra logs dos containers
	docker-compose logs -f

logs-db: ## Mostra logs do MySQL
	docker-compose logs -f db

logs-s3: ## Mostra logs do LocalStack
	docker-compose logs -f localstack

create-bucket: ## Cria o bucket S3 no LocalStack
	@echo "📦 Criando bucket hackathon-videos-bucket..."
	@AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws --endpoint-url=http://localhost:4566 s3 mb s3://hackathon-videos-bucket 2>/dev/null || echo "✅ Bucket já existe"
	@AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws --endpoint-url=http://localhost:4566 s3 ls

list-buckets: ## Lista buckets S3 no LocalStack
	@AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws --endpoint-url=http://localhost:4566 s3 ls

run-app: ## Roda a aplicação Spring Boot
	./mvnw spring-boot:run -Dspring-boot.run.profiles=local

run-app-prod: ## Roda a aplicação com perfil de produção
	./mvnw spring-boot:run

build: ## Compila o projeto
	./mvnw clean install -DskipTests

test: ## Executa os testes
	./mvnw test

clean: ## Remove containers e volumes
	docker-compose down -v
	./mvnw clean

status: ## Mostra status dos containers
	@echo "📊 Status dos containers:"
	@docker-compose ps

all: setup build run-app ## Setup completo + build + run

swagger: ## Abre o Swagger UI
	@echo "🌐 Abrindo Swagger UI..."
	@open http://localhost:8081/swagger-ui.html || xdg-open http://localhost:8081/swagger-ui.html || echo "Acesse: http://localhost:8081/swagger-ui.html"

s3-browser: ## Visualiza buckets S3 do LocalStack (via terminal)
	@./s3-browser.sh

localstack-ui: ## Tenta abrir LocalStack Resource Browser (pode não funcionar)
	@echo "⚠️  AVISO: O Resource Browser cloud pode não se conectar ao localhost"
	@echo "💡 Use 'make s3-browser' para visualizar via terminal"
	@echo ""
	@open "https://app.localstack.cloud/inst/default/resources" || xdg-open "https://app.localstack.cloud/inst/default/resources" || echo "Acesse: https://app.localstack.cloud/inst/default/resources"

localstack-health: ## Verifica health do LocalStack
	@curl -s http://localhost:4566/_localstack/health | jq '.' || curl http://localhost:4566/_localstack/health

