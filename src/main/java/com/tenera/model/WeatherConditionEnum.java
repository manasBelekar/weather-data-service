package com.tenera.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum WeatherConditionEnum {
	UNKNOWN(-1), THUNDERSTORM(200), DRIZZLE(300), RAIN(500), SNOW(600), ATMOSPHERE(700), CLEAR(800), CLOUDS(801);

	private int statusCode;

	public static WeatherConditionEnum parseValue(int value) {
		for (WeatherConditionEnum e : WeatherConditionEnum.values()) {
			if (e.getStatusCode() == value) {
				return e;
			}
		}
		return UNKNOWN;
	}
}
