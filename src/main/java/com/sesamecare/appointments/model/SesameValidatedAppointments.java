package com.sesamecare.appointments.model;

import lombok.Data;

import java.util.List;

@Data
public class SesameValidatedAppointments {
    private List<SesameDoctorAppointments> appointmentsByDoctor;
    private List<SesameCorruptedDoctorAppointments> corruptedAppointments;
}
