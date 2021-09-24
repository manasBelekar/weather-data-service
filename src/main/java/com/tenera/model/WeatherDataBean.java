package com.tenera.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class WeatherDataBean {
	private int id;
	private long cityId;
	private String cityName;
	private String countryCode;
	private double temp;
	private boolean umbrella;
	private long pressure;
	private long weatherGroupId;
	private long timestamp;
	private String weatherDescription;
}
