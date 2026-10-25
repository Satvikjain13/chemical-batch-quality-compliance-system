package com.chemicalmanagement.app;

import com.chemicalmanagement.domain.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        try{
            Chemical chemical = new Chemical("CHEM-001",
                    "Sulphuric Acid",
                    "Litres",
                    365);

            ProductionBatch batch = new ProductionBatch("BATCH-001",
                    chemical,
                    LocalDate.of(2026,10,24),
                    500);

            System.out.println("Batch created successfully.");
            System.out.println("Batch number : "+batch.getbatchNumber());
            System.out.println("Chemical Name: "+batch.getChemical().getName());
            System.out.println("Manufacturing Date: "+batch.getManufacturingDate());
            System.out.println("Quantity: "+batch.getQuantity());
            System.out.println("QualityStatus: "+batch.getQualityStatus());
            System.out.println("Expiry Date: "+batch.getExpiryDate());


            batch.setQuantity(450);
            batch.setQualityStatus(QualityStatus.PASSED);

            QualityTest purityTest = new QualityTest("QT-001",
                    "Purity",
                    95.0,
                    100.0,
                    "%");
            System.out.println("----------------------------------");
            System.out.println("Test ID: "+purityTest.getTestId());
            System.out.println("Test Name: "+purityTest.getTestName());
            System.out.println("Test MinimumAcceptableValue: "+purityTest.getMinimumAcceptableValue());
            System.out.println("Test MaximumAcceptableValue: "+purityTest.getMaximumAcceptableValue());
            System.out.println("Test Unit: "+purityTest.getUnit());

            System.out.println("-------------------------Acceptable Limit Tests-----------------------------------");
            System.out.println("88% : "+purityTest.isWithinAcceptableLimit(88));
            System.out.println("91% : "+purityTest.isWithinAcceptableLimit(91));
            System.out.println("95% : "+purityTest.isWithinAcceptableLimit(95));
            System.out.println("100% : "+purityTest.isWithinAcceptableLimit(100));
            System.out.println("-10% : "+purityTest.isWithinAcceptableLimit(-10));
            System.out.println("98% : "+purityTest.isWithinAcceptableLimit(98));

            QualityTestResult result1 = new QualityTestResult("QTR-001",
                    batch,
                    purityTest,
                    91);

            System.out.println("----------------Quality Test Result1---------------------");
            System.out.println("ResultId: "+result1.getResultId());
            System.out.println("ProductionBatch Number: "+result1.getBatch().getbatchNumber());
            System.out.println("QualityTest Name: "+result1.getQualityTest().getTestName());
            System.out.println("Measured Value: "+result1.getMeasuredValue());
            System.out.println("Passed or Failed: "+result1.isPassed());

        }catch(IllegalArgumentException e){
            System.out.println("Registration Fails " + e.getMessage());
        }

    }
}
