package com.chemicalmanagement.domain;

public class QualityTest {
    private final String testId;
    private final String testName;
    private final double minimumAcceptableValue;
    private final double maximumAcceptableValue;
    private final String unit;

    //Constructors
    public QualityTest(String testId,String testName,double minimumAcceptableValue,double maximumAcceptableValue,String unit){
        if(testId == null || testId.isBlank()){
            throw new IllegalArgumentException("est ID cannot be empty or blank.");
        }

        if(testName == null || testName.isBlank()){
            throw new IllegalArgumentException("Test name cannot be empty or blank.");
        }

        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("Unit cannot be empty or blank."
            );
        }

        if (!Double.isFinite(minimumAcceptableValue) || !Double.isFinite(maximumAcceptableValue)) {
            throw new IllegalArgumentException("Acceptance limits must be finite numbers."
            );
        }

        if(minimumAcceptableValue > maximumAcceptableValue){
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

    public double getMinimumAcceptableValue(){
        return minimumAcceptableValue;
    }

    public double getMaximumAcceptableValue(){
        return maximumAcceptableValue;
    }

    public String getUnit(){
        return unit;
    }

    public boolean isWithinAcceptableLimit(double measuredValue){
        if(!Double.isFinite(measuredValue)){
            throw new IllegalArgumentException("Measured value must be a finite number.");
        }
        return measuredValue >= minimumAcceptableValue && measuredValue <= maximumAcceptableValue;
    }
}
