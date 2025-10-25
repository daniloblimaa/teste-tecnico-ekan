# Teste Técnico Ekan - API de Atendimentos Médicos

Este projeto é uma API desenvolvida em **Java 17** e **Spring Boot 3** para gerenciar atendimentos médicos, pacientes e condições baseadas na classificação CID-10.

Abaixo estão as informações atualizadas do projeto, endpoints e exemplos de uso conforme o código presente no repositório.

Resumo rápido
- Linguagem: Java 17
- Framework: Spring Boot 3
- Banco (runtime): H2 (teste) / PostgreSQL (configurado por propriedades)
- Documentação OpenAPI: via springdoc (Swagger UI)
- Envio de e-mail: Spring JavaMail (serviço implementado para enviar atendimento em formato FHIR/R5)

Como executar
- Com Maven instalado:

```bash
mvn clean install
mvn spring-boot:run
```

- Ou usando o wrapper no Windows (se preferir):

```cmd
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

A aplicação por padrão roda em http://localhost:8080 (quando não sobrescrito por variáveis de ambiente).

Base URL
- http://localhost:8080

OpenAPI / Swagger
- A dependência springdoc está ativa. A UI normalmente fica disponível em:
  - /swagger-ui.html  (ou /swagger-ui/index.html dependendo da versão do client)
  - Os endpoints OpenAPI em /v3/api-docs

Endpoints disponíveis

1) Pacientes
- Prefixo do controller: `/api/pacientes`

- POST /api/pacientes/create
  - Descrição: Cria um novo paciente.
  - Corpo (JSON) — PacienteDTO:
    - nome: string (obrigatório)
    - email: string
    - dataNascimento: string (YYYY-MM-DD)
  - Resposta: 201 Created com o objeto criado (Location apontando para o recurso criado)

- GET /api/pacientes/findall
  - Descrição: Lista todos os pacientes.
  - Resposta: 200 OK — lista de PacienteDTO

- GET /api/pacientes/find/{id}
  - Descrição: Busca paciente por ID.
  - Resposta: 200 OK — PacienteDTO

- PUT /api/pacientes/update/{id}
  - Descrição: Atualiza um paciente (parcial/total conforme DTO enviado).
  - Corpo: PacienteDTO (mesmos campos do create)
  - Resposta: 200 OK — PacienteDTO atualizado

- DELETE /api/pacientes/delete/{id}
  - Descrição: Deleta paciente por ID.
  - Resposta: 204 No Content

2) Atendimentos (consultas)
- Prefixo do controller: `/api/atendimentos`

- POST /api/atendimentos/create
  - Descrição: Cria um novo atendimento vinculado a um paciente e, opcionalmente, a condições (diagnósticos).
  - Corpo (JSON) — AtendimentoDTO:
    - dataAtendimento: string (OffsetDateTime, opcional; se omitido usa OffsetDateTime.now())
    - pacienteId: long (ID do paciente — obrigatório)
    - condicoes: array de CondicaoDTO (opcional)
      - CondicaoDTO:
        - id: Long (apenas para respostas)
        - anotacao: string
        - cidId: string (ID do CID cadastrado — o serviço espera um valor que pode ser parseado para Long no repositório atual)
        - cidNome: string (apenas para respostas)
  - Resposta: 201 Created — AtendimentoDTO com as condições vinculadas

- GET /api/atendimentos/findall
  - Descrição: Lista todos os atendimentos.
  - Resposta: 200 OK — lista de AtendimentoDTO

- GET /api/atendimentos/find/{id}
  - Descrição: Busca um atendimento por ID.
  - Resposta: 200 OK — AtendimentoDTO

- PUT /api/atendimentos/update/{id}
  - Descrição: Atualiza dados do atendimento e suas condições. Quando `condicoes` é enviado, as condições anteriores são deletadas e substituídas pelas novas.
  - Corpo: AtendimentoDTO
  - Resposta: 200 OK — AtendimentoDTO atualizado

- GET /api/atendimentos/paciente/{pacienteId}/condicoes
  - Descrição: Lista todas as condições (condicao -> cid) associadas aos atendimentos de um paciente.
  - Resposta: 200 OK — lista de CondicaoDTO

Observações sobre paths e retornos
- Os controllers montam URIs de Location nos responses de criação; por exemplo o controller de pacientes faz ResponseEntity.created(URI.create("/pacientes/" + id)) — observe que o Location construído não contém o prefixo `/api` (isso reflete o código atual).

Banco de dados e dados iniciais
- O projeto inclui `src/main/resources/data.sql` com inserções dos códigos CID do capítulo X (J09-J18). Exemplo dos códigos inseridos:
  - J09, J10, J11, J12, J13, J14, J15, J16, J17, J18

Configurações importantes (`src/main/resources/application.properties`)
- Propriedades de e-mail:
  - spring.mail.host (ex.: smtp.gmail.com)
  - spring.mail.port (ex.: 587)
  - spring.mail.username
  - spring.mail.password
  - spring.mail.properties.mail.smtp.auth=true
  - spring.mail.properties.mail.smtp.starttls.enable=true
- E-mail destinatário padrão utilizado pelo serviço de notificação:
  - app.notify.to (valor padrão: rdoni.ekan@iamspe.sp.gov.br)

Envio de e-mail / FHIR
- Implementado em `MailService`:
  - Converte um `Atendimento` para JSON no formato FHIR Encounter (via `FhirConverter`) e envia para o endereço definido em `app.notify.to`.
  - A chamada ao envio é feita pelo serviço, mas não há endpoint público específico apenas para disparar o e-mail automaticamente no controller — o envio pode ser acionado a partir de integração de serviço no código.

Exemplos rápidos (curl)
- Criar paciente:

```bash
curl -X POST http://localhost:8080/api/pacientes/create \
 -H "Content-Type: application/json" \
 -d '{"nome":"João Silva","email":"joao@example.com","dataNascimento":"1990-01-01"}'
```

- Criar atendimento (exemplo com condição):

```bash
curl -X POST http://localhost:8080/api/atendimentos/create \
 -H "Content-Type: application/json" \
 -d '{"pacienteId":1, "condicoes":[{"anotacao":"Tosse e febre","cidId":"J10"}] }'
```

Testes
- O projeto contém testes unitários de exemplo em `src/test/java` cobrindo repositório, serviço e controller (utilize `mvn test` para executá-los).

