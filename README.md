# Chemical Batch Quality & Compliance Management System

A console-based **Core Java application** for managing chemical manufacturing batches, quality control, inventory movements, customer orders and reservation records.

**Current status:** The core business workflow has been implemented, including domain models, quality evaluation, inventory management, FEFO allocation, multi-batch reservations, order processing, cancellation, dispatch and in-memory repositories. The project includes automated unit tests using JUnit 5.

## Project Overview

The application models a chemical manufacturing and inventory management workflow:

1. Define chemicals and their shelf-life requirements.
2. Create production batches for chemicals.
3. Define quality specifications for each chemical.
4. Record batch-specific quality measurements.
5. Evaluate batch quality against required specifications.
6. Approve, quarantine or mark batches as quality pending.
7. Create inventory for approved production batches.
8. Track available, reserved, dispatched and damaged quantities.
9. Register customers and create customer orders.
10. Validate customer orders.
11. Allocate inventory using FEFO (First-Expired, First-Out).
12. Reserve inventory across multiple production batches.
13. Cancel orders and release reserved quantities.
14. Dispatch reserved orders.
15. Handle failures through inventory and reservation rollback mechanisms.

## Implemented Components

### 1. Domain Model

- `Chemical`
- `ProductionBatch`
- `QualityTest`
- `QualityTestResult`
- `ChemicalQualityProfile`
- `Inventory`
- `Customer`
- `CustomerOrder`
- `Reservation`

### 2. Services

- `QualityControlService` – Evaluates batch quality against required specifications.
- `InventoryService` – Manages inventory eligibility, FEFO allocation, reservations, releases, dispatch and rollback.
- `OrderService` – Coordinates order validation, reservation, cancellation and dispatch.

### 3. Repository Layer

Repository abstractions and in-memory implementations:

- `InventoryRepository`
- `InMemoryInventoryRepository`
- `ReservationRepository`
- `InMemoryReservationRepository`

### 4. Enums

- `QualityStatus`
- `BatchStatus`
- `CustomerStatus`
- `OrderStatus`
- `ReservationStatus`

## Key Business Rules

### Quality Control

- If any required quality test fails, the overall quality outcome is `FAILED`.
- If no test fails but one or more required results are missing, the outcome is `PENDING`.
- If every required test passes, the outcome is `PASSED`.
- Failed batches are moved to `QUARANTINED` status.
- Pending batches remain in `QUALITY_PENDING` status.
- Passed batches become `APPROVED`.

### Inventory Management

Inventory tracks five quantity values:

- Total quantity
- Available quantity
- Reserved quantity
- Dispatched quantity
- Damaged quantity

The inventory quantity invariant is:

`totalQuantity = availableQuantity + reservedQuantity + dispatchedQuantity + damagedQuantity`

Inventory can only be created for approved production batches.

### FEFO Allocation

The system uses **First-Expired, First-Out (FEFO)** to allocate eligible inventory.

- Inventory is filtered by chemical.
- Only approved batches are eligible.
- Expired batches are excluded.
- Batches with no available quantity are excluded.
- Eligible batches are sorted by earliest expiry date.
- Orders can reserve quantities across multiple batches.

### Order Management

The order lifecycle supports:

`CREATED → VALIDATED → RESERVED → DISPATCHED`

Additional transitions include:

- `CREATED → CANCELLED`
- `VALIDATED → CANCELLED`
- `RESERVED → CANCELLED`
- `CREATED / VALIDATED → REJECTED`

Business rules prevent inactive customers from placing or validating orders.

### Reservation Management

- Reservations are created against specific inventory records.
- Multiple reservations can be created for a single order.
- Cancelling a reserved order releases its reserved quantities back to available inventory.
- Dispatching an order moves reserved quantities into dispatched quantities.
- Rollback mechanisms restore inventory quantities and reservation states when operations fail.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core application development |
| Maven | Dependency and build management |
| JUnit 5 | Unit testing |
| BigDecimal | Precise quantity and measurement calculations |
| LocalDate / LocalDateTime | Date and time handling |
| Java Collections | In-memory data storage |
| Git and GitHub | Version control |

## Testing

The project uses JUnit 5 to test domain models, business rules, services and repository implementations.

The latest complete Maven test execution reported:

- **339 tests executed**
- **0 failures**
- **0 errors**
- **0 skipped**
- **BUILD SUCCESS**

Run the complete test suite:

```bash
mvn clean test
```

Compile the application:

```bash
mvn clean compile
```

Package the application:

```bash
mvn package
```

## Project Documentation

See [Technical Project Documentation v2.0](docs/Chemical_Batch_Quality_System_Documentation_v2.0.md) for the detailed architecture, domain model, business rules, workflows and testing information.

[Download the Word documentation](docs/Chemical_Batch_Quality_System_Documentation_v2.0.docx).
It contains the class reference, workflows, business rules, testing details and implementation information.

A companion Word document is also maintained for detailed project documentation.

## Learning and Engineering Focus

This project demonstrates practical implementation of:

- Object-oriented programming and encapsulation
- Composition and object references
- Enum-based state management
- Business validation and exception handling
- BigDecimal and Java date/time APIs
- Repository design and abstraction
- Service-layer orchestration
- FEFO inventory allocation
- Multi-batch inventory reservation
- Transaction-like rollback mechanisms
- Maven project management
- JUnit 5 unit testing

## Future Enhancements

The current application uses console-based interaction and in-memory repositories. Potential future enhancements include:

- MySQL database persistence
- Spring Boot REST APIs
- Authentication and role-based authorization
- Transaction management using database transactions
- Concurrent inventory reservation handling
- Web-based user interface

**Project scope:** This is an educational Core Java application demonstrating domain modelling, business logic, service orchestration and automated testing. It is not represented as production-ready enterprise software.
