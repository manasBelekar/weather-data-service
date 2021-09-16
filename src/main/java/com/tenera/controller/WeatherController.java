package com.tenera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenera.model.HistoricalResponse;
import com.tenera.model.WeatherResponse;
import com.tenera.service.WeatherService;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

	private static final String JSON = "application/json";

	@Autowired
	WeatherService service;

	@GetMapping(value = "/current", produces = JSON)
	public ResponseEntity<WeatherResponse> getCurrentWeather(@PathVariable("location") String cityName) {
		if(cityName==null || cityName.isEmpty())
			throw new RuntimeException("Not a valid city");
		WeatherResponse response = service.getCurrentWeather(cityName);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/{uuid}/metrics", produces = JSON)
	public ResponseEntity<HistoricalResponse> getHistoricalWeatherData(@PathVariable("location") String cityName) {
		HistoricalResponse response = service.getHistoricalWeatherData(cityName);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
