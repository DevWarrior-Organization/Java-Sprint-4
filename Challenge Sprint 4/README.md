# DevWarrior — API de Mobilidade e Sustentabilidade (Challenge Sprint 4)

API RESTful desenvolvida em **Java 21 + Spring Boot 4**, banco de dados **Oracle (FIAP)**,
evolução do projeto de console da Sprint 3. Permite cadastrar usuários, registrar viagens
sustentáveis (ônibus, metrô, trem, bicicleta), calcular pontos por quilômetro,
manter o saldo de cada usuário e exibir um ranking geral.

## Stack

| Camada | Tecnologia |
|---|---|
| Modelo (beans) | POJOs Java (`Usuario`, `Viagem`, `ViagemRequest`) |
| Acesso a dados | DAO com JDBC + padrão Connection Factory (`ConnectionFactory`) |
| Regras de negócio | BO (`UsuarioBO`, `ViagemBO`, `RankingBO`) |
| API REST | Spring Boot (Resource/Controllers) + `CorsFilter` |
| Banco | Oracle Database (driver `ojdbc17`) |

## Estrutura do projeto

```
src/main/java/br/com/fiap/
├── beans/       → Usuario, Viagem, ViagemRequest (camada modelo)
├── conexoes/    → ConnectionFactory (padrão Connection Factory)
├── dao/         → UsuarioDAO, ViagemDAO (padrão DAO)
├── bo/          → UsuarioBO, ViagemBO, RankingBO (validações e regras)
├── resource/    → UsuarioResource, ViagemResource, RankingResource, HomeController
├── config/      → CorsFilter, InicializadorBanco
└── exception/   → GlobalExceptionHandler + exceções customizadas
```

## Como executar

Requisitos: **JDK 21+** (testado com JDK 21 e 24). Não é necessário instalar Maven — o projeto inclui o *Maven Wrapper*.

```bash
.\mvnw.cmd spring-boot:run      # execute na raiz do projeto (pasta onde esta o pom.xml)
```

A API sobe em `http://localhost:8080`.

### Variáveis de ambiente (opcionais)

| Variável | Padrão | Uso |
|---|---|---|
| `DB_HOST` | `oracle.fiap.com.br` | Host do Oracle |
| `DB_USER` | `RM573332` | Usuário do banco |
| `DB_PASSWORD` | senha FIAP | Senha do banco |
| `PORT` | `8080` | Porta HTTP (definida automaticamente no Render) |

No deploy em nuvem, configure `DB_USER` e `DB_PASSWORD` nas variáveis de ambiente do servidor.

## Endpoints principais

| Verbo | URI | Descrição |
|---|---|---|
| GET | `/usuarios` | Lista usuários |
| GET | `/usuarios/{id}` | Busca usuário por ID |
| POST | `/usuarios` | Cadastra usuário |
| PUT | `/usuarios/{id}` | Atualiza usuário |
| DELETE | `/usuarios/{id}` | Exclui usuário e suas viagens |
| GET | `/viagens` | Lista viagens |
| GET | `/viagens/{id}` | Busca viagem por ID |
| GET | `/viagens/usuario/{usuarioId}` | Viagens de um usuário |
| GET | `/viagens/fatores` | Fatores de pontuação por modal |
| POST | `/viagens` | Registra viagem (calcula pontos e credita saldo) |
| PUT | `/viagens/{id}` | Atualiza viagem (recalcula pontos e saldo) |
| DELETE | `/viagens/{id}` | Exclui viagem (debita pontos) |
| GET | `/ranking` | Ranking por pontuação |

A documentação completa (tabela de endpoints com status, MER, diagrama de classes e
procedimentos) está em [`documentacao/DOCUMENTACAO.md`](documentacao/DOCUMENTACAO.md).

## Deploy

Projeto preparado para o **Render**: o Maven Wrapper é auto-detectado e a porta é
lida da variável `PORT`. Passo a passo completo em [`documentacao/DEPLOY.md`](documentacao/DEPLOY.md).
