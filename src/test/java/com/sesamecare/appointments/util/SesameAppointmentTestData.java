package com.sesamecare.appointments.util;

import com.sesamecare.appointments.model.SesameAppointment;
import com.sesamecare.appointments.model.SesameAppointmentsByLocations;
import com.sesamecare.appointments.model.SesameDoctorAppointments;
import com.sesamecare.appointments.model.SesameService;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SesameAppointmentTestData {

    //this class should only be a utility class with helper functions to create test data for appointments
    private SesameAppointmentTestData() {}

    /*When 'corrupted 'flagged true, some appointments will be corrupted, but not all*/
    public static List<SesameDoctorAppointments> prepareAppointmentsData(boolean someCorrupted) {
        var sesameDoctorAppointments1 = new SesameDoctorAppointments();

        sesameDoctorAppointments1.setFirstName("Filipe");
        sesameDoctorAppointments1.setLastName("Miranda");
        sesameDoctorAppointments1.setAppointmentsByLocation(prepareAppointmentByLocationData(someCorrupted));

        var sesameDoctorAppointments2 = new SesameDoctorAppointments();

        sesameDoctorAppointments2.setFirstName("Gabriel");
        sesameDoctorAppointments2.setLastName("Miranda");
        sesameDoctorAppointments2.setAppointmentsByLocation(prepareAppointmentByLocationData(someCorrupted));

        var sesameDoctorAppointments3 = new SesameDoctorAppointments();

        sesameDoctorAppointments3.setFirstName("Luise");
        sesameDoctorAppointments3.setLastName("Miranda");
        sesameDoctorAppointments3.setAppointmentsByLocation(prepareAppointmentByLocationData(someCorrupted));

        var sesameDoctorAppointmentsList = new ArrayList<SesameDoctorAppointments>();
        sesameDoctorAppointmentsList.add(sesameDoctorAppointments1);
        sesameDoctorAppointmentsList.add(sesameDoctorAppointments2);
        sesameDoctorAppointmentsList.add(sesameDoctorAppointments3);
        return sesameDoctorAppointmentsList;
    }

    private static List<SesameAppointmentsByLocations> prepareAppointmentByLocationData(boolean someCorrupted) {
        var sesameAppointmentsByLocations1 = new SesameAppointmentsByLocations();

        sesameAppointmentsByLocations1.setLocationName("Interesting Location 1");
        sesameAppointmentsByLocations1.setAppointments(prepareSesameAppointmentsData(someCorrupted));

        var maybeCorruptedLocationName = someCorrupted ? "" : "Interesting Location 2";
        var sesameAppointmentsByLocations2 = new SesameAppointmentsByLocations();

        sesameAppointmentsByLocations2.setLocationName(maybeCorruptedLocationName);
        sesameAppointmentsByLocations2.setAppointments(prepareSesameAppointmentsData(someCorrupted));

        var listOfAppointmentsByLocation = new ArrayList<SesameAppointmentsByLocations>();
        listOfAppointmentsByLocation.add(sesameAppointmentsByLocations1);
        listOfAppointmentsByLocation.add(sesameAppointmentsByLocations2);
        return listOfAppointmentsByLocation;
    }

    private static List<SesameAppointment> prepareSesameAppointmentsData(boolean someCorrupted) {
        var sesameService1 = new SesameService();
        sesameService1.setServiceName("Exam");
        sesameService1.setPrice("10");

        var sesameAppointment1 = new SesameAppointment();
        sesameAppointment1.setAppointmentId(UUID.randomUUID().toString());
        sesameAppointment1.setDuration(Duration.ofMinutes(30));
        sesameAppointment1.setStartDateTime(ZonedDateTime.now());
        sesameAppointment1.setService(sesameService1);


        var maybeCorruptedPrice = someCorrupted ? "-1" : "100";
        var sesameService2 = new SesameService();
        sesameService2.setServiceName("Special Exam");
        sesameService1.setPrice("-1");

        var sesameAppointment2 = new SesameAppointment();
        sesameAppointment2.setAppointmentId(UUID.randomUUID().toString());
        sesameAppointment2.setDuration(Duration.ofMinutes(30));
        sesameAppointment2.setStartDateTime(ZonedDateTime.now());
        sesameAppointment2.setService(sesameService2);


        var sesameAppointments = new ArrayList<SesameAppointment>();
        sesameAppointments.add(sesameAppointment1);
        sesameAppointments.add(sesameAppointment2);
        return sesameAppointments;
    }

}
