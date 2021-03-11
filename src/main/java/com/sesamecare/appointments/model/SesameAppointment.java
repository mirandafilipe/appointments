package com.sesamecare.appointments.model;

import lombok.Data;

import java.time.Duration;
import java.time.ZonedDateTime;

@Data
public class SesameAppointment {

    private String appointmentId;
    private ZonedDateTime startDateTime;
    private Duration duration;
    private SesameService service;


}
