package com.tenera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenera.exception.InvalidCityException;
import com.tenera.model.HistoricalResponse;
import com.tenera.model.WeatherResponse;
import com.tenera.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/weather")
@Tag(name = "Weather", description = "Weather Data Controller")
public class WeatherController {

	private static final String JSON = "application/json";
	private static final String WEATHER_TAG = "weather";

	@Autowired
	WeatherService service;

	@Operation(summary = "Get current weather statistics", tags = WEATHER_TAG)
	@GetMapping(value = "/current", produces = JSON)
	public ResponseEntity<WeatherResponse> getCurrentWeather(@PathVariable("location") String cityName) {
		if(cityName==null || cityName.isEmpty())
			throw new InvalidCityException("Not a valid city");
		WeatherResponse response = service.getCurrentWeather(cityName);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Operation(summary = "Get historical weather statistics", tags = WEATHER_TAG)
	@GetMapping(value = "/history", produces = JSON)
	public ResponseEntity<HistoricalResponse> getHistoricalWeatherData(@PathVariable("location") String cityName) {
		HistoricalResponse response = service.getHistoricalWeatherData(cityName);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
