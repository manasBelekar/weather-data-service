package com.tenera.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.tenera.exception.InvalidCityException;
import com.tenera.model.HistoricalResponse;
import com.tenera.model.WeatherResponse;
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

	@Test
	public void testGetCurrentWeatherData() {
		Mockito.when(service.getCurrentWeatherData(Mockito.any(String.class))).thenReturn(new WeatherResponse());
		ResponseEntity<WeatherResponse> result = controller.getCurrentWeather("Berlin");
		Assert.assertEquals("OK", result.getStatusCode().name());
		Assert.assertEquals(200, result.getStatusCode().value());
	}

	@Test
	public void testGetSensorMetricsById() {
		Mockito.when(service.getHistoricalWeatherData(Mockito.any(String.class))).thenReturn(new HistoricalResponse());
		ResponseEntity<HistoricalResponse> result = controller.getHistoricalWeatherData("Berlin");
		Assert.assertEquals("OK", result.getStatusCode().name());
		Assert.assertEquals(200, result.getStatusCode().value());
	}
	
	@Test(expected = InvalidCityException.class)
	public void testGetCurrentWeatherWhenNullData() {
		controller.getCurrentWeather(null);
	}
	
	@Test(expected = InvalidCityException.class)
	public void testGetHistoricalWeatherDataWhenNoData() {
		controller.getHistoricalWeatherData("");
	}

}
