package com.chemicalmanagement.app;

import com.chemicalmanagement.domain.*;
import com.chemicalmanagement.repository.InMemoryInventoryRepository;
import com.chemicalmanagement.service.OrderService;
import com.chemicalmanagement.service.QualityControlService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try{
            //create a chemical
            Chemical chemical = new Chemical("CHEM-001",
                    "Sulphuric Acid",
                    "Litres",
                    365);

            Chemical chemical2 = new Chemical("CHEM-002",
                    "Hydrochloric Acid",
                            "Litres",
                    180);

            //create a Production Batch
            ProductionBatch batch1 = new ProductionBatch("BATCH-001",
                    chemical,
                    LocalDate.of(2026,10,24),
                    new BigDecimal("500"));

            ProductionBatch batch2 = new ProductionBatch("BATCH-002",
                    chemical,
                    LocalDate.of(2026,10,30),
                    new BigDecimal("1000"));

            ProductionBatch batch3 = new ProductionBatch("BATCH-003",
                    chemical,
                    LocalDate.of(2026,10,31),
                    new BigDecimal("800"));

            ProductionBatch hydBatch1 = new ProductionBatch("BATCH-004",
                    chemical2,
                    LocalDate.of(2026,11,02),
                    new BigDecimal("1000"));

//            System.out.println("Batch created successfully.");
//            System.out.println("Batch number : "+batch.getBatchNumber());
//            System.out.println("Chemical Name: "+batch.getChemical().getName());
//            System.out.println("Manufacturing Date: "+batch.getManufacturingDate());
//            System.out.println("Quantity: "+batch.getQuantity());
//            System.out.println("QualityStatus: "+batch.getQualityStatus());
//            System.out.println("Expiry Date: "+batch.getExpiryDate());



            //Define Sulphuric Acid  required Quality Test
            QualityTest sulphuricPurity = new QualityTest("SQT-001",
                    "Purity",
                    new BigDecimal("95.0"),
                    new BigDecimal("100.0"),
                    "%");

            QualityTest sulphuricViscosity = new QualityTest("SQT-002",
                    "Viscosity",
                    new BigDecimal("10"),
                    new BigDecimal("20"),
                    "cP");

            QualityTest sulphuricDensity = new QualityTest("SQT-003",
                    "Density",
                    new BigDecimal("0"),
                    new BigDecimal("1"),
                    "g/cm3");

            //Define Hydrochloric Acid  required Quality Test
            QualityTest hydrochloricPurity = new QualityTest("HQT-004",
                    "Purity",
                    new BigDecimal("99"),
                    new BigDecimal("100"),
                    "%");

            QualityTest hydrochloricDensity = new QualityTest("HQT-005",
                    "Density",
                    new BigDecimal("0.1"),
                    new BigDecimal("0.5"),
                    "g/cm3");

            //Store all of Sulphuric Acid QualityTests
            List<QualityTest> sulphuricTests = new ArrayList<>();
            sulphuricTests.add(sulphuricPurity);
            sulphuricTests.add(sulphuricViscosity);
            sulphuricTests.add(sulphuricDensity);

            //Store all of Hydrochloric  Acid QualityTests
            List<QualityTest> hydrochloricTests = new ArrayList<>();
            hydrochloricTests.add(hydrochloricPurity);
            hydrochloricTests.add(hydrochloricDensity);

            //Record QualityTest Batch1 Result
            QualityTestResult resultBatchPurity1 = new QualityTestResult("QTR-001",
                    batch1,
                    sulphuricPurity,
                    new BigDecimal("95"));
            QualityTestResult resultBatchViscosity1 = new QualityTestResult("QTR-002",
                    batch1,
                    sulphuricViscosity,
                    new BigDecimal("12"));
            QualityTestResult resultBatchDensity1= new QualityTestResult("QTR-003",
                    batch1,
                    sulphuricDensity,
                    new BigDecimal("1"));

            //Record QualityTest Batch2 Result
            QualityTestResult resultBatchPurity2 = new QualityTestResult("QTR-004",
                    batch2,
                    sulphuricPurity,
                    new BigDecimal("96"));

            QualityTestResult resultBatchViscosity2 = new QualityTestResult("QTR-005",
                    batch2,
                    sulphuricViscosity,
                    new BigDecimal("12"));

            QualityTestResult resultBatchDensity2= new QualityTestResult("QTR-006",
                    batch2,
                    sulphuricDensity,
                    new BigDecimal("1"));

            //Record QualityTest Batch3 Result
            QualityTestResult resultBatchPurity3 = new QualityTestResult("QTR-007",
                    batch3,
                    sulphuricPurity,
                    new BigDecimal("95"));

            QualityTestResult resultBatchViscosity3 = new QualityTestResult("QTR-008",
                    batch3,
                    sulphuricViscosity,
                    new BigDecimal("16"));

            QualityTestResult resultBatchDensity3 = new QualityTestResult("QTR-009",
                    batch3,
                    sulphuricDensity,
                    new BigDecimal("0"));

            //Record QualityTest Hydrochloric Batch1 Result
            QualityTestResult resultHydBatchPurity1= new QualityTestResult("QTR-010",
                    hydBatch1,
                    hydrochloricPurity,
                    new BigDecimal("95"));

            QualityTestResult resultHydBatchDensity1= new QualityTestResult("QTR-012",
                    hydBatch1,
                    hydrochloricDensity,
                    new BigDecimal("0.2"));

//Store all QualityTests ResultBatch1
            List<QualityTestResult> resultsBatch1 = new ArrayList<>();
            resultsBatch1.add(resultBatchPurity1);
            resultsBatch1.add(resultBatchViscosity1);
            resultsBatch1.add(resultBatchDensity1);

            //Store all QualityTests ResultBatch2
            List<QualityTestResult> resultsBatch2 = new ArrayList<>();
            resultsBatch2.add(resultBatchPurity2);
            resultsBatch2.add(resultBatchViscosity2);
            resultsBatch2.add(resultBatchDensity2);

            //Store all QualityTests ResultBatch3
            List<QualityTestResult> resultsBatch3 = new ArrayList<>();
            resultsBatch3.add(resultBatchPurity3);
            resultsBatch3.add(resultBatchViscosity3);
            resultsBatch3.add(resultBatchDensity3);

            //Store all QualityTests ResultHydBatch1
            List<QualityTestResult> resultHydBatch1 = new ArrayList<>();
            resultHydBatch1.add(resultHydBatchPurity1);
            resultHydBatch1.add(resultHydBatchDensity1);


            //Creating Profiles of a Chemical1
            ChemicalQualityProfile sulphuricProfile = new ChemicalQualityProfile(chemical,
                    sulphuricTests);

            //Creating Profiles of a Chemical2
            ChemicalQualityProfile hydrochloricProfile = new ChemicalQualityProfile(chemical2,
                    hydrochloricTests);

            Map<String,ChemicalQualityProfile> profileRegistry = new HashMap<>();
            profileRegistry.put(chemical.getChemicalId(),sulphuricProfile);
            profileRegistry.put(chemical2.getChemicalId(),hydrochloricProfile);

            QualityControlService service = new QualityControlService();

//            System.out.println("----------------------------------");
//            System.out.println("Test ID: "+purityTest.getTestId());
//            System.out.println("Test Name: "+purityTest.getTestName());
//            System.out.println("Test MinimumAcceptableValue: "+purityTest.getMinimumAcceptableValue());
//            System.out.println("Test MaximumAcceptableValue: "+purityTest.getMaximumAcceptableValue());
//            System.out.println("Test Unit: "+purityTest.getUnit());


            //Evaluate the batch quality
            System.out.println("---------------------------------------");
            evaluateBatchUsingProfile(
                    batch1, resultsBatch1, profileRegistry, service);

            evaluateBatchUsingProfile(
                    batch2, resultsBatch2, profileRegistry, service);

            evaluateBatchUsingProfile(
                    batch3, resultsBatch3, profileRegistry, service);

            evaluateBatchUsingProfile(
                    hydBatch1, resultHydBatch1, profileRegistry, service);


            try{
                batch2.completeQualityEvaluation(QualityStatus.PASSED);
            }
            catch(IllegalStateException e){
                System.out.println("Cant Update: " + e.getMessage());
            }
            System.out.println("Batch2 After: "+batch2.getBatchStatus());
            System.out.println("Batch3 After: " + batch3.getBatchStatus());

            //Inventory
            Inventory inventory1 = new Inventory("INV-001",
                    batch1);

            Inventory inventory2 = new Inventory("INV-002",
                    batch2);

            Inventory inventory3 = new Inventory("INV-003",
                    batch3);

//            inventory1.reserveQuantity(new BigDecimal("300"));
//            System.out.println("-----------------------------------------------");
//            System.out.println("Total Quantity: "+inventory1.getTotalQuantity());
//            System.out.println("Available Quantity: "+inventory1.getAvailableQuantity());
//            System.out.println("ReservedQuantity: "+inventory1.getReservedQuantity());

//            inventory1.releaseReservedQuantity(new BigDecimal("100"));
//            System.out.println("Total Quantity: "+inventory1.getTotalQuantity());
//            System.out.println("Available Quantity: "+inventory1.getAvailableQuantity());
//            System.out.println("ReservedQuantity: "+inventory1.getReservedQuantity());
//
//            inventory1.releaseReservedQuantity(new BigDecimal("300"));
//            System.out.println("Total Quantity: "+inventory1.getTotalQuantity());
//            System.out.println("Available Quantity: "+inventory1.getAvailableQuantity());
//            System.out.println("ReservedQuantity: "+inventory1.getReservedQuantity());

//            inventory1.dispatchQuantity(new BigDecimal("200"));
//            System.out.println("Dispatch Quantity: "+inventory1.getDispatchedQuantity());
//            System.out.println("Reserve Quantity: "+inventory1.getReservedQuantity());
//            inventory1.releaseReservedQuantity(new BigDecimal("50"));
//            System.out.println("Available Quantity: "+inventory1.getAvailableQuantity());
//            System.out.println("Reserve Quantity: "+inventory1.getReservedQuantity());
//            try{
//                inventory1.dispatchQuantity(new BigDecimal("100"));
//            }catch(IllegalStateException e){
//                System.out.println(e.getMessage());
//            }
            System.out.println("--------------------------------------");
            System.out.println("Total Quantity: "+inventory1.getTotalQuantity());
            System.out.println("Available Quantity: "+inventory1.getAvailableQuantity());
            inventory1.reserveQuantity(new BigDecimal("400"));
            System.out.println("Reserved Quantity: "+inventory1.getReservedQuantity());
            System.out.println("Available Quantity: "+inventory1.getAvailableQuantity());
            inventory1.dispatchQuantity(new BigDecimal("150"));
            System.out.println("Dispatch Quantity: "+inventory1.getDispatchedQuantity());
            System.out.println("Reserved Quantity: "+inventory1.getReservedQuantity());
            inventory1.releaseReservedQuantity(new BigDecimal("100"));
            System.out.println("Release Quantity: "+inventory1.getReservedQuantity());
            System.out.println("Available Quantity: "+inventory1.getAvailableQuantity());
            inventory1.recordDamageFromAvailable(new BigDecimal("50"));
            System.out.println("Damaged Quantity: "+inventory1.getDamagedQuantity());
            System.out.println("Available Quantity: "+inventory1.getAvailableQuantity());

            System.out.println("-----------------------------------------------");
            System.out.println("Total Quantity: "+inventory2.getTotalQuantity());
            System.out.println("Available Quantity: "+inventory2.getAvailableQuantity());
            inventory2.reserveQuantity(new BigDecimal("400"));
            System.out.println("Reserved Quantity: "+inventory2.getReservedQuantity());
            System.out.println("Available Quantity: "+inventory2.getAvailableQuantity());
            inventory2.dispatchQuantity(new BigDecimal("150"));
            System.out.println("Dispatch Quantity: "+inventory2.getDispatchedQuantity());
            System.out.println("Reserved Quantity: "+inventory2.getReservedQuantity());
            inventory2.releaseReservedQuantity(new BigDecimal("100"));
            System.out.println("Release Quantity: "+inventory2.getReservedQuantity());
            System.out.println("Available Quantity: "+inventory2.getAvailableQuantity());
            inventory2.recordDamageFromAvailable(new BigDecimal("50"));
            System.out.println("Damaged Quantity: "+inventory2.getDamagedQuantity());
            System.out.println("Available Quantity: "+inventory2.getAvailableQuantity());

            System.out.println("--------------------------------------------");
            System.out.println("Total Quantity: "+inventory3.getTotalQuantity());
            System.out.println("Available Quantity: "+inventory3.getAvailableQuantity());
            inventory3.reserveQuantity(new BigDecimal("400"));
            System.out.println("Reserved Quantity: "+inventory3.getReservedQuantity());
            System.out.println("Available Quantity: "+inventory3.getAvailableQuantity());
            inventory3.dispatchQuantity(new BigDecimal("150"));
            System.out.println("Dispatch Quantity: "+inventory3.getDispatchedQuantity());
            System.out.println("Reserved Quantity: "+inventory3.getReservedQuantity());
            inventory3.releaseReservedQuantity(new BigDecimal("100"));
            System.out.println("Release Quantity: "+inventory3.getReservedQuantity());
            System.out.println("Available Quantity: "+inventory3.getAvailableQuantity());
            inventory3.recordDamageFromAvailable(new BigDecimal("50"));
            System.out.println("Damaged Quantity: "+inventory3.getDamagedQuantity());
            System.out.println("Available Quantity: "+inventory3.getAvailableQuantity());


            Customer customer = new Customer("CUST-001",
                    "ABC Chemicals",
                    "9039779122",
                    "satvik@gmail.com",
                    "Indore");

            CustomerOrder order1 = new CustomerOrder("ORD-001",
                    customer,
                    chemical,
                    new BigDecimal("500"));

            CustomerOrder order2 = new CustomerOrder("ORD-002",
                    customer,
                    chemical2,
                    new BigDecimal("1500"));

            System.out.println("-----------------------Order Details-----------------------------------");
            System.out.println("Order Id: "+order1.getOrderId());
            System.out.println("Customer Name: "+order1.getCustomer().getCustomerName());
            System.out.println("Chemical Name: "+order1.getChemical().getName());
            System.out.println("Order Quantity: "+order1.getOrderQuantity());
            System.out.println("Order Date: "+order1.getOrderDate());
            System.out.println("Order Status: "+order1.getOrderStatus());
            System.out.println("Order Name At: "+order1.getCustomerNameAtOrder());
            System.out.println("Order Contact At: "+order1.getCustomerContactAtOrder());
            System.out.println("Order Email At: "+order1.getCustomerEmailAtOrder());
            System.out.println("Order Address At: "+order1.getCustomerAddressAtOrder());

            customer.setContactNumber("9123456780");
            System.out.println("Customer Current Contact: "+customer.getContactNumber());
            System.out.println("Order Historical Contact: "+order1.getCustomerContactAtOrder());

//            //Active and Created and also Validated to Validated check
//            OrderService service1 = new OrderService();
//            System.out.println(order1.getOrderStatus());
//            service1.validateOrder(order1);
//            System.out.println(order1.getOrderStatus());
//            service1.validateOrder(order1);
//            System.out.println(order1.getOrderStatus());

            //When Customer is Inactive and null
            OrderService service2 = new OrderService();
            System.out.println(order2.getOrderStatus());
            customer.setCustomerStatus(CustomerStatus.INACTIVE);
//            try{
//                service2.validateOrder(order2);
//            }catch(IllegalStateException e){
//                System.out.println(e.getMessage());
//            }
//            service2.validateOrder(null);
//            System.out.println(order2.getOrderStatus());

            Reservation reservation1 = new Reservation("RES-001",
                    order1,
                    inventory1,
                    new BigDecimal("500"));

//            System.out.println("-----------------------------------------");
//            System.out.println(reservation1.getReservationStatus());
//            reservation1.markAsReleased();
//            System.out.println(reservation1.getReservationStatus());
//            reservation1.markAsDispatched();
//            System.out.println(reservation1.getReservationStatus());

            InMemoryInventoryRepository repo = new InMemoryInventoryRepository();
            repo.save(inventory1);
            repo.save(inventory2);
            repo.save(inventory3);

            for(Inventory inv : repo.findAll()){
                System.out.println(inv.getInventoryId());
            }
            Optional<Inventory> result = repo.findById("INV-001");
            System.out.println(result.isPresent());
            System.out.println(result);
            Optional<Inventory> missing = repo.findById("INV-999");
            System.out.println(missing.isPresent());
            System.out.println(missing);
            System.out.println(repo.existsById("INV-001"));
            repo.save(inventory1);





        }catch(IllegalArgumentException e){
            System.out.println("Registration Fails " + e.getMessage());
        }


    }
    private static void evaluateBatchUsingProfile(
            ProductionBatch batch,
            List<QualityTestResult> results,
            Map<String, ChemicalQualityProfile> profileRegistry,
            QualityControlService service) {
        // Get chemical ID from the batch
        String chemicalId = batch.getChemical().getChemicalId();
        // Retrieve the correct chemical profile
        ChemicalQualityProfile profile = profileRegistry.get(chemicalId);
        // Check whether profile exists
        if (profile == null) {
            throw new IllegalStateException(
                    "No quality profile found for: " + chemicalId);
        }
        // Get required tests from profile
        List<QualityTest> requiredTests = profile.getRequiredTests();
        // Evaluate batch
        service.evaluateBatch(batch, requiredTests, results);
        // Display result
        System.out.println(
                batch.getBatchNumber() + ": " + batch.getBatchStatus());
    }
}
