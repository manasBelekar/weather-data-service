package com.tenera.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @ToString @AllArgsConstructor @NoArgsConstructor @Getter
public class WeatherResponse {
	private double temp;
	
	private long pressure;
	
	private boolean umbrella;
	
	@JsonIgnore
	private long timestamp;
}
