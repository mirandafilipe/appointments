package com.sesamecare.appointments.service;

import com.sesamecare.appointments.model.SesameCorruptedDoctorAppointments;
import com.sesamecare.appointments.model.SesameDoctorAppointments;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SesameAppointmentsValidationService {
    public List<SesameCorruptedDoctorAppointments> validateAppointments(List<SesameDoctorAppointments> appointments);
}
