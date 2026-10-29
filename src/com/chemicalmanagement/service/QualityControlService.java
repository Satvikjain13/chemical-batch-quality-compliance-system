package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.ProductionBatch;
import com.chemicalmanagement.domain.QualityStatus;
import com.chemicalmanagement.domain.QualityTest;
import com.chemicalmanagement.domain.QualityTestResult;
import java.util.List;

public class QualityControlService {
    public void evaluateBatch(ProductionBatch batch,
                              List<QualityTest>  requiredTests,
                              List<QualityTestResult> results){

        // Validate the inputs
        if(batch == null){
            throw new IllegalArgumentException("Production Batch cannot be Null");
        }

        if(requiredTests == null || requiredTests.isEmpty()){
            throw new IllegalArgumentException("Required Tests cannot be Empty or Null ");
        }

        if(results == null){
            throw new IllegalArgumentException("Results cannot be null.");
        }
        // Track test failures and completion
        boolean hasFailedTest = false;
        boolean allTestsCompleted = true;

        // Check each required test
        for(QualityTest requiredTest : requiredTests){
            boolean resultFound = false;
            for(QualityTestResult result : results){
                // Find the result for this batch and test
                if(result.getBatch().getBatchNumber().equals(batch.getBatchNumber())
                && (result.getQualityTest().getTestId().equals(requiredTest.getTestId()))){
                    resultFound = true;
                    // Record if the test failed
                    if(!result.isPassed()){
                        hasFailedTest = true;
                    }
                    break;
                }
            }
            // Mark the batch as incomplete if a result is missing
            if(!resultFound){
                allTestsCompleted = false;
            }
        }

        // Set the final batch statu
        if(hasFailedTest){
            batch.setQualityStatus(QualityStatus.FAILED);
        }else if(!allTestsCompleted){
            batch.setQualityStatus(QualityStatus.PENDING);
        }else{
            batch.setQualityStatus(QualityStatus.PASSED);
        }

    }
}
