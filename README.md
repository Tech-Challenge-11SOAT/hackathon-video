# Hackathon Video - Microsserviço de Gerenciamento de Vídeos

Microsserviço responsável pelo gerenciamento e upload de vídeos para o AWS S3 com autenticação JWT.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 4.0.2**
- **MySQL 8.0**
- **AWS S3** (armazenamento de vídeos)
- **Flyway** (migrations)
- **MapStruct** (mapeamento de objetos)
- **Lombok**
- **JWT** (autenticação)
- **SpringDoc OpenAPI** (documentação)

## 📋 Pré-requisitos

- Java 17+
- Docker & Docker Compose
- Maven 3.6+
- AWS Account (ou LocalStack para testes locais)

## 🏗️ Arquitetura

O projeto segue os princípios de **Hexagonal Architecture (Ports & Adapters)**:

```
├── adapters/
│   ├── inbound/        # Controllers, DTOs, Exception Handlers
│   └── outbound/       # Repositories, S3 Adapter
├── application/
│   ├── ports/          # Interfaces (contratos)
│   ├── service/        # Implementação de casos de uso
│   └── usecases/       # Casos de uso
├── domain/             # Entidades e regras de negócio
│   ├── exception/
│   └── video/
└── infrastructure/     # Configurações (AWS, Security, etc)
```

## 🎯 Funcionalidades

- ✅ Upload de vídeos para AWS S3
- ✅ Armazenamento de metadados no MySQL
- ✅ Autenticação via JWT
- ✅ Validação de formato e tamanho (max 5GB)
- ✅ Suporte a formatos: mp4, avi, mkv, mov, flv, webm, m4v
- ✅ Documentação Swagger/OpenAPI
- ✅ Suporte a LocalStack para testes locais

## 🔧 Configuração

### 1. Clonar o repositório

```bash
git clone <repository-url>
cd hackathon-video
```

### 2. Subir banco de dados e LocalStack

```bash
docker-compose up -d
```

Isso irá subir:
- **MySQL** na porta `3306`
- **LocalStack S3** na porta `4566`

### 3. Criar bucket e iniciar aplicação

Use o script de startup:

```bash
./start.sh
```

Este script irá:
- Subir os containers
- Criar automaticamente o bucket `hackathon-videos-bucket`
- Abrir o LocalStack Resource Browser no navegador

### 4. Configurar variáveis de ambiente

#### Para uso com LocalStack (ambiente local):

```bash
export SPRING_PROFILES_ACTIVE=local
```

#### Para uso com AWS S3 real:

```bash
export AWS_REGION=us-east-1
export AWS_ACCESS_KEY_ID=sua-access-key
export AWS_SECRET_ACCESS_KEY=sua-secret-key
export AWS_S3_BUCKET_NAME=hackathon-videos-bucket
```

### 5. Executar a aplicação

```bash
./mvnw spring-boot:run
```

Ou com perfil local:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## 📖 Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/v3/api-docs
- **LocalStack Health**: http://localhost:4566/_localstack/health

### 🪣 Visualizar S3 (LocalStack)

**Opção 1: Via Terminal (FUNCIONA)** ✅
```bash
# Visualizador bonito com informações detalhadas
./s3-browser.sh

# ou via make
make s3-browser

# ou comandos AWS CLI diretamente
aws --endpoint-url=http://localhost:4566 s3 ls
aws --endpoint-url=http://localhost:4566 s3 ls s3://hackathon-videos-bucket --recursive
```

**Opção 2: LocalStack Resource Browser (Pode não funcionar)** ⚠️

O LocalStack Resource Browser (https://app.localstack.cloud) é um serviço **cloud externo** que geralmente **não consegue se conectar** ao seu LocalStack local devido a:
- Bloqueio de Mixed Content (HTTPS → HTTP)
- Impossibilidade de acessar localhost de um servidor externo

Se quiser tentar mesmo assim:
1. Acesse: https://app.localstack.cloud/inst/default/resources
2. Configure endpoint: `http://localhost:4566`
3. Credenciais: Access Key = `test`, Secret Key = `test`

**Recomendação:** Use `./s3-browser.sh` ou comandos AWS CLI diretos.

## 🔐 Autenticação

A API utiliza JWT Bearer Authentication. Você precisa obter um token do microsserviço `hackathon-auth` e incluir no header:

```
Authorization: Bearer <seu-token-jwt>
```

### Exemplo de requisição com cURL:

```bash
curl -X POST http://localhost:8081/videos/upload \
  -H "Authorization: Bearer eyJhbGc..." \
  -F "file=@/path/to/video.mp4"
```

## 📝 Endpoints Principais

### POST /videos/upload

Upload de vídeo

**Request:**
- `Content-Type: multipart/form-data`
- `file`: arquivo de vídeo

**Response:**
```json
{
  "videoId": "550e8400-e29b-41d4-a716-446655440000"
}
```

## 🗄️ Banco de Dados

### Tabela `videos`

| Campo              | Tipo      | Descrição                          |
|--------------------|-----------|------------------------------------|
| id                 | UUID      | ID único do vídeo                  |
| user_id            | UUID      | ID do usuário                      |
| original_filename  | VARCHAR   | Nome original do arquivo           |
| s3_video_key       | VARCHAR   | Caminho do vídeo no S3             |
| s3_zip_key         | VARCHAR   | Caminho do ZIP processado no S3    |
| status             | VARCHAR   | PENDING, PROCESSING, COMPLETED, FAILED |
| error_message      | TEXT      | Mensagem de erro (se houver)       |
| created_at         | TIMESTAMP | Data de criação                    |
| updated_at         | TIMESTAMP | Data de atualização                |

## 📦 Estrutura no S3

```
hackathon-videos-bucket/
  └── videos/
      └── {uuid}/
          └── {nome-arquivo-original}
```

Exemplo: `videos/550e8400-e29b-41d4-a716-446655440000/meu-video.mp4`

## 🧪 Testes

### Executar testes

```bash
./mvnw test
```

### Testar upload com LocalStack

1. Certifique-se que o LocalStack está rodando:
```bash
docker-compose ps
```

2. Execute a aplicação com perfil local:
```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

3. Faça upload de um vídeo via Swagger ou cURL

## 🐳 Docker

### Build da imagem

```bash
docker build -t hackathon-video:latest .
```

### Executar container

```bash
docker run -p 8081:8081 \
  -e AWS_REGION=us-east-1 \
  -e AWS_ACCESS_KEY_ID=test \
  -e AWS_SECRET_ACCESS_KEY=test \
  -e AWS_S3_BUCKET_NAME=hackathon-videos-bucket \
  hackathon-video:latest
```

## 📊 Monitoramento

### Logs

A aplicação utiliza SLF4J com Logback. Os logs incluem:
- Início e conclusão de uploads
- Erros de validação
- Erros de integração com S3

### Exemplo de log:

```
INFO  - Iniciando upload do vídeo para S3: videos/550e8400.../video.mp4
INFO  - Upload do vídeo concluído com sucesso: videos/550e8400.../video.mp4
INFO  - Vídeo criado com sucesso: 550e8400-e29b-41d4-a716-446655440000
```

## 🔒 Segurança

- ✅ Autenticação JWT obrigatória
- ✅ Validação de formato de arquivo
- ✅ Limite de tamanho de arquivo (5GB)
- ✅ Credenciais AWS via variáveis de ambiente
- ✅ Secrets não commitados no código

## 📚 Documentação Adicional

- [Configuração do AWS S3](docs/AWS_S3_CONFIG.md)
- [Diagramas de Arquitetura](docs/DIAGRAMS.md)

## 🛠️ Troubleshooting

### Erro: "Access Denied" no S3

- Verifique as credenciais AWS
- Confirme as permissões IAM
- Verifique o nome do bucket

### Erro: "Bucket does not exist"

- Crie o bucket no S3 ou execute o script de inicialização do LocalStack
- Verifique o nome do bucket no `application.yaml`

### Erro de conexão com MySQL

- Verifique se o container está rodando: `docker-compose ps`
- Verifique as credenciais no `application.yaml`

## 👥 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.

## 📞 Suporte

Para mais informações, consulte a documentação completa ou abra uma issue no repositório.

