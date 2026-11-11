# ReTech — DDD / CitizenTech | Global Solution FIAP 2026.2

## Identidade da CitizenTech

- **Nome:** ReTech — Tecnologia que ganha novas possibilidades
- **Local:** bairros de São Paulo, piloto a definir
- **Problema:** descarte inadequado de equipamentos eletrônicos e desigualdade no acesso à tecnologia
- **Beneficiários:** cidadãos que ganham recompensas e instituições que precisam de eletrônicos
- **Solução:** máquina recebe eletrônicos, atribui pontos por categoria e quantidade e vincula o descarte a instituições/parceiros
- **ODS:** 12 (Consumo e Produção Responsáveis), 10 (Redução das Desigualdades), 4 (Educação de Qualidade)

## DER oficial — 
| Tabela | Colunas | Relacionamentos |
|---|---|---|
| `T_USUARIO` | `id_usuario`, `nome_completo`, `cpf`, `pontos_saldo`, `data_cadastro` | 1 usuário realiza 0..N descartes |
| `T_CATEGORIA_ELETRO` | `id_categoria`, `nome_categoria`, `pontos_por_unidade` | 1 categoria em 0..N descartes |
| `T_MAQUINA` | `id_maquina`, `bairro_localizacao`, `cep`, `status_operacao` | 1 máquina em 0..N descartes |
| `T_INSTITUICAO` | `id_instituicao`, `nome_fantasia`, `cnpj`, `tipo_parceiro` | 1 instituição em 0..N descartes |
| `T_DESCARTE` | `id_descarte`, `id_usuario`, `id_maquina`, `id_categoria`, `id_instituicao`, `data_registro`, `quantidade`, `pontos_gerados` | quatro FKs obrigatórias |


### Regras de negócio atuais (modificáveis por decisão do grupo)

1. Cadastra-se usuário, categoria, máquina e instituição (as quatro chaves devem existir antes de um descarte).
2. Ao registrar um descarte, calcula-se `pontosGerados = quantidade × pontosPorUnidade` (pontuação fictícia acadêmica, não avaliação automática por IA).
3. `PONTOS_GERADOS` e `PONTOS_SALDO` estão limitados a 6 dígitos (`0..999999`); `quantidade` aceita `1..999`; `pontosPorUnidade` aceita `0..9999`.
4. O saldo do usuário é atualizado **na mesma transação** do descarte. Ao alterar a quantidade/categoria, credita-se somente a diferença; ao excluir, o total é estornado.
5. O `PUT /descartes/{id}` preserva o dono (`ID_USUARIO`) e a `DATA_REGISTRO`; o usuário associado não é trocado nessa primeira versão.
6. Os valores de categoria alterados no cadastro **não reavaliam automaticamente descartes antigos**. O histórico conserva `PONTOS_GERADOS` registrado.
7. O banco do DER **não tem status do descarte, identificação individual do aparelho, data de destinação nem histórico de resgate**. Por isso, *vincular um descarte a uma instituição não comprova que a doação/reciclagem ocorreu*. Não inventamos essas colunas.
8. `TIPO_PARCEIRO` e `STATUS_OPERACAO` são textos (`VARCHAR2`) sem enum no DER, portanto a API não força valores específicos nesta versão.

**Privacidade:** CPF e CNPJ nos exemplos são fictícios. O MVP não tem autenticação; use somente dados fictícios em testes e não publique uma API aberta com CPF real antes de implementar segurança/LGPD.

## Banco Oracle e senha

- Usuário Oracle de referência: `rm566198`.
- A senha: 300906
- O host, porta e service name do Oracle FIAP ainda não foram Definidos
- A variável `DB_USERNAME` possui `rm566198` como valor padrão e pode ser substituída.

Arquivo de exemplo: `.env.example` (a linha `DB_PASSWORD=` foi deixada vazia de propósito).

### Rodando no IntelliJ IDEA (recomendado)

1. Instale **JDK 21** e **Maven 3.9+**; abra a pasta do projeto no IntelliJ.
2. Use a conexão existente do **Oracle FIAP** no SQL Developer, verificando se as cinco tabelas já existem. Se não existirem, execute `src/main/resources/db/schema-oracle.sql` apenas uma vez.
3. Em **Run → Edit Configurations → Environment variables**, cadastre:
   - `DB_URL=jdbc:oracle:thin:@//SEU_HOST_FIAP:1521/SEU_SERVICE_NAME`
   - `DB_USERNAME=rm566198`
   - `DB_PASSWORD=300906`
4. Inicie `ReTechApplication` no IntelliJ ou `mvn spring-boot:run` no terminal com as mesmas variáveis exportadas.
5. Acesse `http://localhost:8080/actuator/health` e teste `requisicoes/api.http` em ordem.

## Endpoints REST disponíveis

| Recurso | GET | POST | PUT | DELETE |
|---|---|---|---|---|
| Usuários | `/api/v1/usuarios`, `/api/v1/usuarios/{id}` | `/api/v1/usuarios` | `/api/v1/usuarios/{id}` | `/api/v1/usuarios/{id}` |
| Categorias | `/api/v1/categorias`, `/api/v1/categorias/{id}` | `/api/v1/categorias` | `/api/v1/categorias/{id}` | `/api/v1/categorias/{id}` |
| Máquinas | `/api/v1/maquinas`, `/api/v1/maquinas/{id}` | `/api/v1/maquinas` | `/api/v1/maquinas/{id}` | `/api/v1/maquinas/{id}` |
| Instituições | `/api/v1/instituicoes`, `/api/v1/instituicoes/{id}` | `/api/v1/instituicoes` | `/api/v1/instituicoes/{id}` | `/api/v1/instituicoes/{id}` |
| Descartes | `/api/v1/descartes`, `/api/v1/descartes/{id}` | `/api/v1/descartes` | `/api/v1/descartes/{id}` | `/api/v1/descartes/{id}` |

Adicionais: `GET /api/v1/descartes?idUsuario=1`, `GET /api/v1/impacto`, `GET /actuator/health`.

Retornos típicos: `200` consulta/atualização; `201` criação; `204` exclusão; `400` validação; `404` não encontrado; `409` regra/conflito; `503` falha de banco.

## Estrutura

```text
ReTech_DDD_Oracle_DER_v2/
├── pom.xml
├── Dockerfile                    # build Maven + runtime Java 21
├── render.yaml                   # modelo, não publicado
├── .env.example                  # senha em branco
├── .gitignore                    # exclui .env
├── src/main/java/br/com/fiap/retech/
│   ├── model/                    # exatamente 5 entidades do DER
│   ├── dao/                      # 5 DAOs + OracleDAO + ImpactoDAO
│   ├── bo/                       # regras e transações
│   ├── dto/request/
│   ├── dto/response/
│   ├── controller/               # GET POST PUT DELETE
│   └── exception/                # erros padronizados
├── src/main/resources/
│   ├── application.yml
│   └── db/schema-oracle.sql
├── requisicoes/api.http
├── docs/                         # diagramas e especificação
└── prototipos/index.html         # protótipo visual estático
```

## Render e entrega

Deploy **não realizado**. Para o Render: conectar repositório do GitHub ao serviço Docker e preencher `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` **somente no painel privado**. A aplicação pode falhar no Render se o Oracle FIAP exigir VPN ou restringir conexões externas. Confirmar acessibilidade real antes de incluir link no PDF.


