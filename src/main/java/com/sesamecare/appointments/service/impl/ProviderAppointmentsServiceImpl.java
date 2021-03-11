package com.sesamecare.appointments.service.impl;

import com.sesamecare.appointments.model.SesameAppointment;
import com.sesamecare.appointments.model.SesameAppointmentsByLocations;
import com.sesamecare.appointments.model.SesameDoctorAppointments;
import com.sesamecare.appointments.model.SesameService;
import com.sesamecare.appointments.model.provider.Doctor;
import com.sesamecare.appointments.model.provider.Location;
import com.sesamecare.appointments.model.provider.ProviderAppointment;
import com.sesamecare.appointments.service.SesameAppointmentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ProviderAppointmentsServiceImpl implements SesameAppointmentsService {

    @Autowired
    @Qualifier("webClientWithTimeout")
    private RestTemplate appointmentsWebClient;

    @Override
    public List<SesameDoctorAppointments> findAllProcessedAppointments() {
        var providerAppointments = appointmentsWebClient
                .getForObject("/sesame_programming_test_api", ProviderAppointment[].class);
        if (providerAppointments == null || providerAppointments.length == 0) {
            log.debug("No appointments were found from the 3rd party provider");
            return Collections.emptyList();
        }
        return convertToSesameAppointments(List.of(providerAppointments));
    }

    private List<SesameDoctorAppointments> convertToSesameAppointments(List<ProviderAppointment> providerAppointments) {
        var appointmentsGroupedByDoctorAndLocation = providerAppointments
                .stream()
                .collect(Collectors.groupingBy(ProviderAppointment::getDoctor,
                        Collectors.groupingBy(ProviderAppointment::getLocation)));

        return appointmentsGroupedByDoctorAndLocation
                .entrySet()
                .stream()
                .map(this::createSesameAppointment)
                .collect(Collectors.toList());
    }

    private SesameDoctorAppointments createSesameAppointment(Map.Entry<Doctor, Map<Location, List<ProviderAppointment>>> doctorMapEntry) {
        var doctor = doctorMapEntry.getKey();
        var appointmentProviderLocation = doctorMapEntry.getValue();
        var sesameDoctorAppointment = new SesameDoctorAppointments();

        var appointmentsByLocation = appointmentProviderLocation.entrySet()
                .stream()
                .map(this::createAppointmentsByLocation)
                .collect(Collectors.toList());

        sesameDoctorAppointment.setFirstName(doctor.getFirstName());
        sesameDoctorAppointment.setLastName(doctor.getLastName());
        sesameDoctorAppointment.setAppointmentsByLocation(appointmentsByLocation);
        return sesameDoctorAppointment;
    }

    private SesameAppointmentsByLocations createAppointmentsByLocation(Map.Entry<Location, List<ProviderAppointment>> locationToAppointments) {
        var sesameAppointmentLocation = new SesameAppointmentsByLocations();
        sesameAppointmentLocation.setLocationName(locationToAppointments.getKey().getName());
        var sesameAppointments = locationToAppointments.getValue() //
                .stream()
                .map(this::createSesameAppointment)
                .collect(Collectors.toList());
        sesameAppointmentLocation.setAppointments(sesameAppointments);
        return sesameAppointmentLocation;
    }

    private SesameAppointment createSesameAppointment(ProviderAppointment providerAppointment) {
        var sesameAppointment = new SesameAppointment();
        sesameAppointment.setAppointmentId(providerAppointment.getId());
        sesameAppointment.setDuration(Duration.ofMinutes(providerAppointment.getDurationInMinutes()));

        var providerService = providerAppointment.getService();
        var sesameService = new SesameService();
        sesameService.setServiceName(providerService.getName());
        sesameService.setPrice(String.valueOf(providerService.getPrice().doubleValue()));
        sesameAppointment.setService(sesameService);

        var time = providerAppointment.getTime();
        var timeZoneCode = providerAppointment.getLocation().getTimeZoneCode();
        var zoneId = ZoneId.of(timeZoneCode);
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var zonedDateTime = ZonedDateTime.parse(time, formatter.withZone(zoneId));
        sesameAppointment.setStartDateTime(zonedDateTime.withZoneSameInstant(ZoneOffset.UTC));
        return sesameAppointment;
    }

}
