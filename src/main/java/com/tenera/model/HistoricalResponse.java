package com.tenera.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @AllArgsConstructor
public class HistoricalResponse {
	private int avg_temp;
	
	private int avg_pressure;
	
	private List<WeatherResponse> history;
}
