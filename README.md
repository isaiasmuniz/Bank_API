# 💰 Bank API

> API REST para simulação de operações bancárias, desenvolvida com **Java 21** e **Spring Boot**.

O projeto implementa o gerenciamento de **usuários e contas**, além de operações financeiras como **depósitos, saques e transferências entre contas**.

A API também possui documentação com **OpenAPI/Swagger**, implementação de **HATEOAS**, suporte a **JSON e XML**, testes unitários e testes de integração utilizando **Testcontainers**.

---

## 🚀 Tecnologias

- ☕ **Java 21**
- 🌱 **Spring Boot**
- 🌐 **Spring Web**
- 🗃️ **Spring Data JPA**
- 🔄 **Hibernate**
- 🐬 **MySQL**
- 📦 **Maven**
- 🔗 **Spring HATEOAS**
- 📖 **OpenAPI / Swagger**
- 🧪 **JUnit 5**
- 🎭 **Mockito**
- 🔍 **Rest Assured**
- 🐳 **Testcontainers**
- 🐋 **Docker**
- 🐋 **Docker Compose**
- 🔄 **Dozer Mapper**

---

## 📌 Objetivo do Projeto

O projeto foi desenvolvido com o objetivo de **praticar e demonstrar conhecimentos em desenvolvimento de APIs REST** utilizando o ecossistema **Java/Spring**.

### Principais conceitos aplicados

- Desenvolvimento de APIs REST
- Arquitetura em camadas
- DTOs
- Persistência com JPA/Hibernate
- Relacionamentos entre entidades
- Paginação
- HATEOAS
- Content Negotiation
- Tratamento global de exceções
- Testes unitários
- Testes de integração
- Testcontainers
- Docker
- Documentação de APIs com OpenAPI

---

## 📋 Funcionalidades

### 👤 Usuários

- Criar usuário
- Consultar usuário por ID
- Consultar todos os usuários
- Atualizar usuário
- Excluir usuário

### 🏦 Contas

- Criar conta
- Consultar conta por ID
- Bloquear conta
- Desbloquear conta
- Associar conta a um usuário

### 💸 Transações

- Realizar depósito
- Realizar saque
- Realizar transferência entre contas
- Validar saldo disponível
- Impedir operações com valores menores ou iguais a zero
- Impedir operações em contas bloqueadas
- Consultar histórico de transações
- Paginação do histórico de transações

---

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura baseada na **separação de responsabilidades** entre as principais camadas da aplicação:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

---

## 🔗 HATEOAS

A API utiliza **Spring HATEOAS** para adicionar links relacionados aos recursos retornados.

### Exemplo de resposta

```json
{
  "id": 1,
  "type": "deposit",
  "value": 100.00,
  "_links": {
    "deposit": {
      "href": "..."
    },
    "withdrawal": {
      "href": "..."
    },
    "viewHistory": {
      "href": "..."
    }
  }
}
```

Os recursos retornados pela API podem conter links para operações relacionadas, facilitando a **navegação entre os endpoints**.

---

## 📄 JSON e XML

Os endpoints da API suportam respostas nos formatos:

- `application/json`
- `application/xml`

O formato da resposta pode ser definido através do header `Accept`.

### JSON

```http
Accept: application/json
```

### XML

```http
Accept: application/xml
```

---

## 📖 Documentação da API

A documentação da API é disponibilizada utilizando **OpenAPI/Swagger**.

Após iniciar a aplicação, a interface do Swagger pode ser acessada através de:

```text
http://localhost:80/swagger-ui/index.html
```

A especificação OpenAPI também pode ser acessada através de:

```text
http://localhost:80/v3/api-docs
```

---

## 🐳 Executando com Docker

O projeto possui configuração para execução utilizando **Docker e Docker Compose**.

### Pré-requisitos

É necessário ter instalado:

- Docker
- Docker Compose

### ▶️ Subindo os containers

Na pasta que contém o arquivo `docker-compose.yml`, execute:

```bash
docker compose up -d
```

### 🔍 Verificando os containers

```bash
docker compose ps
```

### 📜 Visualizando os logs

```bash
docker compose logs -f
```

---

## 🧪 Testes

O projeto possui **testes unitários e testes de integração**.

### 🔬 Testes unitários

Os testes unitários utilizam:

- **JUnit 5**
- **Mockito**

As principais regras de negócio relacionadas a **usuários, contas e transações** são testadas isoladamente.

Para executar os testes:

```bash
mvn test
```

### 🧩 Testes de integração

Os testes de integração utilizam:

- **Spring Boot Test**
- **Testcontainers**
- **MySQL**
- **Rest Assured**

O MySQL utilizado nos testes é executado através de um **container**, permitindo testar a integração da aplicação com um banco de dados real sem depender de uma instalação local do MySQL.

Os testes também contemplam respostas nos formatos:

- JSON
- XML

---


## 🔄 Fluxo Básico

```text
Criar usuário
     ↓
Criar conta
     ↓
Realizar depósito
     ↓
Realizar saque
     ↓
Realizar transferência
     ↓
Consultar histórico
```

---

## 🛠️ Executando Localmente

Para executar a aplicação sem Docker, é necessário possuir:

- Java 21
- Maven
- MySQL

Entre no diretório da aplicação:

```bash
cd bank-Api-restFull
```

Execute:

```bash
mvn spring-boot:run
```

A aplicação será iniciada na porta configurada no `application.yaml`.

---

⭐ Projeto desenvolvido como parte do meu portfólio de desenvolvimento backend, com foco no ecossistema **Java e Spring**.