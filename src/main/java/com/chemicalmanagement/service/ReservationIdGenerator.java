package com.chemicalmanagement.service;

import java.util.UUID;

public class ReservationIdGenerator {
    public String generateId(){
        return "RES-"+ UUID.randomUUID();
    }
}
