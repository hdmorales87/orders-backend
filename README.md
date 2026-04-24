# Orders Backend

Java backend with ports and adapters architecture, organized as a Maven multi-module project.

## Modules

- `core`: shared domain
- `app-sync`: synchronous application layer, REST entry point and JPA persistence
- `app-reactive`: reactive application layer, WebFlux entry point and R2DBC persistence

## Structure

- `core`
  - `domain/model`
  - `domain/port/out`
- `app-sync`
  - `application`
  - `application/port/in`
  - `infrastructure/adapter/in/rest`
  - `infrastructure/adapter/out/persistence`
  - `infrastructure/config`
- `app-reactive`
  - `application/reactive`
  - `application/reactive/port/in`
  - `infrastructure/adapter/in/rest`
  - `infrastructure/adapter/out/persistence/reactive`
  - `infrastructure/adapter/out/messaging`
  - `infrastructure/adapter/out/email`
  - `infrastructure/config`

## Architectural Criteria

- Shared domain lives in `core`.
- Each application composes its own use cases.
- Persistence belongs to each app, not to a shared module.
- The reactive app does not depend on JPA.
- Reactor is not exposed from `core`.
- The SQL schema of the synchronous app is controlled with Flyway.
- `app-sync` is the current owner of shared schema migrations.
- `app-reactive` consumes the existing schema and does not run migrations.

## Message Queue & Notification System

The reactive application implements an asynchronous notification system using RabbitMQ:

### Features
- **Request-Reply Pattern**: Messages are sent to queue and processed asynchronously
- **Email Notifications**: Triggered only after successful queue processing
- **Correlation IDs**: Ensures message tracking and proper response routing
- **Timeout Handling**: 30-second timeout for queue processing confirmation
- **Error Resilience**: Graceful handling of failures without blocking main flow

### Flow
1. Order is saved to database
2. Message is sent to `order.notifications` queue with correlation ID
3. Consumer processes the message and sends confirmation to reply queue
4. Email notification is sent only after receiving confirmation
5. If timeout occurs, error is handled without affecting order creation

## Build

```bash
mvn clean compile
```

## Run Synchronous App

```bash
mvn -pl app-sync spring-boot:run
```

## Run Reactive App

```bash
mvn -pl app-reactive spring-boot:run
```

## Docker

### Docker Files
- `app-sync/Dockerfile`: synchronous app image
- `app-reactive/Dockerfile`: reactive app image
- `docker-compose.yml`: orchestrates MySQL, RabbitMQ, `app-sync` and `app-reactive`

### Services
- **MySQL**: Database server (port 3307)
- **RabbitMQ**: Message broker with management UI (ports 5672, 15672)
- **app-sync**: Synchronous API (port 8080)
- **app-reactive**: Reactive API (port 8081)

### Docker Commands

#### Complete Solution
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f

# Restart specific service
docker-compose restart app-reactive
```

#### Individual Services
```bash
# Start only infrastructure (MySQL + RabbitMQ)
docker-compose up -d mysql rabbitmq

# Start only database
docker-compose up -d mysql

# Start only message broker
docker-compose up -d rabbitmq

# Start specific app
docker-compose up -d app-reactive

# Build and start specific app
docker-compose up -d --build app-reactive
```

#### Development Workflow
```bash
# Start infrastructure first
docker-compose up -d mysql rabbitmq

# Wait for services to be healthy
docker-compose ps

# Run app locally (connected to Docker services)
mvn -pl app-reactive spring-boot:run

# Or run app in Docker
docker-compose up -d app-reactive
```

### Management UI
- **RabbitMQ Management**: http://localhost:15672
  - Username: `orders_user`
  - Password: `orders_pass`

### Environment Variables
Both applications use the following environment variables for RabbitMQ connection:
- `RABBITMQ_HOST`: rabbitmq
- `RABBITMQ_PORT`: 5672
- `RABBITMQ_USERNAME`: orders_user
- `RABBITMQ_PASSWORD`: orders_pass
- `RABBITMQ_VIRTUAL_HOST`: orders_vhost
