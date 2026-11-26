package com.chemicalmanagement.domain;

import java.math.BigDecimal;

public class QualityTest {
    private final String testId;
    private final String testName;
    private final BigDecimal minimumAcceptableValue;
    private final BigDecimal maximumAcceptableValue;
    private final String unit;

    //Constructors
    public QualityTest(String testId, String testName, BigDecimal minimumAcceptableValue, BigDecimal maximumAcceptableValue, String unit){
        if(testId == null || testId.isBlank()){
            throw new IllegalArgumentException("Test ID cannot be empty or blank.");
        }

        if(testName == null || testName.isBlank()){
            throw new IllegalArgumentException("Test name cannot be empty or blank.");
        }

        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("Unit cannot be empty or blank."
            );
        }

        if(minimumAcceptableValue == null){
            throw new IllegalArgumentException("MinimumAcceptableValue cannot be null.");
        }

        if(maximumAcceptableValue == null){
            throw new IllegalArgumentException("MaximumAcceptableValue cannot be null.");
        }

        if (minimumAcceptableValue.compareTo(maximumAcceptableValue) > 0) {
            throw new IllegalArgumentException("Minimum acceptable value cannot exceed maximum.");
        }

        this.testId=testId;
        this.testName=testName;
        this.minimumAcceptableValue=minimumAcceptableValue;
        this.maximumAcceptableValue=maximumAcceptableValue;
        this.unit=unit;
    }

    public String getTestId(){
        return testId;
    }

    public String getTestName(){
        return testName;
    }

    public BigDecimal getMinimumAcceptableValue(){
        return minimumAcceptableValue;
    }

    public BigDecimal getMaximumAcceptableValue(){
        return maximumAcceptableValue;
    }

    public String getUnit(){
        return unit;
    }

    public boolean isWithinAcceptableLimit(BigDecimal measuredValue){
        if(measuredValue == null){
            throw new IllegalArgumentException("Measured Value cannot be null.");
        }
        return measuredValue.compareTo(minimumAcceptableValue) >=0  && measuredValue.compareTo(maximumAcceptableValue) <= 0 ;
    }
}
