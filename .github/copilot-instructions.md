# Copilot instructions for keycloak-sandbox

## Build, test, and validation

The main application code lives in `ecommerce-bff/`, which is a Spring Boot project built with Gradle.

- Build the app:
  `cd ecommerce-bff && ./gradlew build`
- Run the test suite:
  `cd ecommerce-bff && ./gradlew test`
- Run a single test class:
  `cd ecommerce-bff && ./gradlew test --tests "com.nps.ecommercebff.orders.controller.OrdersCrudControllerV1Test"`
- Run a single test method (Gradle filter syntax):
  `cd ecommerce-bff && ./gradlew test --tests "com.nps.ecommercebff.orders.controller.OrdersCrudControllerV1Test.getOrder_returnsNotFoundWhenOrderDoesNotExist"`
- There is no dedicated lint task configured in `ecommerce-bff/build.gradle.kts`; `./gradlew test` is the repository's standard verification step.

The repo also contains local Keycloak environments:

- Start the developer stack from the repo root:
  `docker compose -f dev-local/docker-compose.yml up -d`
- Stop it:
  `docker compose -f dev-local/docker-compose.yml down`
- Remove volumes when resetting the database:
  `docker compose -f dev-local/docker-compose.yml down -v`

The `prod-local/` setup exists as a production-like local environment, but the README explicitly marks it as "UNDER CONSTRUCTION".

## High-level architecture

This repository is a small integration sandbox rather than a single-service app:

- `dev-local/` and `prod-local/` define Docker Compose environments for Keycloak and Postgres. The dev-local stack is the working default and exposes the database on `5432` and Keycloak on `8180`.
- `ecommerce-bff/` is the actual Java/Spring application that acts as a backend-for-frontend ordering API and is the code most likely to need edits.

Within `ecommerce-bff`, the app follows a conventional Spring MVC layout:

- `src/main/java/com/nps/ecommercebff/EcommerceBffApplication.java` boots the application.
- `orders/` contains the order feature: `controller/`, `service/`, and `model/`.
- `common/` contains reusable API response and error-handling pieces.
- `src/main/resources/application.properties` contains the app configuration.

The API contract is intentionally consistent: controllers return `ResponseEntity<ApiResponseBody<T>>`, and the shared `GlobalExceptionHandler` converts `ResourceNotFoundException` into a JSON envelope shaped like `{ status, httpStatus, message, data }`.

## Key conventions

- The project uses Java records for request/response DTOs instead of verbose POJO classes (`OrderRequest`, `OrderItemRequest`, `ApiResponseBody`).
- Service boundaries are explicit: controllers depend on `OrderCrudService`, while `OrderService` implements that interface. This keeps controllers thin and makes tests easier to mock.
- Controllers are organized by versioned endpoints (`/api/v1/orders`) and use Spring MVC annotations plus OpenAPI annotations (`@Operation`, `@ApiResponse`, etc.).
- Tests are close to the controller logic and use `@WebMvcTest` with `MockMvc` and `@MockitoBean` to validate endpoint behavior and response payloads.
- This app targets Java 25 via the Gradle toolchain (`languageVersion = JavaLanguageVersion.of(25)`), and it uses Spring Boot 4.1.1 with Spring MVC.

## Local runtime notes

- The repo's default local auth infrastructure is the Keycloak dev environment in `dev-local/docker-compose.yaml`.
- The Postgres and Keycloak containers are configured to share the `keycloak-network` Docker network.
- The Spring app is expected to be developed independently of the Keycloak infrastructure and is not a monolith; changes are usually made in the BFF and the local Keycloak stack is used for integration testing without requiring a full production deployment.
