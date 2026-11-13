# Chemical Batch Quality & Compliance Management System

A console-based **Core Java** project for modelling chemical
manufacturing batches, quality checks, inventory movements, customers,
orders and reservation records.

> **Current status:** Domain, quality, inventory, customer/order,
> reservation and in-memory repository components have been implemented.
> Automated testing is in progress. `InventoryService`, FEFO allocation
> and cancellation orchestration have **not yet been implemented**.

## Project scope

The application models this workflow:

1.  Define a chemical and its shelf life.
2.  Create production batches for that chemical.
3.  Define required quality specifications.
4.  Record batch-specific quality measurements.
5.  Evaluate batches as `PASSED`, `FAILED` or `PENDING`.
6.  Track approved batch stock through inventory quantity buckets.
7.  Create customers and customer orders.
8.  Validate orders.
9.  Store reservation records using in-memory repositories.

## Implemented components

### Domain model

-   `Chemical`
-   `ProductionBatch`
-   `QualityTest`
-   `QualityTestResult`
-   `ChemicalQualityProfile`
-   `Inventory`
-   `Customer`
-   `CustomerOrder`
-   `Reservation`

### Services

-   `QualityControlService`
-   `OrderService`

### Repositories

-   `InventoryRepository`
-   `InMemoryInventoryRepository`
-   `ReservationRepository`
-   `InMemoryReservationRepository`

### Enums

-   `QualityStatus`
-   `BatchStatus`
-   `CustomerStatus`
-   `OrderStatus`
-   `ReservationStatus`

## Technology

-   Java (project environment: Java 21)
-   Maven
-   JUnit 5
-   `BigDecimal` for precise decimal quantities and quality measurements
-   `LocalDate` / `LocalDateTime` for date and timestamp handling
-   Java collections for current in-memory storage

## Quality evaluation rules

-   If any required test fails, the overall quality outcome is `FAILED`.
-   If no test fails but at least one required result is missing, the
    outcome is `PENDING`.
-   If every required test has a passing result, the outcome is
    `PASSED`.
-   A failed batch is quarantined; a pending batch remains in
    quality-pending status; a passed batch is approved.

## Inventory model

Inventory tracks total, available, reserved, dispatched and damaged
quantities.

The quantity invariant is:

`total = available + reserved + dispatched + damaged`

## Current work / not yet implemented

-   `InventoryService`
-   FEFO (First-Expired, First-Out) inventory allocation
-   Multi-batch reservation orchestration and rollback
-   Reservation cancellation and release orchestration
-   Database persistence, REST APIs, authentication and concurrency

These are planned features, not completed functionality.

## Build and test

Run the full automated test suite:

``` bash
mvn clean test
```

Compile without running tests:

``` bash
mvn clean compile
```

Package the project:

``` bash
mvn package
```

## Project documentation

See
[`Chemical_Batch_Quality_System_Documentation_v1.1.md`](docs/Chemical_Batch_Quality_System_Documentation_v1.1.md)
for the detailed class reference, workflows, business rules, testing
progress and implementation tracker.

The matching Word document is maintained as a companion project
document.

## Learning / engineering focus

-   Object-oriented design and encapsulation
-   Composition and object references
-   Enums and controlled state transitions
-   Exception-based validation
-   `BigDecimal` and date/time APIs
-   Maven and JUnit 5
-   Repository abstractions and in-memory implementations
-   Business-rule testing and service-layer orchestration

This is an educational Core Java application and is **not represented as
production-ready software**.
