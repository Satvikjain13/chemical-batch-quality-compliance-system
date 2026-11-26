# Chemical Batch Quality & Compliance Management System

**Technical Project Documentation — Version 2.0**  
**Updated:** 27 November 2026  
**Application type:** Console-based Core Java application  
**Runtime:** Java 21  
**Build:** Maven  
**Testing:** JUnit 5  
**Persistence:** In-memory repositories (no database integration)

---

## 1. Executive summary

The Chemical Batch Quality & Compliance Management System is a Core Java application that models key workflows in a chemical manufacturing and distribution environment. It connects chemical master data, production batches, quality specifications, batch-level quality results, inventory accounting, customer orders and reservation records.

The application is structured around domain objects, service-layer orchestration and repository abstractions. Its primary objective is to demonstrate sound object-oriented modelling and enforce business rules consistently across manufacturing, quality control and order fulfilment workflows.

The implemented console demonstration covers the principal lifecycle:

**Chemical definition → production batch → quality evaluation → approved inventory → customer order → FEFO reservation → dispatch or cancellation.**

The project currently uses in-memory Java collections. It is an educational Core Java application and is not represented as production-ready enterprise software.

### 1.1 Implementation status

| Capability | Current status |
|---|---|
| Chemical and production-batch domain | Implemented |
| Quality-test definitions and batch-specific results | Implemented |
| Chemical quality profiles | Implemented |
| Quality evaluation and batch status transitions | Implemented |
| Inventory quantity buckets and invariants | Implemented |
| In-memory inventory repository | Implemented |
| Customer and customer-order domain | Implemented |
| Order validation and lifecycle transitions | Implemented |
| Reservation domain and in-memory repository | Implemented |
| FEFO inventory selection | Implemented |
| Multi-batch inventory reservation | Implemented |
| Reservation release on cancellation | Implemented |
| Dispatch of active reservations | Implemented |
| Snapshot-based rollback for service operations | Implemented |
| Console workflow demonstration | Implemented |
| Automated JUnit 5 test suite | 339 tests passed in latest reported run |
| MySQL/database persistence | Future enhancement |
| REST API, authentication and web UI | Future enhancements |
| Database transaction and concurrent reservation handling | Future enhancements |

The test result above reflects the Maven run reported on 27 November 2026: **339 tests, 0 failures, 0 errors and 0 skipped**. It is a test-execution result, not a claim of exhaustive branch or mutation coverage.

---

## 2. Business problem and project objectives

Chemical manufacturing operations need traceability between a chemical definition, individual production batches, quality outcomes and stock available for customer fulfilment. A batch that fails quality checks must not become saleable inventory. Similarly, inventory reserved for an order must not remain available for another order, and dispatched quantities must be distinguishable from available or damaged quantities.

This project models those controls in a compact Java application.

### 2.1 Objectives

- Separate chemical master data from individual production batches.
- Define reusable quality specifications and record batch-specific measurements.
- Derive an overall quality outcome from all required tests.
- Prevent non-approved batches from entering inventory.
- Preserve inventory quantity accounting through explicit quantity buckets.
- Select eligible inventory using First-Expired, First-Out (FEFO).
- Support an order drawing stock from more than one production batch.
- Preserve reservation records and represent their lifecycle.
- Release reserved stock when an order is cancelled.
- Move reserved stock into dispatched quantities when an order is dispatched.
- Validate important business transitions and recover in-memory state when a multi-step operation fails.
- Verify behaviour with automated unit tests.

---

## 3. Scope and architecture

### 3.1 Application scope

The current application is a console-based Core Java system. It demonstrates domain and service behaviour without a database, HTTP layer, authentication system or graphical user interface.

### 3.2 Layered design

The codebase follows a lightweight layered structure:

| Layer | Responsibility | Representative classes |
|---|---|---|
| Application | Builds sample data and demonstrates workflows | `Main` |
| Domain | Holds business state, rules and valid transitions | `Chemical`, `ProductionBatch`, `Inventory`, `CustomerOrder`, `Reservation` |
| Service | Coordinates workflows across domain objects and repositories | `QualityControlService`, `InventoryService`, `OrderService` |
| Repository | Abstracts storage operations | `InventoryRepository`, `ReservationRepository` |
| In-memory infrastructure | Stores objects using Java collections | `InMemoryInventoryRepository`, `InMemoryReservationRepository` |
| Tests | Verifies domain, repository and service behaviour | JUnit 5 test classes |

### 3.3 Dependency direction

Services depend on repository abstractions rather than directly managing collection storage. Domain objects enforce their own local invariants. Services coordinate operations that span multiple domain objects. In-memory repository implementations provide the current storage mechanism.

This separation makes the business workflow easier to test and provides a path to alternative repository implementations later. Replacing in-memory storage with a database would still require explicit persistence design, mapping, transaction handling and integration tests.

---

## 4. Technology stack

| Technology | Role |
|---|---|
| Java 21 | Language and runtime |
| Maven | Build and dependency management |
| JUnit 5 | Automated unit testing |
| `BigDecimal` | Decimal quantities and quality measurements |
| `LocalDate` | Manufacturing and expiry dates |
| `LocalDateTime` | Date-time values where required by the order model |
| Java Collections | In-memory storage and workflow collections |
| Git / GitHub | Version control and branch-based collaboration |

`BigDecimal` is used for quantities and measurements where binary floating-point rounding would be undesirable. Date-only manufacturing and expiry values use `LocalDate`.

---

## 5. Domain model

### 5.1 Chemical

`Chemical` represents a chemical master definition rather than a manufactured quantity.

**Core attributes**
- Chemical identifier
- Chemical name
- Unit of measurement
- Shelf-life duration in days

**Business role**
- Provides the master data referenced by production batches.
- Supplies the shelf-life value used when calculating a batch's expiry date.

### 5.2 ProductionBatch

`ProductionBatch` represents a specific manufacturing run of a chemical.

**Core attributes**
- Batch number
- Reference to a `Chemical`
- Manufacturing date
- Expiry date
- Produced quantity
- Batch status
- Quality evaluation state

The expiry date is derived from the manufacturing date and the referenced chemical's shelf life. The chemical is modelled as an object reference rather than duplicating only its name, preserving the relationship to the chemical definition.

**Quality outcome to batch status mapping**

| Quality outcome | Batch status |
|---|---|
| `PASSED` | `APPROVED` |
| `FAILED` | `QUARANTINED` |
| `PENDING` | `QUALITY_PENDING` |

Only approved batches are eligible for inventory creation under the current domain rule.

### 5.3 QualityTest

`QualityTest` defines a reusable quality specification.

**Core attributes**
- Test identifier
- Test name
- Minimum acceptable value
- Maximum acceptable value
- Measurement unit

The acceptable range is inclusive. The minimum must not exceed the maximum. Decimal limits use `BigDecimal`.

### 5.4 QualityTestResult

`QualityTestResult` records the actual measurement for one batch against one test definition.

A result is batch-specific. The same `QualityTest` definition can be reused across multiple batches, while each batch has its own measured value and pass/fail outcome.

A measured value outside the accepted range is a valid recorded result with a failed outcome; it is not inherently an invalid record.

### 5.5 ChemicalQualityProfile

`ChemicalQualityProfile` groups the quality tests required for a particular chemical.

The current application uses a profile registry keyed by chemical ID to retrieve the required test definitions during batch evaluation. The profile validates its chemical reference and required test collection, rejects invalid or duplicate test definitions, and stores an immutable copy of the supplied list.

### 5.6 QualityControlService

`QualityControlService` evaluates a batch against its required tests and recorded results.

| Evaluation condition | Overall quality status |
|---|---|
| At least one required, matched result fails | `FAILED` |
| No required result fails, but one or more are missing | `PENDING` |
| Every required test has a passing result | `PASSED` |

A failure takes precedence over missing results. Result matching uses batch identity and test identity. The resulting quality outcome is applied to the batch's lifecycle state.

### 5.7 Inventory

`Inventory` represents the stock quantity associated with one approved production batch.

**Quantity buckets**
- `totalQuantity`
- `availableQuantity`
- `reservedQuantity`
- `dispatchedQuantity`
- `damagedQuantity`

**Invariant**

```text
totalQuantity =
    availableQuantity
  + reservedQuantity
  + dispatchedQuantity
  + damagedQuantity
```

The inventory constructor initializes the full batch quantity as available and the other buckets to zero. It rejects a null batch and a batch that is not approved.

**Supported quantity movements**

| Operation | Quantity movement |
|---|---|
| Reserve | Available → Reserved |
| Release reservation | Reserved → Available |
| Dispatch | Reserved → Dispatched |
| Record damage | Available → Damaged |

Operations validate positive quantities and sufficient source-bucket quantity. State restoration validates non-negative values and preserves the total-quantity invariant.

### 5.8 Customer

`Customer` represents a customer identity and contact information.

The model includes a customer identifier, name, contact number, optional email and address, and customer status. New customers are initialized as `ACTIVE`. The current validation rules require the key identity/contact fields, enforce a ten-digit contact number, and apply basic format validation when an email is supplied.

An inactive customer cannot pass order validation and cannot create a new order under the current constructor rule.

### 5.9 CustomerOrder

`CustomerOrder` represents a customer's request for a quantity of a chemical.

**Core attributes**
- Order identifier
- Customer reference
- Chemical reference
- Requested quantity
- Order date/time
- Order status
- Customer detail snapshots captured at order creation

The requested quantity must be positive. Customer details are snapshotted so later changes to the customer's contact information do not rewrite the historical details stored with the order.

**Order statuses**

`CREATED`, `VALIDATED`, `RESERVED`, `REJECTED`, `DISPATCHED`, `CANCELLED`.

The domain model controls valid status transitions and rejects operations that are not valid for the current state.

### 5.10 Reservation

`Reservation` links a customer order to a specific inventory record and the quantity allocated from that inventory.

A single order may have multiple reservation records when its requested quantity is allocated across multiple production batches.

**Reservation statuses**
- `ACTIVE`
- `RELEASED`
- `DISPATCHED`

Reservation status changes are coordinated with inventory quantity movements by `InventoryService`.

---

## 6. Service-layer workflows

### 6.1 Quality evaluation workflow

1. Create the chemical definition.
2. Create a production batch referencing the chemical.
3. Define required quality tests.
4. Group the tests in a `ChemicalQualityProfile`.
5. Record `QualityTestResult` measurements for the batch.
6. Retrieve the profile using the chemical identifier.
7. Evaluate the batch through `QualityControlService`.
8. Apply the resulting batch status.
9. Create inventory only when the batch is approved.

### 6.2 Inventory eligibility and FEFO selection

`InventoryService.findInventoriesByChemical()` identifies inventory eligible for allocation.

The current selection rules are:

- The inventory must belong to the requested chemical.
- Its production batch must be `APPROVED`.
- It must have a positive available quantity.
- Its expiry date must be after the current date.
- Eligible inventory is sorted by ascending expiry date.

FEFO means **First-Expired, First-Out**. The earliest-expiring eligible stock is selected first. This is distinct from FIFO, which is based on arrival or creation order.

### 6.3 Inventory sufficiency check

`hasSufficientInventory()` sums the available quantities in the eligible inventory list and compares the total with the requested quantity. The request must be non-null and greater than zero.

### 6.4 Multi-batch reservation

`reserveInventoryForOrder()` accepts only a validated order.

1. Find eligible inventory for the order's chemical.
2. Determine whether the combined available quantity is sufficient.
3. If insufficient, reject the order and return an empty reservation list.
4. If sufficient, iterate through eligible inventory in FEFO order.
5. Reserve the required quantity from each inventory record until the order is fully allocated.
6. Create a reservation record for each allocation.
7. Mark the order as reserved after the allocations complete.
8. If a runtime failure occurs during the operation, restore inventory snapshots and delete reservation records created by the failed attempt; rollback failures are attached as suppressed exceptions to the original exception.

The current implementation uses an all-or-nothing reservation policy: the order is either fully reserved or the attempted reservation is rolled back.

### 6.5 Order validation and orchestration

`OrderService` coordinates order lifecycle operations and delegates inventory operations to `InventoryService`.

| Operation | Behaviour |
|---|---|
| Validate | Rejects null orders and inactive customers; transitions an eligible order to `VALIDATED` |
| Reserve | Requires a validated order; delegates allocation to `InventoryService`; reports whether reservations were created |
| Cancel | Allows cancellation from `CREATED`, `VALIDATED` or `RESERVED`; releases active reservations when the order is reserved, then marks it cancelled |
| Dispatch | Requires a reserved order; delegates reservation dispatch, then marks the order dispatched |

The service layer keeps order lifecycle coordination separate from the inventory quantity calculations.

### 6.6 Reservation release on cancellation

For a reserved order, `releaseReservationForOrder()` retrieves its reservation records and selects active reservations. It snapshots inventory quantities and reservation statuses, returns each reserved quantity to available stock, and marks the corresponding reservations as released.

If a runtime failure occurs, the service attempts to restore inventory snapshots and reservation statuses before rethrowing the original exception.

### 6.7 Dispatch

`dispatchReservationForOrder()` requires an order in `RESERVED` status and at least one active reservation. It moves each active reservation's quantity from reserved to dispatched and marks the reservation as dispatched.

The method snapshots the affected inventory and reservation states. If a runtime failure occurs, it attempts to restore the snapshots and rethrows the original exception.

---

## 7. Repository design

The repository layer separates storage operations from domain and service logic.

### 7.1 InventoryRepository

The inventory repository abstraction supports saving inventory records, finding an inventory by identifier, retrieving all inventory records and checking whether an identifier exists.

`InMemoryInventoryRepository` stores inventory objects in a Java map and provides the current in-memory implementation.

### 7.2 ReservationRepository

The reservation repository abstraction supports saving reservations, finding by reservation identifier, finding reservations for an order, deleting a reservation by identifier, retrieving all reservations and checking identifier existence.

`InMemoryReservationRepository` stores reservation objects in a Java map and supports order-based retrieval.

### 7.3 Persistence boundary

The current repositories are in-memory implementations. Data is not durable across application restarts. Database persistence, schema design, ORM/JDBC mapping and transaction management have not been included in the current implementation.

---

## 8. Console demonstration

`Main` assembles sample chemicals, production batches, quality-test definitions, profiles and quality results. It evaluates the batches and creates inventory only for approved batches.

The demonstrated order scenarios include:

| Scenario | Expected business outcome |
|---|---|
| Valid order with sufficient stock | Reserve inventory using FEFO and dispatch |
| Reserved order cancelled | Release reserved quantities back to available |
| Requested quantity exceeds available stock | Reject the order without partially reserving stock |
| Customer becomes inactive before validation | Reject validation and leave order unvalidated |
| Already inactive customer attempts to create an order | Reject order creation |

The sample data is illustrative and is not intended to represent certified industrial chemical specifications.

---

## 9. Automated testing

The project uses JUnit 5 for automated tests across domain models, repositories and services.

### 9.1 Latest reported test execution

**Command**

```bash
mvn test
```

**Reported result — 27 November 2026**

| Metric | Result |
|---|---:|
| Tests executed | 339 |
| Failures | 0 |
| Errors | 0 |
| Skipped | 0 |
| Build result | `BUILD SUCCESS` |
| Reported execution time | 3.803 seconds |

The `OrderServiceTest` class reported 9 passing tests in that run. The `InventoryServiceTest` class reported 21 passing tests, and the `QualityControlServiceTest` class reported 21 passing tests. The full Maven summary is the authoritative total for that execution.

### 9.2 Testing principles

The test suite is intended to verify:
- Constructor and input validation.
- Domain invariants and quantity movements.
- Quality evaluation outcomes.
- Valid and invalid lifecycle transitions.
- Repository save, lookup, deletion and retrieval behaviour.
- FEFO selection and inventory sufficiency.
- Reservation, release and dispatch behaviour.
- Failure recovery and state restoration where covered by the tests.

A passing test suite confirms the assertions exercised by those tests. It does not, by itself, establish exhaustive coverage or production readiness.

### 9.3 Build commands

Run the complete test suite:

```bash
mvn clean test
```

Compile the project:

```bash
mvn clean compile
```

Package the project:

```bash
mvn package
```

---

## 10. Error handling and consistency approach

The domain layer uses argument validation and state-transition checks to prevent invalid local state. Illegal arguments are rejected with `IllegalArgumentException`; operations that are invalid for the object's current lifecycle state use `IllegalStateException`.

For multi-object inventory operations, `InventoryService` captures snapshots of the affected inventory quantities and reservation statuses. If a runtime exception interrupts the operation, it attempts to restore the previous in-memory state.

This is an application-level rollback strategy for the current in-memory implementation. It is **not equivalent to an ACID database transaction**. A future persistent implementation must use appropriate transaction boundaries and concurrency controls.

---

## 11. Current limitations and future enhancements

The following items are outside the current implementation scope:

| Area | Potential enhancement |
|---|---|
| Persistence | MySQL schema, JDBC or ORM-based repositories |
| Transactions | Database transaction boundaries for reservation and dispatch |
| Concurrency | Prevent competing orders from reserving the same available stock |
| API | Spring Boot REST endpoints |
| Security | Authentication, authorization and role-based access |
| User interface | Web-based dashboard for batches, quality and orders |
| Auditability | Durable audit events for quality decisions and inventory movements |
| Operations | Structured logging, configuration and deployment support |

Additional hardening opportunities include defining duplicate quality-result policy, retesting policy, repository uniqueness guarantees, and stronger integration tests when persistent storage is introduced.

---

## 12. Interview discussion guide

Be prepared to explain these design decisions:

1. **Why separate `Chemical` and `ProductionBatch`?**  
   A chemical is master data; a production batch is a specific manufactured instance with its own dates, quantity and quality outcome.

2. **Why separate `QualityTest` and `QualityTestResult`?**  
   A test defines acceptable limits; a result records a batch's actual measurement against those limits.

3. **Why use `BigDecimal`?**  
   It avoids binary floating-point representation issues for decimal quantities and measurements.

4. **Why use FEFO?**  
   It prioritizes eligible stock with the earliest expiry date, helping reduce expiry-related waste.

5. **Why use inventory quantity buckets?**  
   They make stock movements explicit and allow the system to enforce the total-quantity invariant.

6. **Why use repository interfaces?**  
   They separate storage concerns from business logic and make it possible to replace the in-memory implementation later.

7. **Why implement snapshots and rollback?**  
   A reservation or dispatch operation can update multiple inventory and reservation objects. Snapshots allow the service to attempt restoration if a later step fails.

8. **Is the rollback equivalent to a database transaction?**  
   No. It is an in-memory recovery mechanism. A database-backed version needs real transaction management and concurrency handling.

---

## 13. Revision history

| Version | Date | Summary |
|---|---|---|
| 1.0 | 2 November 2026 | Initial domain, quality, inventory and customer/order foundation |
| 1.1 | 13 November 2026 | Added BigDecimal migration, quality profiles, reservation and repository progress |
| 2.0 | 27 November 2026 | Updated documentation to reflect implemented InventoryService, FEFO allocation, multi-batch reservations, cancellation, dispatch, rollback, console scenarios and the latest reported JUnit results |

---

## 14. Documentation maintenance

Update this document when implementation behaviour, business rules, repository contracts, test results or project scope changes. Keep implementation status factual: distinguish implemented behaviour from planned enhancements, and update test counts only from a fresh Maven test summary.
