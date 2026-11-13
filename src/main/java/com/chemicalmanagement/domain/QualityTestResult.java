package com.chemicalmanagement.domain;

import java.math.BigDecimal;

public class QualityTestResult {
    private final String resultId;
    private final ProductionBatch batch;
    private final QualityTest qualityTest;
    private final BigDecimal measuredValue;
    private final boolean passed;

    public QualityTestResult(String resultId, ProductionBatch batch, QualityTest qualityTest, BigDecimal measuredValue){
        if(resultId == null || resultId.isBlank()){
            throw new IllegalArgumentException("resultId cannot be null or blank.");
        }

        if(batch == null){
            throw new IllegalArgumentException("ProductionBatch cannot be null. ");
        }

        if(qualityTest == null){
            throw new IllegalArgumentException("QualityTest cannot be null. ");
        }

        if(measuredValue == null){
            throw new IllegalArgumentException("Measured Value cannot be null.");
        }

        this.resultId = resultId;
        this.batch = batch;
        this.qualityTest = qualityTest;
        this.measuredValue = measuredValue;
        this.passed = qualityTest.isWithinAcceptableLimit(measuredValue);
    }

    public String getResultId() {
        return resultId;
    }

    public ProductionBatch getBatch() {
        return batch;
    }

    public QualityTest getQualityTest() {
        return qualityTest;
    }

    public BigDecimal getMeasuredValue() {
        return measuredValue;
    }

    public boolean isPassed() {
        return passed;
    }
}
