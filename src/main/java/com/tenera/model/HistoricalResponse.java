package com.tenera.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class HistoricalResponse {
	@JsonProperty("avg_temp")
	private double avgTtemp;
	
	@JsonProperty("avg_pressure")
	private double avgPressure;
	
	private List<WeatherResponse> history;
}
