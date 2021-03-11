package com.sesamecare.appointments.service.impl;

import com.sesamecare.appointments.model.SesameCorruptedAppointment;
import com.sesamecare.appointments.model.SesameCorruptedDoctorAppointments;
import com.sesamecare.appointments.model.SesameDoctorAppointments;
import com.sesamecare.appointments.service.SesameAppointmentsValidationService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DefaultSesameAppointmentsValidationServiceImpl implements SesameAppointmentsValidationService {

    @Override
    public List<SesameCorruptedDoctorAppointments> validateAppointments(List<SesameDoctorAppointments> appointments) {
        return appointments
                .stream()
                .map(this::findAppointmentCorruption)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<SesameCorruptedDoctorAppointments> findAppointmentCorruption(SesameDoctorAppointments doctorAppointments) {
        var mapAppointmentIdsToErrors = new HashMap<String, List<String>>();
        //TODO perhaps do this non-null validation in an automated way with reflection?
        validateLocationOfAppointments(doctorAppointments, mapAppointmentIdsToErrors);
        validatePriceOfAppointments(doctorAppointments, mapAppointmentIdsToErrors);

        if (mapAppointmentIdsToErrors.isEmpty()) {
            log.debug("No errors identified for appointments");
            return Optional.empty();
        }
        log.debug("Not all appointments are valid. Found {} invalid appointments", mapAppointmentIdsToErrors.entrySet().size());
        List<SesameCorruptedAppointment> corruptedAppointments = fromMapOfIdsErrorsToListOdCorruptedAppointments(mapAppointmentIdsToErrors);
        var sesameCorruptedDoctorAppointment = new SesameCorruptedDoctorAppointments();
        sesameCorruptedDoctorAppointment.setFirstName(doctorAppointments.getFirstName());
        sesameCorruptedDoctorAppointment.setLastName(doctorAppointments.getLastName());
        sesameCorruptedDoctorAppointment.setCorruptedAppointments(corruptedAppointments);
        return Optional.of(sesameCorruptedDoctorAppointment);
    }

    private void validatePriceOfAppointments(SesameDoctorAppointments doctorAppointments, HashMap<String, List<String>> mapAppointmentIdsToErrors) {
        doctorAppointments.getAppointmentsByLocation()
                .stream()
                .flatMap(appointmentsByLocations -> appointmentsByLocations.getAppointments().stream())
                .forEach(sesameAppointment -> {
                    String price = sesameAppointment.getService().getPrice();
                    if (isNull(price) || Double.parseDouble(price) <= 0) {
                        var errors = mapAppointmentIdsToErrors.getOrDefault(sesameAppointment.getAppointmentId(), new ArrayList<>());
                        errors.add("Appointment with invalid price, check price: " + price);
                        mapAppointmentIdsToErrors.put(sesameAppointment.getAppointmentId(), errors);
                    }
                });
    }

    private void validateLocationOfAppointments(SesameDoctorAppointments doctorAppointments, Map<String, List<String>> mapAppointmentIdsToErrors) {
        doctorAppointments.getAppointmentsByLocation()
                .forEach(appointmentsByLocations -> {
                    if (isNull(appointmentsByLocations.getLocationName())) {
                        //this is a group of appointments with no location
                        appointmentsByLocations
                                .getAppointments()
                                .forEach(sesameAppointment -> {
                                    var errors = mapAppointmentIdsToErrors.getOrDefault(sesameAppointment.getAppointmentId(), new ArrayList<>());
                                    errors.add("Missing Location");
                                    mapAppointmentIdsToErrors.put(sesameAppointment.getAppointmentId(), errors);
                                });
                    }
                });
    }

    private List<SesameCorruptedAppointment> fromMapOfIdsErrorsToListOdCorruptedAppointments(HashMap<String, List<String>> mapAppointmentIdsToErrors) {
        return mapAppointmentIdsToErrors.entrySet()
                .stream()
                .map(entry -> {
                    var corruptedAppointment = new SesameCorruptedAppointment();
                    corruptedAppointment.setAppointmentId(entry.getKey());
                    corruptedAppointment.setErrors(entry.getValue());
                    return corruptedAppointment;
                }).collect(Collectors.toList());
    }

    private boolean isNull(String value) {
        return value == null || value.isBlank();
    }


}
