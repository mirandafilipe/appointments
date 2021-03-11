package com.sesamecare.appointments.controllers;

import com.sesamecare.appointments.model.SesameValidatedAppointments;
import com.sesamecare.appointments.service.SesameAppointmentsService;
import com.sesamecare.appointments.service.SesameAppointmentsValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentsController {

    @Autowired
    private SesameAppointmentsService appointmentsService;

    @Autowired
    private SesameAppointmentsValidationService appointmentsValidationService;

    @GetMapping(path = "/appointments", produces = "application/json")
    public SesameValidatedAppointments appointments() {
        var allAppointments = appointmentsService.findAllProcessedAppointments();
        var corruptedAppointments = appointmentsValidationService.validateAppointments(allAppointments);
        var sesameValidatedAppointments =  new SesameValidatedAppointments();
        sesameValidatedAppointments.setAppointmentsByDoctor(allAppointments);
        sesameValidatedAppointments.setCorruptedAppointments(corruptedAppointments);
        return sesameValidatedAppointments;
    }

}
