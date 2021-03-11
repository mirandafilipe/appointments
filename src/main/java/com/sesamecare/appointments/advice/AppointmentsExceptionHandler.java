package com.sesamecare.appointments.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*Just a generic error handling advice to get started, more specific methods and error handling mechanism,
might be created upon necessity*/
@Slf4j
@RestControllerAdvice
public class AppointmentsExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleApplicationException(Exception ex) {
        log.error("Exception in application request ", ex);
        return new ResponseEntity<String>("Internal Error while processing the request, please check the logs of errors" , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
