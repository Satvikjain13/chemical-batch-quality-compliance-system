package com.chemicalmanagement.domain;

public class Chemical {
    private String chemicalId;
    private String name;
    private String unit;
    private int shelfLifeDays;

    public Chemical(String chemicalId,String name,String unit,int shelfLifeDays){

        if(chemicalId == null || chemicalId.isBlank()){
            throw new IllegalArgumentException("Chemical ID cannot be empty.");
        }

        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Chemical name cannot be empty or blank.");
        }

        if(unit == null || unit.isBlank()){
            throw new IllegalArgumentException("Unit cannot be empty or blank");
        }

        if(shelfLifeDays <= 0){
            throw new IllegalArgumentException("Shelf life must be greater than zero.");
        }

        this.chemicalId = chemicalId;
        this.name = name;
        this.unit = unit;
        this.shelfLifeDays = shelfLifeDays;
    }

    public String getChemicalId(){
        return chemicalId;
    }

    public String getName(){
        return name;
    }

    public String getUnit(){
        return unit;
    }

    public int getShelfLifeDays(){
        return shelfLifeDays;
    }
}
