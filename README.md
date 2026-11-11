# ReTech

**Tecnologia que ganha novas possibilidades.**

A ReTech é uma plataforma de descarte de resíduos eletrônicos que conecta cidadãos, pontos de coleta e instituições parceiras. Cada descarte registrado gera pontos conforme a categoria e a quantidade de equipamentos, incentivando a reciclagem, o reuso e a destinação responsável.

##Deploy

https://retech-api-13j1.onrender.com

## Funcionalidades

- Cadastro e login com CPF e senha.
- Consulta de perfil e saldo de pontos.
- Registro de descartes por categoria, máquina e instituição.
- Cálculo automático e atualização dos pontos a cada descarte.
- Histórico de descartes por usuário.
- Alteração de senha e encerramento de sessão.
- Gerenciamento de usuários, categorias, máquinas, instituições e descartes por meio de API REST.

## Tecnologias

- Java 26
- Spring Boot 4.1.2
- Spring JDBC
- Oracle Database
- HTML, CSS e JavaScript
- Maven

## Estrutura

```text
src/main/java/br/com/fiap/retech/
├── bo/
├── config/
├── controller/
├── dao/
├── dto/
├── exception/
├── model/
└── security/

src/main/resources/
├── static/
│   ├── index.html
│   ├── style.css
│   └── app.js
└── application.yml
```

## Banco de dados

O Oracle armazena os usuários, as categorias de eletrônicos, as máquinas de coleta, as instituições parceiras e os descartes nas tabelas `T_USUARIO`, `T_CATEGORIA_ELETRO`, `T_MAQUINA`, `T_INSTITUICAO` e `T_DESCARTE`.

A pontuação é calculada por `quantidade × pontos_por_unidade`. O saldo do usuário é atualizado na mesma transação do registro do descarte.

## API

| Recurso | Endpoint | Operações |
|---|---|---|
| Autenticação | `/api/v1/auth` | Cadastro, login, sessão, troca de senha e logout |
| Usuários | `/api/v1/usuarios` | GET, POST, PUT, DELETE |
| Categorias | `/api/v1/categorias` | GET, POST, PUT, DELETE |
| Máquinas | `/api/v1/maquinas` | GET, POST, PUT, DELETE |
| Instituições | `/api/v1/instituicoes` | GET, POST, PUT, DELETE |
| Descartes | `/api/v1/descartes` | GET, POST, PUT, DELETE |
| Impacto | `/api/v1/impacto` | GET |

## Execução

**Requisitos:** JDK 26, Maven e acesso ao Oracle Database.

Configure a conexão com o banco em `src/main/resources/application.yml` e execute:

```bash
mvn spring-boot:run
```

- Interface: `http://localhost:8080/`
- API: `http://localhost:8080/api/v1/`
- Verificação de saúde: `http://localhost:8080/actuator/health`
