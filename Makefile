.PHONY: help setup start stop restart clean logs status build test run-app create-bucket list-buckets swagger localstack-health all

help: ## Mostra esta mensagem de ajuda
	@echo "🎯 Hackathon Video - Comandos Disponíveis"
	@echo "=========================================="
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'

## 🚀 Setup e Inicialização

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

start: ## Inicia os containers (MySQL + LocalStack)
	@echo "🐳 Iniciando containers..."
	docker-compose up -d
	@echo "⏳ Aguardando serviços ficarem prontos..."
	@sleep 5
	@docker-compose ps

stop: ## Para os containers
	@echo "🛑 Parando containers..."
	docker-compose down

restart: ## Reinicia os containers
	@echo "🔄 Reiniciando containers..."
	@docker-compose down
	@docker-compose up -d
	@sleep 5
	@docker-compose ps

clean: ## Remove containers, volumes e limpa build
	@echo "🧹 Limpando tudo..."
	docker-compose down -v
	./mvnw clean
	@echo "✅ Limpeza completa!"

## 📦 Gerenciamento de Build

build: ## Compila o projeto
	@echo "🔨 Compilando projeto..."
	./mvnw clean install -DskipTests

build-skip-tests: ## Compila sem executar testes
	./mvnw clean install -DskipTests

test: ## Executa os testes
	@echo "🧪 Executando testes..."
	./mvnw test

## 🏃 Execução da Aplicação

run-app: ## Roda a aplicação Spring Boot (perfil local)
	@echo "🚀 Iniciando aplicação (perfil: local)..."
	./mvnw spring-boot:run -Dspring-boot.run.profiles=local

run-app-prod: ## Roda a aplicação (perfil produção)
	@echo "🚀 Iniciando aplicação (perfil: produção)..."
	./mvnw spring-boot:run

all: setup build run-app ## Setup + Build + Run completo

## 🪣 Gerenciamento S3 (LocalStack)

create-bucket: ## Cria o bucket S3 no LocalStack
	@echo "📦 Criando bucket hackathon-videos-bucket..."
	@AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws --endpoint-url=http://localhost:4566 s3 mb s3://hackathon-videos-bucket 2>/dev/null || echo "✅ Bucket já existe"
	@AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws --endpoint-url=http://localhost:4566 s3 ls

list-buckets: ## Lista todos os buckets S3 do LocalStack
	@echo "📋 Buckets S3 (LocalStack):"
	@AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws --endpoint-url=http://localhost:4566 s3 ls

list-objects: ## Lista objetos no bucket hackathon-videos-bucket
	@echo "📄 Objetos no bucket:"
	@AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test aws --endpoint-url=http://localhost:4566 s3 ls s3://hackathon-videos-bucket --recursive --human-readable


localstack-health: ## Verifica status do LocalStack
	@echo "🏥 Status do LocalStack:"
	@curl -s http://localhost:4566/_localstack/health | jq '.' 2>/dev/null || curl -s http://localhost:4566/_localstack/health

## 📊 Monitoramento e Logs

status: ## Mostra status dos containers
	@echo "📊 Status dos Containers:"
	@docker-compose ps

logs: ## Mostra logs de todos os containers
	docker-compose logs -f

logs-db: ## Mostra logs do MySQL
	docker-compose logs -f db

logs-localstack: ## Mostra logs do LocalStack
	docker-compose logs -f localstack

logs-app: ## Mostra logs da aplicação Spring Boot (tail)
	@tail -f target/*.log 2>/dev/null || echo "⚠️  Aplicação não está rodando ou logs não encontrados"

## 🌐 Interfaces Web

swagger: ## Abre o Swagger UI no navegador
	@echo "🌐 Abrindo Swagger UI..."
	@open http://localhost:8081/swagger-ui.html 2>/dev/null || xdg-open http://localhost:8081/swagger-ui.html 2>/dev/null || echo "📱 Acesse: http://localhost:8081/swagger-ui.html"

## 🔍 Verificações

health-check: ## Verifica health de todos os serviços
	@echo "🏥 Verificando saúde dos serviços..."
	@echo ""
	@echo "📊 MySQL:"
	@docker exec hackathon-video-db mysqladmin ping -h localhost -uroot -proot 2>/dev/null && echo "  ✅ Rodando" || echo "  ❌ Não está rodando"
	@echo ""
	@echo "📊 LocalStack S3:"
	@curl -s http://localhost:4566/_localstack/health | jq -r '.services.s3' 2>/dev/null | grep -q "running" && echo "  ✅ Rodando" || echo "  ❌ Não está rodando"
	@echo ""
	@echo "📊 Aplicação Spring Boot:"
	@curl -s http://localhost:8081/actuator/health 2>/dev/null | jq -r '.status' 2>/dev/null | grep -q "UP" && echo "  ✅ Rodando" || echo "  ℹ️  Não está rodando (normal se não iniciou ainda)"

check-ports: ## Verifica quais portas estão em uso
	@echo "🔌 Portas em uso:"
	@echo "  3306 (MySQL):     " && (lsof -i :3306 | grep LISTEN > /dev/null && echo "✅ Em uso" || echo "❌ Livre")
	@echo "  4566 (LocalStack):" && (lsof -i :4566 | grep LISTEN > /dev/null && echo "✅ Em uso" || echo "❌ Livre")
	@echo "  8081 (App):       " && (lsof -i :8081 | grep LISTEN > /dev/null && echo "✅ Em uso" || echo "❌ Livre")

## 🛠️ Utilidades

db-cli: ## Abre terminal MySQL no container
	@echo "🗄️  Conectando ao MySQL..."
	docker exec -it hackathon-video-db mysql -uroot -proot hackathon-videos

db-reset: ## Reseta o banco de dados (CUIDADO: apaga todos os dados)
	@echo "⚠️  ATENÇÃO: Isso irá apagar TODOS os dados do banco!"
	@read -p "Tem certeza? [y/N] " -n 1 -r; \
	echo; \
	if [[ $$REPLY =~ ^[Yy]$$ ]]; then \
		docker-compose down -v; \
		docker-compose up -d db; \
		sleep 5; \
		echo "✅ Banco resetado!"; \
	else \
		echo "❌ Operação cancelada"; \
	fi

install-deps: ## Instala dependências Maven
	./mvnw dependency:resolve

update-deps: ## Atualiza dependências Maven
	./mvnw versions:display-dependency-updates
