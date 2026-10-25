package com.chemicalmanagement.domain;

import java.time.LocalDate;

public class ProductionBatch {
    private final String batchNumber;
    private final Chemical chemical;
    private final LocalDate manufacturingDate;
    private double quantity;
    private QualityStatus qualityStatus;

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
    }

    //getters
    public String getbatchNumber(){
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

    public void setQualityStatus(QualityStatus qualityStatus){
        if (qualityStatus == null) {
            throw new IllegalArgumentException("Quality status cannot be null.");}
        this.qualityStatus = qualityStatus;
    }
}
