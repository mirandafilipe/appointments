package com.sesamecare.appointments.model;

import lombok.Data;

import java.util.List;

@Data
public class SesameDoctorAppointments {

    private String firstName;
    private String lastName;
    private List<SesameAppointmentsByLocations> appointmentsByLocation;


}
