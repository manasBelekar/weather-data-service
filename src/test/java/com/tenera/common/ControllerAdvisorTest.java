package com.tenera.common;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestClientException;

public class ControllerAdvisorTest {
	
    @InjectMocks
    private ControllerAdvisor controllerAdvice;
	
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testHandleCityNotFoundException() {
    	RestClientException cityNotFoundException = new RestClientException("dummy");
        ResponseEntity<Object> response = controllerAdvice.handleCityNotFoundException(cityNotFoundException, new MockHttpServletRequest());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
