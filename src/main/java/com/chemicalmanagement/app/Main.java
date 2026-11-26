package com.chemicalmanagement.app;

import com.chemicalmanagement.domain.*;
import com.chemicalmanagement.repository.InMemoryInventoryRepository;
import com.chemicalmanagement.repository.InMemoryReservationRepository;
import com.chemicalmanagement.repository.InventoryRepository;
import com.chemicalmanagement.repository.ReservationRepository;
import com.chemicalmanagement.service.InventoryService;
import com.chemicalmanagement.service.OrderService;
import com.chemicalmanagement.service.QualityControlService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   CHEMICAL BATCH QUALITY MANAGEMENT");
        System.out.println("========================================");

        Chemical sulphuricAcid = new Chemical("CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365);

        Chemical hydrochloricAcid = new Chemical("CHEM-002",
                "Hydrochloric Acid",
                "Litres",
                180);

        System.out.println("\n========== REGISTERED CHEMICALS ==========");

        System.out.printf(
                "%-12s | %-22s | %-12s | %-18s%n",
                "Chemical ID",
                "Chemical Name",
                "Unit",
                "Shelf Life (Days)"
        );

        System.out.println("---------------------------------------------------------------------");

        printChemical(sulphuricAcid);
        printChemical(hydrochloricAcid);

        System.out.println("---------------------------------------------------------------------");
        ProductionBatch sulProductionBatch1 = new ProductionBatch("BATCH-001",
                sulphuricAcid,
                LocalDate.of(2026,11,20),
                new BigDecimal("500"));

        ProductionBatch sulProductionBatch2 = new ProductionBatch("BATCH-002",
                sulphuricAcid,
                LocalDate.of(2026,11,22),
                new BigDecimal("1000"));

        ProductionBatch sulProductionBatch3 = new ProductionBatch("BATCH-003",
                sulphuricAcid,
                LocalDate.of(2026,11,24),
                new BigDecimal("800"));

        ProductionBatch hydProductionBatch1 = new ProductionBatch("BATCH-004",
                hydrochloricAcid,
                LocalDate.of(2026,11,21),
                new BigDecimal("1000"));

        System.out.println("\n========== PRODUCTION BATCH DETAILS ==========");

        System.out.printf(
                "%-12s | %-20s | %-15s | %-15s | %10s%n",
                "Batch ID",
                "Chemical",
                "Manufactured",
                "Expiry Date",
                "Quantity"
        );

        System.out.println("--------------------------------------------------------------------------------");

        printBatch(sulProductionBatch1);
        printBatch(sulProductionBatch2);
        printBatch(sulProductionBatch3);
        printBatch(hydProductionBatch1);

        System.out.println("--------------------------------------------------------------------------------");

        QualityTest purityTestSulphuric = new QualityTest("TEST-001",
                "Purity",
                new BigDecimal("95"),
                new BigDecimal("100"),
                "%");

        QualityTest densityTestSulphuric = new QualityTest("TEST-002",
                "Density",
                new BigDecimal("1.80"),
                new BigDecimal("1.85"),
                "g/cm³");

        QualityTest concentrationTestSulphuric = new QualityTest("TEST-003",
                "Concentration",
                new BigDecimal("30"),
                new BigDecimal("35"),
                "%");

        QualityTest purityTestHydrochloric = new QualityTest("TEST-004",
                "Purity",
                new BigDecimal("30"),
                new BigDecimal("35"),
                "%");

        QualityTest densityTestHydrochloric = new QualityTest("TEST-005",
                "Density",
                new BigDecimal("1.15"),
                new BigDecimal("1.20"),
                "g/cm³");

        List<QualityTest> requiredTestsSulphuric = new ArrayList<>();
        requiredTestsSulphuric.add(purityTestSulphuric);
        requiredTestsSulphuric.add(densityTestSulphuric);
        requiredTestsSulphuric.add(concentrationTestSulphuric);

        List<QualityTest> requiredTestsHydrochloric = new ArrayList<>();
        requiredTestsHydrochloric.add(purityTestHydrochloric);
        requiredTestsHydrochloric.add(densityTestHydrochloric);

        ChemicalQualityProfile sulphuricAcidProfile =
                new ChemicalQualityProfile(
                        sulphuricAcid,
                        requiredTestsSulphuric
                );

        ChemicalQualityProfile hydrochloricAcidProfile =
                new ChemicalQualityProfile(
                        hydrochloricAcid,
                        requiredTestsHydrochloric
                );

        Map<String, ChemicalQualityProfile> profileRegistry = new HashMap<>();

        profileRegistry.put(
                sulphuricAcid.getChemicalId(),
                sulphuricAcidProfile
        );

        profileRegistry.put(
                hydrochloricAcid.getChemicalId(),
                hydrochloricAcidProfile
        );

        System.out.println("\n========== CHEMICAL QUALITY PROFILE REGISTRY ==========");

        System.out.printf(
                "%-15s | %-22s | %-15s%n",
                "Chemical ID",
                "Chemical Name",
                "Required Tests"
        );

        System.out.println("----------------------------------------------------------");

        for (Map.Entry<String, ChemicalQualityProfile> entry : profileRegistry.entrySet()) {

            ChemicalQualityProfile profile = entry.getValue();

            System.out.printf(
                    "%-15s | %-22s | %-15d%n",
                    entry.getKey(),
                    profile.getChemical().getName(),
                    profile.getRequiredTests().size()
            );
        }

        System.out.println("----------------------------------------------------------");

        System.out.println("\n========== SULPHURIC ACID QUALITY TESTS ==========");

        printTestHeader();

        printTest(purityTestSulphuric);
        printTest(densityTestSulphuric);
        printTest(concentrationTestSulphuric);

        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n========== HYDROCHLORIC ACID QUALITY TESTS ==========");

        printTestHeader();

        printTest(purityTestHydrochloric);
        printTest(densityTestHydrochloric);

        System.out.println("--------------------------------------------------------------------------");

        QualityTestResult sulProductionBatch1PurityResult = new QualityTestResult("QTR-001",
                sulProductionBatch1,
                purityTestSulphuric,
                new BigDecimal("98"));

        QualityTestResult sulProductionBatch1DensityResult = new QualityTestResult("QTR-002",
                sulProductionBatch1,
                densityTestSulphuric,
                new BigDecimal("1.82"));

        QualityTestResult sulProductionBatch1ConcentrationResult = new QualityTestResult("QTR-003",
                sulProductionBatch1,
                concentrationTestSulphuric,
                new BigDecimal("33"));

        QualityTestResult sulProductionBatch2PurityResult = new QualityTestResult(
                "QTR-004",
                sulProductionBatch2,
                purityTestSulphuric,
                new BigDecimal("97")
        );

        QualityTestResult sulProductionBatch2DensityResult = new QualityTestResult(
                "QTR-005",
                sulProductionBatch2,
                densityTestSulphuric,
                new BigDecimal("1.83")
        );

        QualityTestResult sulProductionBatch2ConcentrationResult = new QualityTestResult(
                "QTR-006",
                sulProductionBatch2,
                concentrationTestSulphuric,
                new BigDecimal("32")
        );

        QualityTestResult sulProductionBatch3PurityResult = new QualityTestResult(
                "QTR-007",
                sulProductionBatch3,
                purityTestSulphuric,
                new BigDecimal("92")
        );

        QualityTestResult sulProductionBatch3DensityResult = new QualityTestResult(
                "QTR-008",
                sulProductionBatch3,
                densityTestSulphuric,
                new BigDecimal("1.83")
        );

        QualityTestResult sulProductionBatch3ConcentrationResult = new QualityTestResult(
                "QTR-009",
                sulProductionBatch3,
                concentrationTestSulphuric,
                new BigDecimal("32")
        );

        QualityTestResult hydProductionBatch1PurityResult = new QualityTestResult(
                "QTR-010",
                hydProductionBatch1,
                purityTestHydrochloric,
                new BigDecimal("33")
        );

        QualityTestResult hydProductionBatch1DensityResult = new QualityTestResult(
                "QTR-011",
                hydProductionBatch1,
                densityTestHydrochloric,
                new BigDecimal("1.22")
        );

        System.out.println("\n========== QUALITY TEST RESULTS ==========");

        System.out.printf(
                "%-12s | %-12s | %-20s | %-18s | %12s | %-10s%n",
                "Result ID",
                "Batch ID",
                "Chemical",
                "Test Name",
                "Actual Value",
                "Result"
        );

        System.out.println("------------------------------------------------------------------------------------------------");

// BATCH-001: Sulphuric Acid
        System.out.println("\n--- BATCH-001 ---");
        printQualityResult(sulProductionBatch1PurityResult);
        printQualityResult(sulProductionBatch1DensityResult);
        printQualityResult(sulProductionBatch1ConcentrationResult);

// BATCH-002: Sulphuric Acid
        System.out.println("\n--- BATCH-002 ---");
        printQualityResult(sulProductionBatch2PurityResult);
        printQualityResult(sulProductionBatch2DensityResult);
        printQualityResult(sulProductionBatch2ConcentrationResult);

// BATCH-003: Sulphuric Acid
        System.out.println("\n--- BATCH-003 ---");
        printQualityResult(sulProductionBatch3PurityResult);
        printQualityResult(sulProductionBatch3DensityResult);
        printQualityResult(sulProductionBatch3ConcentrationResult);

// BATCH-004: Hydrochloric Acid
        System.out.println("\n--- BATCH-004 ---");
        printQualityResult(hydProductionBatch1PurityResult);
        printQualityResult(hydProductionBatch1DensityResult);

        System.out.println("------------------------------------------------------------------------------------------------");

        List<QualityTestResult> allQualityResults = new ArrayList<>();

        // BATCH-001
        allQualityResults.add(sulProductionBatch1PurityResult);
        allQualityResults.add(sulProductionBatch1DensityResult);
        allQualityResults.add(sulProductionBatch1ConcentrationResult);

        // BATCH-002
        allQualityResults.add(sulProductionBatch2PurityResult);
        allQualityResults.add(sulProductionBatch2DensityResult);
        allQualityResults.add(sulProductionBatch2ConcentrationResult);

        // BATCH-003
        allQualityResults.add(sulProductionBatch3PurityResult);
        allQualityResults.add(sulProductionBatch3DensityResult);
        allQualityResults.add(sulProductionBatch3ConcentrationResult);

        // BATCH-004
        allQualityResults.add(hydProductionBatch1PurityResult);
        allQualityResults.add(hydProductionBatch1DensityResult);

        QualityControlService qualityControlService = new QualityControlService();

        qualityControlService.evaluateBatchUsingProfile(
                sulProductionBatch1,
                allQualityResults,
                profileRegistry
        );

        qualityControlService.evaluateBatchUsingProfile(
                sulProductionBatch2,
                allQualityResults,
                profileRegistry
        );

        qualityControlService.evaluateBatchUsingProfile(
                sulProductionBatch3,
                allQualityResults,
                profileRegistry
        );

        qualityControlService.evaluateBatchUsingProfile(
                hydProductionBatch1,
                allQualityResults,
                profileRegistry
        );
        System.out.println("\n========== FINAL BATCH QUALITY STATUS ==========");

        System.out.printf(
                "%-15s | %-22s | %-20s%n",
                "Batch ID",
                "Chemical",
                "Batch Status"
        );

        System.out.println("---------------------------------------------------------------");

        printBatchStatus(sulProductionBatch1);
        printBatchStatus(sulProductionBatch2);
        printBatchStatus(sulProductionBatch3);
        printBatchStatus(hydProductionBatch1);

        System.out.println("---------------------------------------------------------------");


        // Initialize repositories
        InventoryRepository inventoryRepository =
                new InMemoryInventoryRepository();

        ReservationRepository reservationRepository =
                new InMemoryReservationRepository();

        // Initialize Inventory Service
        InventoryService inventoryService =
                new InventoryService(inventoryRepository, reservationRepository);

        // Create inventory for approved batches
        Inventory inventory1 =
                new Inventory("INV-001", sulProductionBatch1);

        Inventory inventory2 =
                new Inventory("INV-002", sulProductionBatch2);

        // Save inventory
        inventoryRepository.save(inventory1);
        inventoryRepository.save(inventory2);

        printInventoryDetails(inventoryRepository, "INITIAL INVENTORY");
        printInventoryDetails(inventoryRepository);

        // Create customer
        Customer customer1 = new Customer(
                "CUST-001",
                "Rahul Sharma",
                "9876543210",
                "rahul@gmail.com",
                "Indore, Madhya Pradesh"
        );

        // Create customer order
        CustomerOrder order1 = new CustomerOrder(
                "ORD-001",
                customer1,
                sulphuricAcid,
                new BigDecimal("700")
        );

        // Initialize OrderService
        OrderService orderService = new OrderService(inventoryService);
        System.out.println("\n========== CUSTOMER ORDER DETAILS ==========");

        System.out.println("Order ID       : " + order1.getOrderId());
        System.out.println("Customer       : " + order1.getCustomerNameAtOrder());
        System.out.println("Chemical       : " + order1.getChemical().getName());
        System.out.println("Order Quantity : " + order1.getOrderQuantity());
        System.out.println("Order Status   : " + order1.getOrderStatus());


        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
        // Validate order
        orderService.validateOrder(order1);

        System.out.println("\nOrder validated successfully.");

        // Reserve inventory
        boolean reserved = orderService.reserveOrder(order1);

        if (reserved) {
            System.out.println("Inventory reserved successfully.");
        } else {
            System.out.println("Insufficient inventory. Order rejected.");
        }

        System.out.println("Final Order Status: " + order1.getOrderStatus());

        printInventoryDetails(inventoryRepository, "INVENTORY AFTER RESERVATION");
        printInventoryDetails(inventoryRepository);

        // Dispatch reserved inventory
        System.out.println("\n========== ORDER DISPATCH ==========");

        orderService.dispatchOrder(order1);

        System.out.println("Order ID     : " + order1.getOrderId());
        System.out.println("Order Status : " + order1.getOrderStatus());

        // Display inventory after dispatch
        printInventoryDetails(inventoryRepository, "INVENTORY AFTER DISPATCH");
        printInventoryDetails(inventoryRepository);

        // Create second customer order
        CustomerOrder order2 = new CustomerOrder(
                "ORD-002",
                customer1,
                sulphuricAcid,
                new BigDecimal("300")
        );
        System.out.println("\n========== SECOND CUSTOMER ORDER ==========");

        System.out.println("Order ID       : " + order2.getOrderId());
        System.out.println("Customer       : " + order2.getCustomerNameAtOrder());
        System.out.println("Chemical       : " + order2.getChemical().getName());
        System.out.println("Order Quantity : " + order2.getOrderQuantity());
        System.out.println("Order Status   : " + order2.getOrderStatus());

        orderService.validateOrder(order2);

        boolean secondOrderReserved = orderService.reserveOrder(order2);

        if (secondOrderReserved) {
            System.out.println("Inventory reserved successfully.");
        } else {
            System.out.println("Insufficient inventory.");
        }

        System.out.println("Final Order Status: " + order2.getOrderStatus());

        printInventoryDetails(
                inventoryRepository,
                "INVENTORY AFTER SECOND RESERVATION"
        );
        printInventoryDetails(inventoryRepository);
        System.out.println("\n========== ORDER CANCELLATION ==========");

        orderService.cancelOrder(order2);

        System.out.println("Order ID     : " + order2.getOrderId());
        System.out.println("Order Status : " + order2.getOrderStatus());

        printInventoryDetails(
                inventoryRepository,
                "INVENTORY AFTER CANCELLATION"
        );
        printInventoryDetails(inventoryRepository);

        // Create third customer order
        CustomerOrder order3 = new CustomerOrder(
                "ORD-003",
                customer1,
                sulphuricAcid,
                new BigDecimal("2000")
        );

        System.out.println("\n========== THIRD CUSTOMER ORDER ==========");

        System.out.println("Order ID       : " + order3.getOrderId());
        System.out.println("Customer       : " + order3.getCustomerNameAtOrder());
        System.out.println("Chemical       : " + order3.getChemical().getName());
        System.out.println("Order Quantity : " + order3.getOrderQuantity());
        System.out.println("Order Status   : " + order3.getOrderStatus());

// Validate order
        orderService.validateOrder(order3);

        System.out.println("\nOrder validated successfully.");

// Attempt inventory reservation
        boolean thirdOrderReserved = orderService.reserveOrder(order3);

        if (thirdOrderReserved) {
            System.out.println("Inventory reserved successfully.");
        } else {
            System.out.println("Insufficient inventory. Order rejected.");
        }

        System.out.println("Final Order Status: " + order3.getOrderStatus());

        // Display inventory after failed reservation
        printInventoryDetails(
                inventoryRepository,
                "INVENTORY AFTER REJECTED ORDER"
        );
        printInventoryDetails(inventoryRepository);

        // Create second customer
        Customer customer2 = new Customer(
                "CUST-002",
                "Amit Verma",
                "9876543211",
                "amit@gmail.com",
                "Indore, Madhya Pradesh"
        );

        CustomerOrder order4 = new CustomerOrder(
                "ORD-004",
                customer2,
                sulphuricAcid,
                new BigDecimal("200")
        );

        customer2.setCustomerStatus(CustomerStatus.INACTIVE);

        System.out.println("\n========== CUSTOMER STATUS ==========");
        System.out.println("Customer ID     : " + customer2.getCustomerId());
        System.out.println("Customer Name   : " + customer2.getCustomerName());
        System.out.println("Customer Status : " + customer2.getCustomerStatus());

        System.out.println("\n========== INACTIVE CUSTOMER ORDER TEST ==========");

        try {
            orderService.validateOrder(order4);

            System.out.println("Order validated successfully.");

        } catch (IllegalStateException e) {
            System.out.println("Order validation failed: " + e.getMessage());
        }

        System.out.println("Order ID     : " + order4.getOrderId());
        System.out.println("Order Status : " + order4.getOrderStatus());

        printInventoryDetails(
                inventoryRepository,
                "INVENTORY AFTER INACTIVE CUSTOMER TEST"
        );
        printInventoryDetails(inventoryRepository);

        System.out.println("\n========== INACTIVE CUSTOMER ORDER CREATION TEST ==========");

        try {
            CustomerOrder order5 = new CustomerOrder(
                    "ORD-005",
                    customer2,
                    sulphuricAcid,
                    new BigDecimal("200")
            );

            System.out.println("Order created: " + order5.getOrderId());

        } catch (IllegalStateException e) {
            System.out.println("Order creation failed: " + e.getMessage());
        }
    }



    private static void printChemical(Chemical chemical) {
        System.out.printf(
                "%-12s | %-22s | %-12s | %-18d%n",
                chemical.getChemicalId(),
                chemical.getName(),
                chemical.getUnit(),
                chemical.getShelfLifeDays()
        );
    }

    private static void printBatch(ProductionBatch batch) {
        System.out.printf(
                "%-12s | %-20s | %-15s | %-15s | %10s%n",
                batch.getBatchNumber(),
                batch.getChemical().getName(),
                batch.getManufacturingDate(),
                batch.getExpiryDate(),
                batch.getQuantity().toPlainString()
        );
    }

    private static void printTest(QualityTest qualityTest) {
        System.out.printf(
                "%-12s | %-18s | %10s | %10s | %-10s%n",
                qualityTest.getTestId(),
                qualityTest.getTestName(),
                qualityTest.getMinimumAcceptableValue(),
                qualityTest.getMaximumAcceptableValue(),
                qualityTest.getUnit()
        );
    }
    private static void printTestHeader() {
        System.out.printf(
                "%-12s | %-18s | %10s | %10s | %-10s%n",
                "Test ID",
                "Test Name",
                "Minimum",
                "Maximum",
                "Unit"
        );

        System.out.println("--------------------------------------------------------------------------");
    }

    private static void printQualityResult(QualityTestResult result) {
        System.out.printf(
                "%-12s | %-12s | %-20s | %-18s | %12s | %-10s%n",
                result.getResultId(),
                result.getBatch().getBatchNumber(),
                result.getBatch().getChemical().getName(),
                result.getQualityTest().getTestName(),
                result.getMeasuredValue(),
                result.isPassed() ? "PASSED" : "FAILED"
        );
    }
    private static void printBatchStatus(ProductionBatch batch) {
        System.out.printf(
                "%-15s | %-22s | %-20s%n",
                batch.getBatchNumber(),
                batch.getChemical().getName(),
                batch.getBatchStatus()
        );
    }
    private static void printInventoryDetails(InventoryRepository inventoryRepository) {

        System.out.printf(
                "%-12s | %-12s | %-20s | %12s | %12s | %12s | %12s | %12s%n",
                "Inventory ID",
                "Batch ID",
                "Chemical",
                "Total",
                "Available",
                "Reserved",
                "Dispatched",
                "Damaged"
        );

        System.out.println("------------------------------------------------------------------------------------------------------------------------------");

        for (Inventory inventory : inventoryRepository.findAll()) {

            System.out.printf(
                    "%-12s | %-12s | %-20s | %12s | %12s | %12s | %12s | %12s%n",
                    inventory.getInventoryId(),
                    inventory.getBatch().getBatchNumber(),
                    inventory.getBatch().getChemical().getName(),
                    inventory.getTotalQuantity(),
                    inventory.getAvailableQuantity(),
                    inventory.getReservedQuantity(),
                    inventory.getDispatchedQuantity(),
                    inventory.getDamagedQuantity()
            );
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
    }
    private static void printInventoryDetails(
            InventoryRepository inventoryRepository,
            String heading) {

        System.out.println("\n========== " + heading + " ==========");

        // Keep the remaining table-printing code unchanged.
    }
}
