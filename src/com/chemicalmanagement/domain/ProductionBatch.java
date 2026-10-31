package com.chemicalmanagement.domain;

import java.time.LocalDate;

public class ProductionBatch {
    private final String batchNumber;
    private final Chemical chemical;
    private final LocalDate manufacturingDate;
    private double quantity;
    private QualityStatus qualityStatus;
    private BatchStatus batchStatus;

    //constructors
    public ProductionBatch(String batchNumber,Chemical chemical,LocalDate manufacturingDate,double quantity){
        if(batchNumber == null || batchNumber.isBlank()){
            throw new IllegalArgumentException("Batch number cannot be empty or blank.");
        }
        if(chemical == null){
            throw new IllegalArgumentException("Chemical cannot be null.");
        }
        if(manufacturingDate == null || manufacturingDate.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("\"Manufacturing date cannot be null or in the future.");
        }
        if(quantity <= 0 ||!Double.isFinite(quantity)){
            throw new IllegalArgumentException("Quantity must be a finite number greater than zero.");
        }

        this.batchNumber=batchNumber;
        this.chemical=chemical;
        this.manufacturingDate=manufacturingDate;
        this.quantity=quantity;
        this.qualityStatus=QualityStatus.PENDING;
        this.batchStatus=BatchStatus.IN_PRODUCTION;
    }

    //getters
    public String getBatchNumber(){
        return batchNumber;
    }

    public Chemical getChemical(){
        return chemical;
    }

    public LocalDate getManufacturingDate(){
        return manufacturingDate;
    }

    public double getQuantity(){
        return quantity;
    }

    public QualityStatus getQualityStatus(){
        return qualityStatus;
    }

    public BatchStatus getBatchStatus(){
        return batchStatus;
    }

    public LocalDate getExpiryDate(){
        return manufacturingDate.plusDays(chemical.getShelfLifeDays());
    }

    //setters
    public void setQuantity(double quantity){
        if(quantity < 0 ||!Double.isFinite(quantity)){
            throw new IllegalArgumentException("Quantity must be a finite number greater than zero.");
        }
        this.quantity = quantity;
    }

    public void completeQualityEvaluation(QualityStatus evaluationOutcome){
        if (evaluationOutcome == null) {
            throw new IllegalArgumentException(
                    "Evaluation outcome cannot be null"
            );
        }

        if(this.batchStatus == BatchStatus.QUARANTINED){
            throw new IllegalStateException("Quarantine Batch cannot be Evaluate Again");
        }

        if(this.batchStatus == BatchStatus.APPROVED){
            throw new IllegalStateException("Approved Batch cannot be Evaluated Again");
        }

        if(evaluationOutcome == QualityStatus.FAILED){
            this.qualityStatus = QualityStatus.FAILED;
            this.batchStatus = BatchStatus.QUARANTINED;
        }else if(evaluationOutcome == QualityStatus.PENDING){
            this.qualityStatus = QualityStatus.PENDING;
            this.batchStatus = BatchStatus.QUALITY_PENDING;
        }else {
            this.qualityStatus = QualityStatus.PASSED;
            this.batchStatus = BatchStatus.APPROVED;
        }


    }

}
