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

## Architecture Analysis

### Hexagonal Architecture (Ports & Adapters)

The project implements **Hexagonal Architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────────┐
│                    Infrastructure Layer                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │   REST API  │  │   Messaging  │  │   Email     │  │
│  │  Controllers │  │  Adapters   │  │  Adapters   │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  │
└─────────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────────┐
│                   Application Layer                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │ Use Cases   │  │   Services   │  │   DTOs      │  │
│  │ (Business    │  │ (Orchestrate │  │ (Request/   │  │
│  │  Logic)     │  │  Use Cases)  │  │ Response)   │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  │
└─────────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────────┐
│                     Domain Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │   Models    │  │    Ports    │  │  Constants  │  │
│  │ (Entities)  │  │ (Interfaces) │  │ (Config)    │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### SOLID Principles Applied

#### **S - Single Responsibility Principle (SRP)**
- **Order Model**: Only handles order data and validation
- **SMTPEmailAdapter**: Solely responsible for email sending
- **RabbitMQAdapter**: Exclusively handles message queue operations
- **OrderConsumer**: Only processes incoming messages and sends confirmations
- **Controllers**: Only handle HTTP request/response mapping

#### **O - Open/Closed Principle (OCP)**
- **Port Interfaces**: Open for extension, closed for modification
- **Adapter Pattern**: New implementations can be added without changing existing code
- **Message Queue Ports**: Can add new messaging systems without modifying business logic

#### **L - Liskov Substitution Principle (LSP)**
- **AsyncMessageQueuePort**: Both reactive and sync implementations are interchangeable
- **OrderRepository**: Reactive and sync implementations can be substituted
- **EmailNotificationPort**: Different email providers can be swapped seamlessly

#### **I - Interface Segregation Principle (ISP)**
- **Separate Ports**: `EmailNotificationPort`, `MessageQueuePort`, `OrderRepository`
- **Specific Methods**: Each interface contains only related methods
- **Client-Specific**: No client is forced to implement unused methods

#### **D - Dependency Inversion Principle (DIP)**
- **Dependencies Inverted**: High-level modules depend on abstractions (ports)
- **Constructor Injection**: Dependencies injected through constructors
- **Configuration Classes**: Bean definitions manage dependency creation

### Design Patterns Implemented

#### **Adapter Pattern**
```java
// Infrastructure adapts to domain ports
public class SMTPEmailAdapter implements EmailNotificationPort
public class AsyncRabbitMQAdapter implements AsyncMessageQueuePort
public class ReactiveOrderRepositoryAdapter implements OrderRepository
```

#### **Strategy Pattern**
- **Messaging Strategies**: Different implementations for reactive vs sync
- **Persistence Strategies**: R2DBC vs JPA approaches
- **Notification Strategies**: Email vs other notification channels

#### **Factory Pattern**
- **BeanConfig**: Creates and configures use case implementations
- **MessageConverter**: Factory for JSON message conversion

#### **Observer Pattern**
- **Reactive Streams**: Flux/Mono as observable sequences
- **Message Queue**: Consumers observe queue for messages

#### **Command Pattern**
- **Use Cases**: Each use case encapsulates a business command
- **REST Controllers**: HTTP requests mapped to command objects

#### **Request-Reply Pattern**
- **Correlation IDs**: Track request-response pairs
- **Reply Queues**: Separate queues for responses
- **Timeout Handling**: Prevents indefinite waiting

### Clean Code Principles

#### **Meaningful Names**
```java
// Clear, descriptive names
CreateOrderReactiveUseCase
AsyncMessageQueuePort
SMTPEmailAdapter
OrderConsumer
```

#### **Small, Focused Functions**
```java
// Single responsibility per method
public Mono<Void> sendMessageAndWaitConfirmation(String queueName, Object message)
public Mono<Void> sendConfirmation(String replyQueueName, String correlationId)
```

#### **No Duplication (DRY)**
- **Constants**: `RabbitMQConfig.ORDER_NOTIFICATIONS_QUEUE`
- **Shared Domain**: Core module contains common models and ports
- **Configuration**: Centralized in config classes

#### **Comments and Documentation**
- **Purpose Documentation**: Clear comments explaining business logic
- **Configuration Comments**: Docker and environment variable documentation
- **API Documentation**: REST endpoints clearly documented

#### **Error Handling**
```java
// Consistent error handling patterns
.onErrorContinue((error, throwable) -> {
    System.err.println("Error in order processing: " + error.getMessage());
});
```

#### **Configuration Management**
- **Environment Variables**: Externalized configuration
- **Bean Configuration**: Dependency injection setup
- **Modular Configuration**: Separate config classes per concern

### Architectural Benefits

#### **Maintainability**
- **Clear Boundaries**: Each layer has distinct responsibilities
- **Loose Coupling**: Dependencies are interfaces, not implementations
- **High Cohesion**: Related functionality grouped together

#### **Testability**
- **Interface Mocking**: Easy to mock dependencies
- **Unit Testing**: Each component can be tested in isolation
- **Integration Testing**: Clear test boundaries at adapter level

#### **Scalability**
- **Horizontal Scaling**: Each app can be scaled independently
- **Message Queue**: Asynchronous processing enables load distribution
- **Reactive Programming**: Non-blocking I/O for better throughput

#### **Flexibility**
- **Technology Agnostic**: Easy to swap implementations
- **Multi-Environment**: Configuration-driven deployment
- **Hybrid Approach**: Both sync and reactive patterns supported

### Technology Stack Alignment

#### **Domain-Driven Design (DDD)**
- **Ubiquitous Language**: Order, Repository, Use Case
- **Bounded Contexts**: Core domain shared across applications
- **Domain Events**: Message queue events represent domain events

#### **Reactive Programming**
- **Backpressure Handling**: Reactive streams manage flow control
- **Non-blocking**: Better resource utilization
- **Event-Driven**: Message queue enables event-driven architecture

#### **Microservices Ready**
- **Service Boundaries**: Clear separation between sync and reactive apps
- **Independent Deployment**: Each app can be deployed separately
- **Inter-service Communication**: Message queue for async communication
