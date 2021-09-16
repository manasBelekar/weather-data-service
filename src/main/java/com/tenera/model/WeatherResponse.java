package com.tenera.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @AllArgsConstructor
public class WeatherResponse {
	private int temp;
	
	private int pressure;
	
	private boolean umbrella;
	
	@JsonIgnore
	private long timestamp;
}
