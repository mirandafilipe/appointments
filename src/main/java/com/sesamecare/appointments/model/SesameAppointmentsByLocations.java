package com.sesamecare.appointments.model;

import lombok.Data;

import java.util.List;

@Data
public class SesameAppointmentsByLocations {

    private String locationName;
    private List<SesameAppointment> appointments;


}
