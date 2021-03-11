package com.sesamecare.appointments.model.provider;

import lombok.Data;

/**
 * Represents the Appointment entity from the provider Sesame is consuming from
 * along with all other model classes in the same package
 */
@Data
public class ProviderAppointment {

    private Doctor doctor;
    private Integer durationInMinutes;
    private String time;
    private Service service;
    private Location location;
    private String id;

}
