package com.chemicalmanagement.domain;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChemicalQualityProfile {
    private final Chemical chemical;
    private final List<QualityTest> requiredTests;

    public ChemicalQualityProfile(Chemical chemical,List<QualityTest> requiredTests){
        if(chemical == null){
            throw new IllegalArgumentException("Chemical cannot be null.");
        }
        if(requiredTests == null || requiredTests.isEmpty()){
            throw new IllegalArgumentException("Required Test cannot be null or empty.");
        }
        for(QualityTest test:requiredTests){
            if(test == null){
                throw new IllegalArgumentException("Required tests cannot contain null elements.");
            }
        }
        Set<String> testIds = new HashSet<>();
        for(QualityTest test: requiredTests){
            if(!testIds.add(test.getTestId())){
                throw new IllegalArgumentException("Duplicate quality test ID found.");
            }
        }
        this.chemical = chemical;
        this.requiredTests = List.copyOf(requiredTests);
    }

    public Chemical getChemical() {
        return chemical;
    }

    public List<QualityTest> getRequiredTests() {
        return requiredTests;
    }
}
