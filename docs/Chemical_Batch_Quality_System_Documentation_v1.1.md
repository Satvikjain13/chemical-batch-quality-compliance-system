# Chemical Batch Quality & Compliance Management System

**Project documentation --- Version 1.1**\
**Updated:** 13 November 2026\
**Implementation:** Core Java, Maven, JUnit 5\
**Current storage:** In-memory Java objects and collections

> **Documentation accuracy:** This document records the implementation
> status described by the project owner and the previously supplied
> Version 1.0 document. It is not a source-code audit of every class.
> Exact method signatures not supplied are deliberately not invented.

## 1. Project overview

The application models chemical manufacturing, batch quality evaluation,
inventory tracking, customer orders, and reservation records. It is
currently a console-based Core Java project. Maven manages the build and
JUnit 5 is used for automated tests.

  -----------------------------------------------------------------------
  Area                                Current status
  ----------------------------------- -----------------------------------
  Chemical and production batch       Implemented
  domain                              

  Quality test definitions and        Implemented
  results                             

  Chemical quality profiles           Implemented

  Quality control service             Implemented; automated tests are
                                      the current focus

  Inventory quantity tracking         Implemented

  Customer and customer orders        Implemented

  Order validation service            Implemented

  Reservation domain model and status Implemented

  Reservation repository and          Implemented; tests/review in
  in-memory implementation            progress

  Inventory repository and in-memory  Implemented; tests/review in
  implementation                      progress

  InventoryService / FEFO allocation  **Not started**
  / cancellation workflow             

  Database persistence, REST API,     Future work
  authentication, concurrency         
  -----------------------------------------------------------------------

## 2. Technology and approach

-   Java (project environment previously reported as Java 21)
-   Maven
-   JUnit 5
-   In-memory objects and collections; no database persistence yet
-   `BigDecimal` for decimal quantities and quality measurements
-   `LocalDate` for manufacturing/expiry dates; `LocalDateTime` for
    order timestamps where applicable
-   Domain objects protect local invariants; services coordinate
    workflows; repositories abstract storage

## 3. End-to-end workflows

### 3.1 Manufacturing → quality → inventory

1.  Create a `Chemical` master definition.
2.  Create a `ProductionBatch` referencing that chemical.
3.  Define applicable quality specifications using `QualityTest`
    definitions grouped in a `ChemicalQualityProfile`.
4.  Record actual measurements as `QualityTestResult` objects associated
    with a batch and test.
5.  `QualityControlService` compares required tests with recorded
    results.
6.  The service determines `PASSED`, `FAILED`, or `PENDING` and asks the
    batch to update its lifecycle state.
7.  `Inventory` represents stock for an approved batch.

### 3.2 Customer → order → reservation

1.  Create a `Customer`.
2.  Create a `CustomerOrder` for a chemical and requested quantity.
3.  `OrderService` validates whether the order can proceed under current
    customer/order rules.
4.  **Not yet implemented:** `InventoryService` will identify eligible
    inventory and plan FEFO allocations.
5.  Reservation records link an order to inventory and allocated
    quantity.
6.  **Not yet implemented:** cancellation coordination will release
    active reservations and update statuses.

Order validation is not the same as reserving stock. Stock availability
must be checked when reservation is attempted because inventory can
change after order validation.

## 4. Domain model reference

### 4.1 Chemical

Represents the definition/type of a chemical, not a manufactured batch.

-   Fields: chemical ID, name, unit, shelf-life days.
-   Rules: required text fields cannot be null/blank; shelf life must be
    positive.
-   Role: provides master data used by production batches.

### 4.2 ProductionBatch

Represents one manufactured batch of a chemical.

-   Fields: batch number, `Chemical` reference, manufacturing date,
    quantity, quality status, batch status.
-   Rules: batch number is required; chemical cannot be null;
    manufacturing date cannot be null or in the future; quantity must be
    positive.
-   Expiry is derived from manufacturing date plus the chemical's
    shelf-life days.

  Quality outcome   Batch status
  ----------------- -------------------
  `FAILED`          `QUARANTINED`
  `PENDING`         `QUALITY_PENDING`
  `PASSED`          `APPROVED`

The current implementation prevents reevaluation after a batch is
approved or quarantined. Retesting policy remains a future design
consideration.

### 4.3 Enums

  -----------------------------------------------------------------------
  Enum                                Values / purpose
  ----------------------------------- -----------------------------------
  `QualityStatus`                     `PENDING`, `PASSED`, `FAILED` ---
                                      quality evaluation outcome

  `BatchStatus`                       `IN_PRODUCTION`, `QUALITY_PENDING`,
                                      `APPROVED`, `QUARANTINED` --- batch
                                      lifecycle

  `CustomerStatus`                    `ACTIVE`, `INACTIVE` --- customer
                                      eligibility

  `OrderStatus`                       `CREATED`, `VALIDATED`, `RESERVED`,
                                      `DISPATCHED`, `CANCELLED`,
                                      `REJECTED` --- order lifecycle;
                                      some later transitions are planned

  `ReservationStatus`                 `ACTIVE`, `RELEASED` ---
                                      reservation lifecycle
  -----------------------------------------------------------------------

Quality status and batch status represent different concepts and should
not be conflated.

### 4.4 QualityTest

Defines a reusable quality specification; it does not store a batch's
actual measurement.

-   Fields: test ID, test name, minimum acceptable value, maximum
    acceptable value, unit.
-   Numeric representation: acceptable limits use `BigDecimal`.
-   Rule: minimum acceptable value must not exceed maximum acceptable
    value.
-   Range: inclusive.
-   Responsibility: determines whether a measured value is within the
    defined limits.

### 4.5 ChemicalQualityProfile

Groups the required `QualityTest` definitions applicable to a particular
`Chemical`.

-   A profile associates a chemical with its required quality
    specifications.
-   The application uses a registry keyed by chemical ID to retrieve the
    corresponding profile.
-   Different chemicals may have tests with the same display name but
    different test IDs and acceptable ranges.
-   The profile determines which tests a batch must complete.

### 4.6 QualityTestResult

Records one measurement for one production batch against one
quality-test definition.

-   Fields: result ID, batch reference, quality-test reference, measured
    value, calculated pass/fail flag.
-   Measured value uses `BigDecimal`.
-   Rules: result ID cannot be null/blank; batch, quality test, and
    measured value cannot be null.
-   Pass/fail is calculated by asking the associated `QualityTest`
    whether the measurement is within its acceptable range.
-   A valid measurement outside the acceptable range is a failed result,
    not an invalid record.

### 4.7 QualityControlService

Coordinates evaluation of a batch using required test definitions and
recorded results.

  -----------------------------------------------------------------------
  Condition                           Overall quality outcome
  ----------------------------------- -----------------------------------
  At least one matched result failed  `FAILED`

  No failures, but one or more        `PENDING`
  required results are missing        

  Every required test has a matching  `PASSED`
  passing result                      
  -----------------------------------------------------------------------

A failure takes precedence over missing results. The service matches
results using batch identity and test identity, then calls the batch's
quality-evaluation transition.

**Hardening items:** null elements inside lists, duplicate results for
the same batch/test, unexpected extra results, and chemical/test
compatibility.

### 4.8 Inventory

Tracks quantity buckets for one approved production batch.

-   `totalQuantity`
-   `availableQuantity`
-   `reservedQuantity`
-   `dispatchedQuantity`
-   `damagedQuantity`

Core invariant:

`totalQuantity = availableQuantity + reservedQuantity + dispatchedQuantity + damagedQuantity`

  Operation             Movement
  --------------------- -----------------------
  Reserve               Available → Reserved
  Release reservation   Reserved → Available
  Dispatch              Reserved → Dispatched
  Record damage         Available → Damaged

Operations must validate positive quantities and sufficient
source-bucket quantity. The initial policy is full-quantity reservation
or no reservation.

### 4.9 Customer

Represents a customer/business identity and current contact information.

-   Fields: customer ID, name, contact number, email, address, customer
    status.
-   Version 1.0 records: required fields validated; contact number
    expects exactly 10 digits; email is optional with basic format
    validation when supplied; status cannot be null.
-   New customers default to `ACTIVE`.
-   An inactive customer cannot pass the current order-validation rule.

### 4.10 CustomerOrder

Represents a customer's request for a chemical quantity.

-   Fields: order ID, customer reference, chemical reference, requested
    quantity, order date/time, order status, customer snapshot fields.
-   Requested quantity is a positive `BigDecimal`.
-   Customer details are snapshotted at order creation so later customer
    edits do not rewrite historical order details.
-   The currently documented controlled transition is `CREATED` →
    `VALIDATED`.
-   Later lifecycle transitions are not assumed to be implemented merely
    because enum values exist.

### 4.11 OrderService

Coordinates order validation; it does not directly mutate inventory
quantities.

-   Null order is rejected with `IllegalArgumentException`.
-   An inactive customer is rejected with `IllegalStateException`.
-   Otherwise, the service requests the order's `CREATED` → `VALIDATED`
    transition.

Order creation management, stock checks, reservations, cancellation and
dispatch are separate workflows.

### 4.12 Reservation and ReservationStatus

The reservation model and status enum have been implemented.

-   A reservation represents an allocation linking a customer order to
    an inventory record and a quantity.
-   `ReservationStatus` currently uses `ACTIVE` and `RELEASED`.
-   The reservation repository interface and in-memory implementation
    have also been created.
-   Exact method signatures and detailed transition rules should be
    confirmed against source files before being added here.

### 4.13 Repository interfaces and in-memory implementations

The project includes repository abstractions and in-memory
implementations for inventory and reservations:

-   `InventoryRepository`
-   `InMemoryInventoryRepository`
-   `ReservationRepository`
-   `InMemoryReservationRepository`

The repository layer separates storage operations from domain and
service logic. Current implementations use collections rather than a
database. Repository tests and edge cases are being worked through;
exact method contracts should be checked against source code.

## 5. Responsibility boundaries

  -----------------------------------------------------------------------
  Business question                   Responsible class/layer
  ----------------------------------- -----------------------------------
  What chemical is this?              `Chemical`

  What was manufactured, when and how `ProductionBatch`
  much?                               

  Which quality tests apply to this   `ChemicalQualityProfile`
  chemical?                           

  What quality limits apply?          `QualityTest`

  What measurement was recorded?      `QualityTestResult`

  Did the batch satisfy its required  `QualityControlService`
  tests?                              

  What stock quantities exist?        `Inventory`

  Who is the customer?                `Customer`

  What quantity did the customer      `CustomerOrder`
  request?                            

  Can the order pass current          `OrderService`
  validation?                         

  Where are inventory/reservation     Repository interfaces and in-memory
  records stored?                     implementations

  How are eligible batches selected   `InventoryService` --- **not
  and reservations coordinated?       started**
  -----------------------------------------------------------------------

A domain object protects its own state and local invariants. A service
coordinates a workflow involving multiple objects. A repository
abstracts storage.

## 6. Maven and JUnit testing

Useful commands:

``` bash
mvn clean test
mvn clean compile
mvn package
```

-   `mvn clean test` cleans previous build output, compiles
    production/test sources and runs tests.
-   Tests are kept under `src/test/java`.
-   Tests should cover meaningful validation, boundaries, state
    transitions, business rules and failure scenarios.
-   Avoid adding tests solely to inflate the test count.

### Test progress recorded from project sessions

  -----------------------------------------------------------------------
  Test class                          Reported test count/status
  ----------------------------------- -----------------------------------
  `ChemicalTest`                      12 tests passed

  `ProductionBatchTest`               18 tests passed

  `QualityTestTest`                   20 tests passed after BigDecimal
                                      migration

  `QualityTestResultTest`             12 scenarios authored; confirm
                                      final Maven run before recording as
                                      verified

  `QualityControlServiceTest`         Next testing task

  Inventory, customer/order,          Continue according to testing
  reservation and repository tests    roadmap
  -----------------------------------------------------------------------

The earlier combined Maven run reported 50 passing tests across
Chemical, ProductionBatch and QualityTest. That older total does not
include later test classes.

## 7. InventoryService and FEFO --- not started

`InventoryService` has not been implemented. No FEFO allocation or
cancellation orchestration should be described as completed.

Planned responsibilities:

1.  Identify eligible inventory for the order's chemical.
2.  Exclude batches that are not eligible for allocation.
3.  Sort eligible inventory by earliest expiry date (FEFO).
4.  Plan allocations across multiple batches if needed.
5.  Apply reservations through `Inventory` and `Reservation` objects.
6.  Enforce all-or-nothing policy: if the full order cannot be reserved,
    undo earlier successful reservation movements.
7.  On cancellation, release active reservations to their source
    inventory and mark reservations as released.

These are planned behaviours, not implemented features.

## 8. Known design and hardening items

-   Validate null elements in required-test and result lists.
-   Define policy for duplicate quality results for the same batch/test.
-   Validate that a test belongs to the chemical profile for the batch's
    chemical.
-   Enforce unique IDs at the appropriate application/service boundary.
-   Define an explicit retesting policy.
-   Ensure at most one inventory record exists per production batch.
-   Preserve inventory quantity-bucket invariants through successful and
    failed operations.
-   Implement all-or-nothing multi-batch reservations and rollback.
-   Retain reservation history after release.
-   Add persistence and transaction management only after in-memory
    workflows are correct.

## 9. Current implementation tracker

  -----------------------------------------------------------------------
  Component                           Status
  ----------------------------------- -----------------------------------
  Chemical                            Implemented; unit tests reported
                                      passing

  ProductionBatch                     Implemented; unit tests reported
                                      passing

  QualityTest                         Implemented with BigDecimal; unit
                                      tests reported passing

  QualityTestResult                   Implemented with BigDecimal; test
                                      class authored

  ChemicalQualityProfile              Implemented

  QualityControlService               Implemented; unit tests next

  Inventory                           Implemented

  Customer                            Implemented

  CustomerOrder                       Implemented

  Reservation                         Implemented

  ReservationStatus                   Implemented

  InventoryRepository                 Implemented

  InMemoryInventoryRepository         Implemented

  ReservationRepository               Implemented

  InMemoryReservationRepository       Implemented

  OrderService                        Implemented

  InventoryService                    **Not started**

  FEFO allocation                     **Not started**

  Cancellation orchestration          **Not started**

  Database persistence                Future work
  -----------------------------------------------------------------------

## 10. Class documentation template

For each new or materially changed class, record:

1.  Exact class name and package.
2.  Business purpose.
3.  Fields, types, mutability and why each field exists.
4.  Methods, responsibilities, validations and state changes.
5.  Dependencies on other domain objects/services/repositories.
6.  Business rules enforced.
7.  Unit tests: positive, negative, boundary, state-transition and
    rollback scenarios as relevant.
8.  New Java concepts used and why they were appropriate.
9.  Known limitations and deferred work.

## 11. Revision and learning routine

Use active recall rather than rereading every class every day:

-   **Daily (15 minutes):** explain one recently completed class, its
    collaborators and one important business rule without opening the
    code.
-   **Weekly (30 minutes):** explain the end-to-end manufacturing →
    quality → inventory → order workflow.
-   Update this document when a class, method, business rule, test
    result or implementation status changes.

## 12. Progress log

  ------------------------------------------------------------------------------------
  Version                 Date                    Changes
  ----------------------- ----------------------- ------------------------------------
  1.0                     2 November 2026         Initial domain, quality, inventory
                                                  and customer/order foundation
                                                  documentation

  1.1                     13 November 2026        Added BigDecimal migration,
                                                  ChemicalQualityProfile, reservation
                                                  and repository implementation
                                                  status, testing progress, and
                                                  clarified that
                                                  InventoryService/FEFO/cancellation
                                                  remain unimplemented
  ------------------------------------------------------------------------------------
