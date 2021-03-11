package com.sesamecare.appointments.model;

import lombok.Data;

import java.util.List;

@Data
public class SesameCorruptedAppointment {
    private String appointmentId;
    private List<String> errors;

}
