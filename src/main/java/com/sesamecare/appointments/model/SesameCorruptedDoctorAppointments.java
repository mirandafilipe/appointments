package com.sesamecare.appointments.model;

import lombok.Data;

import java.util.List;

@Data
public class SesameCorruptedDoctorAppointments {
    private String firstName;
    private String lastName;
    private List<SesameCorruptedAppointment> corruptedAppointments;

}
