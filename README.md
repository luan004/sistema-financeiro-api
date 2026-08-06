# Sistema Financeiro API

Base de uma API REST em Spring Boot organizada em três camadas DDD.

## Requisitos

- JDK 21 ou superior (o projeto é compilado com alvo Java 21)
- Maven 3.9+

## Executar

```powershell
mvn spring-boot:run
```

Com a aplicação ativa, verifique o endpoint de status. O campo `uptime` é expresso em segundos:

```powershell
Invoke-RestMethod http://localhost:8080/api/status
```

Envie `ping=true` para receber também `"ping": "pong!"`:

```powershell
Invoke-RestMethod 'http://localhost:8080/api/status?ping=true'
```

## Organização

```text
br.com.sistema.financeiro.api
├── application                    # casos de uso e orquestração
├── domain                         # regras, entidades e objetos de valor
└── infraestructure                # adaptadores HTTP e integrações técnicas
```

As responsabilidades são mantidas separadas, evitando que o domínio dependa de Spring, HTTP ou banco de dados:

- `domain`: entidades, objetos de valor, agregados e regras de negócio.
- `application`: casos de uso e portas de entrada/saída.
- `infraestructure`: adaptadores HTTP, persistência e clientes externos.

Cada endpoint possui uma action e um caso de uso próprios. Por exemplo, `GET /api/status` é atendido por `GetApiStatusAction`, que cria `GetApiStatusRequest` e delega para `GetApiStatusUseCase`, retornando `GetApiStatusResponse`.
- `interfaces`: controladores REST, DTOs e demais adaptadores de entrada.

## Próximas etapas planejadas

1. Adicionar Spring Data JPA, driver do banco e migrations (por exemplo, Flyway) em `infraestructure`.
2. Modelar o agregado de usuário e os casos de uso de autenticação.
3. Adicionar Spring Security OAuth2, mantendo o controlador e os DTOs na camada `interfaces`.
