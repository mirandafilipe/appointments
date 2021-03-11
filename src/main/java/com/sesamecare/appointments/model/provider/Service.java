package com.sesamecare.appointments.model.provider;

import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Data
public class Service {
    private String name;
    private BigDecimal price;
}
