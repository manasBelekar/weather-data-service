package com.tenera.controller;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tenera.service.WeatherService;

public class WeatherControllerTest {

	@InjectMocks
	WeatherController controller;

	@Mock
	WeatherService service;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

}
