package com.sesamecare.appointments;

import com.sesamecare.appointments.model.SesameValidatedAppointments;
import com.sesamecare.appointments.service.SesameAppointmentsService;
import com.sesamecare.appointments.util.SesameAppointmentTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.stream.Collectors;


/*Testing componenets integrated and especially the controller for appointments*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AppointmentsApplicationTests {

    @Autowired
    private RestTemplate restTemplateClient;

    @MockBean
    private SesameAppointmentsService sesameAppointmentsService;

    @BeforeEach
    public void setUp() {
        var sesameAppointments = SesameAppointmentTestData.prepareAppointmentsData(true);
        Mockito.when(sesameAppointmentsService.findAllProcessedAppointments())
                .thenReturn(sesameAppointments);
    }

    @Test
    public void when_ProviderAppointmentsAreQueried_ThenAPIReturnsAppointmentsAndFlaggedCorruptions() throws Exception {
        var sesameValidatedAppointments = restTemplateClient.getForObject("/appointments", SesameValidatedAppointments.class);

        Assertions.assertEquals(sesameValidatedAppointments.getCorruptedAppointments().size(), 3);

        var allAppointmentsByLocation = sesameValidatedAppointments.getAppointmentsByDoctor().stream()
                .flatMap(sesameDoctorAppointments -> sesameDoctorAppointments.getAppointmentsByLocation().stream())
                .collect(Collectors.toList());

        Assertions.assertEquals(allAppointmentsByLocation.size(), 6);

        var allAppointments = allAppointmentsByLocation.stream()
                .flatMap(allAppointmentsByLoc -> allAppointmentsByLoc.getAppointments().stream())
                .collect(Collectors.toList());

        Assertions.assertEquals(allAppointments.size(), 12);

        var appointmentIdForIncorrectPrice = allAppointments
                .stream()
                .filter(sesameAppointment -> sesameAppointment.getService().getPrice().equals("-1"))
                .map(sesameAppointment -> sesameAppointment.getAppointmentId())
                .findFirst()
                .get();

        var corruptedAppointment = sesameValidatedAppointments
                .getCorruptedAppointments()
                .stream()
                .flatMap(sesameCorruptedDoctorAppointments -> sesameCorruptedDoctorAppointments.getCorruptedAppointments().stream())
                .filter(corrupted -> corrupted.getAppointmentId().equals(appointmentIdForIncorrectPrice))
                .findFirst()
                .get();

        Assertions.assertEquals(corruptedAppointment.getAppointmentId(), appointmentIdForIncorrectPrice);

        Assertions.assertTrue(corruptedAppointment.getErrors().contains("Appointment with invalid price, check price: -1"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public RestTemplate restTemplateClient() {
            DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory("http://localhost:8080");
            RestTemplateBuilder builder = new RestTemplateBuilder();
            builder.uriTemplateHandler(uriBuilderFactory);
            var restTemplate = builder.build();
            restTemplate.setUriTemplateHandler(uriBuilderFactory);
            return restTemplate;
        }

    }

}
