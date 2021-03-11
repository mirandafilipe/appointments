package com.sesamecare.appointments.configurations;

import com.sesamecare.appointments.service.SesameAppointmentsService;
import com.sesamecare.appointments.service.SesameAppointmentsValidationService;
import com.sesamecare.appointments.service.impl.DefaultSesameAppointmentsValidationServiceImpl;
import com.sesamecare.appointments.service.impl.ProviderAppointmentsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

@Configuration
public class AppConfiguration {

    @Value("${provider.appointments.baseURI}")
    private String baseExAppsServiceURI;

    @Value("${provider.appointments.timeout}")
    private String clientTimeout;

    @Bean
    public RestTemplate webClientWithTimeout() {
        //having a timeout defined may be an interesting "safety net" to the service
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(baseExAppsServiceURI);
        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.uriTemplateHandler(uriBuilderFactory);
        builder.setConnectTimeout(Duration.ofMillis(Long.parseLong(clientTimeout)));
        var restTemplate = builder.build();
        restTemplate.setUriTemplateHandler(uriBuilderFactory);
        return restTemplate;
    }

    @Bean
    public SesameAppointmentsService providerAppointmentService() {
        return new ProviderAppointmentsServiceImpl();
    }

    @Bean
    public SesameAppointmentsValidationService sesameAppointmentsValidationService() {
        return new DefaultSesameAppointmentsValidationServiceImpl();
    }

}
