package com.tenera.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class WeatherDataBean {
	private int cityId;
	private String cityName;
	private double temp;
	private int pressure;
	private int weatherId;
	private String description;
	
//	@JsonIgnore
//	private Coord coord;
//	private Weather weather;
//	private String base;
//	private Main main;
//	private int timezone;
//	private int id;
//	private String name;
//	private int cod;
//}
//
//@Getter @Setter @AllArgsConstructor
//class Main{
//	private double temp;
//	private int pressure;
//	@JsonIgnore
//	private double feels_like;
//	@JsonIgnore
//	private double temp_min;
//	@JsonIgnore
//	private double temp_max;
//	@JsonIgnore
//	private int humidity;
//}
//
//@Getter @Setter @AllArgsConstructor
//class Weather{
//	private int id;
//	private String main;
//	private String description;
//	@JsonIgnore
//	private String icon;
}

//@Getter @Setter @AllArgsConstructor
//class Coord{
//	private double lat;
//	private double lon;
//}
