package com.chemicalmanagement.app;

import com.chemicalmanagement.domain.*;
import com.chemicalmanagement.service.QualityControlService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{
            //create a chemical
            Chemical chemical = new Chemical("CHEM-001",
                    "Sulphuric Acid",
                    "Litres",
                    365);

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

//            System.out.println("Batch created successfully.");
//            System.out.println("Batch number : "+batch.getBatchNumber());
//            System.out.println("Chemical Name: "+batch.getChemical().getName());
//            System.out.println("Manufacturing Date: "+batch.getManufacturingDate());
//            System.out.println("Quantity: "+batch.getQuantity());
//            System.out.println("QualityStatus: "+batch.getQualityStatus());
//            System.out.println("Expiry Date: "+batch.getExpiryDate());



            //Define required Quality Test
            QualityTest purityTest = new QualityTest("QT-001",
                    "Purity",
                    95.0,
                    100.0,
                    "%");

            QualityTest viscosity = new QualityTest("QT-002",
                    "Viscosity",
                    10,
                    20,
                    "%");

            QualityTest density = new QualityTest("QT-003",
                    "Density",
                    0,
                    1,
                    "%");

//            System.out.println("----------------------------------");
//            System.out.println("Test ID: "+purityTest.getTestId());
//            System.out.println("Test Name: "+purityTest.getTestName());
//            System.out.println("Test MinimumAcceptableValue: "+purityTest.getMinimumAcceptableValue());
//            System.out.println("Test MaximumAcceptableValue: "+purityTest.getMaximumAcceptableValue());
//            System.out.println("Test Unit: "+purityTest.getUnit());

            //Record QualityTest Batch1 Result
            QualityTestResult resultBatchPurity1 = new QualityTestResult("QTR-001",
                    batch1,
                    purityTest,
                    91);
            QualityTestResult resultBatchViscosity1 = new QualityTestResult("QTR-002",
                    batch1,
                    viscosity,
                    12);
            QualityTestResult resultBatchDensity1= new QualityTestResult("QTR-003",
                    batch1,
                    density,
                    1);

            //Record QualityTest Batch2 Result
            QualityTestResult resultBatchPurity2 = new QualityTestResult("QTR-004",
                    batch2,
                    purityTest,
                    96);

            QualityTestResult resultBatchViscosity2 = new QualityTestResult("QTR-005",
                    batch2,
                    viscosity,
                    12);

            QualityTestResult resultBatchDensity2= new QualityTestResult("QTR-006",
                    batch2,
                    density,
                    1);

            //Record QualityTest Batch3 Result
            QualityTestResult resultBatchPurity3 = new QualityTestResult("QTR-007",
                    batch3,
                    purityTest,
                    95);

            QualityTestResult resultBatchViscosity3 = new QualityTestResult("QTR-008",
                    batch3,
                    viscosity,
                    16);

            QualityTestResult resultBatchDensity3 = new QualityTestResult("QTR-009",
                    batch3,
                    density,
                    0);

            //Store all QualityTests
            List<QualityTest> requiredTests = new ArrayList<>();
            requiredTests.add(purityTest);
            requiredTests.add(viscosity);
            requiredTests.add(density);

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


            //Evaluate the batch quality
            QualityControlService service = new QualityControlService();
            service.evaluateBatch(batch1,requiredTests,resultsBatch1);
            service.evaluateBatch(batch2,requiredTests,resultsBatch2);
            service.evaluateBatch(batch3,requiredTests,resultsBatch3);

            //display the final quality status
            System.out.println("Batch1: "+batch1.getBatchStatus());
            System.out.println("Batch2: "+batch2.getBatchStatus());
            System.out.println("Batch3: "+batch3.getBatchStatus());

            try{
                batch2.completeQualityEvaluation(QualityStatus.PASSED);
            }
            catch(IllegalStateException e){
                System.out.println("Cant Update: " + e.getMessage());
            }
            System.out.println("Batch2 After: "+batch2.getBatchStatus());
            System.out.println("Batch3 After: " + batch3.getBatchStatus());

            Inventory inventory1 = new Inventory("INV-001",
                    batch2);

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



        }catch(IllegalArgumentException e){
            System.out.println("Registration Fails " + e.getMessage());
        }


    }
}
