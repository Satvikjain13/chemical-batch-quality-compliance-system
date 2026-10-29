package com.chemicalmanagement.app;

import com.chemicalmanagement.domain.*;
import com.chemicalmanagement.service.QualityControlService;

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
            ProductionBatch batch = new ProductionBatch("BATCH-001",
                    chemical,
                    LocalDate.of(2026,10,24),
                    500);

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

            //Record QualityTest Result
            //Record QualityTest Result
            QualityTestResult result1 = new QualityTestResult("QTR-001",
                    batch,
                    purityTest,
                    95);
            QualityTestResult result2 = new QualityTestResult("QTR-002",
                    batch,
                    viscosity,
                    12);
            QualityTestResult result3 = new QualityTestResult("QTR-003",
                    batch,
                    density,
                    1);

            //Store all QualityTests
            List<QualityTest> requiredTests = new ArrayList<>();
            requiredTests.add(purityTest);
            requiredTests.add(viscosity);
            requiredTests.add(density);

            //Store all QualityTests Result
            List<QualityTestResult> results = new ArrayList<>();
            results.add(result1);
            results.add(result2);
            results.add(result3);

            //Evaluate the batch quality
            QualityControlService service = new QualityControlService();
            service.evaluateBatch(batch,requiredTests,results);
            //display the final quality status
            System.out.println(batch.getQualityStatus());

        }catch(IllegalArgumentException e){
            System.out.println("Registration Fails " + e.getMessage());
        }

    }
}
