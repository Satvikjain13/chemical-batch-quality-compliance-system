package com.chemicalmanagement.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChemicalTest {
    @Test
    void shouldThrowExceptionWhenChemicalIdIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class,()
        -> {
            new Chemical(null,"Sulphuric Acid","Litres",365);
        });
    }

    @Test
    void shouldCreateChemicalWhenAllFieldsAreValid(){
        Chemical sulphuricAcid = new Chemical("CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365);
        Assertions.assertEquals("CHEM-001",sulphuricAcid.getChemicalId());
        Assertions.assertEquals("Sulphuric Acid",sulphuricAcid.getName());
        Assertions.assertEquals("Litres",sulphuricAcid.getUnit());
        Assertions.assertEquals(365,sulphuricAcid.getShelfLifeDays());
    }

    @Test
    void shouldThrowExceptionWhenChemicalIdIsEmpty(){
        Assertions.assertThrows(IllegalArgumentException.class, ()
        -> {
            new Chemical("","Sulphuric Acid","Litres",365);
        });
    }

    @Test
    void shouldThrowExceptionWhenChemicalIdIsBlank(){
        Assertions.assertThrows(IllegalArgumentException.class,()
        -> {
            new Chemical("   ","Sulphuric Acid","Litres",365);
        });
    }

    @Test
    void shouldThrowExceptionWhenChemicalNameIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class,()
        -> {
            new Chemical("CHEM-001",null,"Litres",365);
        });
    }

    @Test
    void shouldThrowExceptionWhenChemicalNameIsEmpty(){
        Assertions.assertThrows(IllegalArgumentException.class,()
         ->{
            new Chemical("CHEM-001","","Litres",365);
        });
    }

    @Test
    void shouldThrowExceptionWhenChemicalNameIsBlank(){
        Assertions.assertThrows(IllegalArgumentException.class,()
                ->{
            new Chemical("CHEM-001","    ","Litres",365);
        });
    }

    @Test
    void shouldThrowExceptionWhenUnitIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Chemical("CHEM-001", "Sulphuric Acid", null, 365);
        });
    }

    @Test
    void shouldThrowExceptionWhenUnitIsEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Chemical("CHEM-001", "Sulphuric Acid", "", 365);
        });
    }

    @Test
    void shouldThrowExceptionWhenUnitIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Chemical("CHEM-001", "Sulphuric Acid", "   ", 365);
        });
    }

    @Test
    void shouldThrowExceptionWhenShelfLifeIsZero(){
        Assertions.assertThrows(IllegalArgumentException.class,() ->{
            new Chemical("CHEM-001", "Sulphuric Acid", "Litres", 0);
        });
    }

    @Test
    void shouldThrowExceptionWhenShelfLifeIsNegative(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            new Chemical("CHEM-001", "Sulphuric Acid", "Litres", -5);
        });
    }
}
