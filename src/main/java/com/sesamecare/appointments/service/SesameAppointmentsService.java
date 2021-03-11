package com.sesamecare.appointments.service;

import com.sesamecare.appointments.model.SesameDoctorAppointments;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SesameAppointmentsService {
    List<SesameDoctorAppointments> findAllProcessedAppointments();
}
