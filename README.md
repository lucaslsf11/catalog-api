# Catalog API — RESTful Service

API RESTful completa desenvolvida com **Java 21** e **Spring Boot 3**, estruturada em arquitetura de camadas (*Layered Architecture*) para gestão de catálogo de produtos e categorias.

---

## Tecnologias e Recursos

- **Java 21** (LTS) & **Spring Boot 3**
- **Spring Data JPA & Hibernate** (Mapeamento Objeto-Relacional)
- **H2 In-Memory Database** (Banco em memória com console web habilitado)
- **Bean Validation (Hibernate Validator)** (Validação rigorosa de dados de entrada)
- **Java Records** (DTOs imutáveis de alto desempenho)
- **SpringDoc OpenAPI / Swagger UI** (Documentação interativa e testes de contrato)
- **Tratamento Global de Exceções** (`@RestControllerAdvice` com respostas semânticas RFC 7807)
- **Lombok** (Redução de código boilerplate em entidades)

---

## Arquitetura em Camadas

A aplicação adota separação estrita de responsabilidades:

```text
src/main/java/com/portfolio/catalog/
├── controller/     # Endpoints HTTP REST (rotas, serialização, respostas HTTP)
├── dto/            # Data Transfer Objects (Records de Request/Response e validações)
├── service/        # Regras de negócio, transações (@Transactional) e orquestração
├── model/          # Entidades de domínio JPA mapeadas para tabelas relacionais
├── repository/     # Interfaces de persistência com Spring Data JPA
├── exception/      # Exceções customizadas e interceptador global (@RestControllerAdvice)
└── config/         # Configurações de documentação OpenAPI e carga inicial de dados
```
---

## Como Executar a Aplicação

### Pré-requisitos
- **JDK 21** instalado e configurado nas variáveis de ambiente (`JAVA_HOME`)
- **Git** instalado
- **Maven** (opcional, pois o projeto já inclui o executável Maven Wrapper)

### Passo a passo para execução

1. **Clonar o repositório:**
   ```bash
   git clone [https://github.com/lucaslsf11/catalog-api.git](https://github.com/lucaslsf11/catalog-api.git)
   cd catalog-api
   ```

2. **Compilar e rodar via Maven Wrapper:**
    - **No Windows (PowerShell / Prompt de Comando):**
      ```powershell
      .\mvnw.cmd spring-boot:run
      ```
    - **No Linux / macOS:**
      ```bash
      ./mvnw spring-boot:run
      ```

3. **Ou rodar diretamente pelo IntelliJ IDEA:**
    - Abra o projeto clonado na IDE.
    - Navegue até `src/main/java/com/portfolio/catalog/CatalogApiApplication.java`.
    - Clique no botão verde de **Run (Play)** ao lado da classe.

A API estará acessível em: `http://localhost:8080`

---

## Documentação da API (Swagger UI)

Com a aplicação rodando, acesse a interface interativa do Swagger para consultar os esquemas e disparar requisições em tempo real:

**[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Console do Banco H2 (Acesso Web)
- **URL de acesso:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:catalogdb`
- **User:** `sa`
- **Password:** *(deixar em branco)*

---

## Endpoints Principais

### Categorias (`/api/v1/categories`)
| Método | Endpoint | Descrição | Status Sucesso |
|---|---|---|---|
| `GET` | `/api/v1/categories` | Lista todas as categorias cadastradas | `200 OK` |
| `GET` | `/api/v1/categories/{id}` | Busca os detalhes de uma categoria por ID | `200 OK` |
| `POST` | `/api/v1/categories` | Cadastra uma nova categoria | `201 Created` |
| `PUT` | `/api/v1/categories/{id}` | Atualiza uma categoria existente | `200 OK` |
| `DELETE` | `/api/v1/categories/{id}` | Remove uma categoria | `204 No Content` |

### Produtos (`/api/v1/products`)
| Método | Endpoint | Descrição | Status Sucesso |
|---|---|---|---|
| `GET` | `/api/v1/products` | Lista produtos paginados (`page`, `size`, `sort`) | `200 OK` |
| `GET` | `/api/v1/products/{id}` | Busca os detalhes de um produto por ID | `200 OK` |
| `GET` | `/api/v1/products/category/{categoryId}` | Lista produtos filtrados por categoria | `200 OK` |
| `POST` | `/api/v1/products` | Cadastra um novo produto | `201 Created` |
| `PUT` | `/api/v1/products/{id}` | Atualiza os dados de um produto | `200 OK` |
| `DELETE` | `/api/v1/products/{id}` | Remove um produto | `204 No Content` |