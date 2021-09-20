package com.tenera.common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tenera.model.WeatherConditionEnum;
import com.tenera.model.WeatherDataBean;

@Component
public class WeatherUtility {

	@Value("${openweather.api.baseurl}")
	private String apiBaseUrl;

	@Value("${openweather.api.key}")
	private String apiKey;

	@Value("${openweather.api.unit}")
	private String apiUnit;

	@Autowired
	RestTemplate restTemplate;

	/**
	 * @param cityName
	 * @return response by calling REST API exposed by openWaether platform
	 */
	public WeatherDataBean getCurrentWeather(String cityName) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl).queryParam("q", cityName)
				.queryParam("appid", apiKey).queryParam("units", apiUnit);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		HttpEntity<String> response;
		response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
		return parseResponse(response.getBody());
	}

	/**
	 * @param response
	 * @return weatherdataBean from response
	 */
	private WeatherDataBean parseResponse(String response) {
		// creating an object of JSONObject and casting the object into JSONObject type
		JSONObject jsonObject = (JSONObject) JSONValue.parse(response);
		boolean umbrella = false;

		// getting values form JSONObject and casting values into corresponding types
		long statusCode = (long) jsonObject.get("cod");

		if (statusCode == HttpStatus.NOT_FOUND.value()) {
			throw new RuntimeException("City Not Found");
		}

		long cityId = (long) jsonObject.get("id");
		String name = (String) jsonObject.get("name");
		long timestamp = (long) jsonObject.get("dt");

		JSONObject mainNode = (JSONObject) jsonObject.get("main");
		double temp = (double) mainNode.get("temp");
		long pressure = (long) mainNode.get("pressure");

		JSONObject sysNode = (JSONObject) jsonObject.get("sys");
		String countryCode = (String) sysNode.get("country");

		JSONArray weather = (JSONArray) jsonObject.get("weather");
		long weatherId = (long) ((JSONObject) weather.get(0)).get("id");
		String description = (String) ((JSONObject) weather.get(0)).get("description");

		// if weather is Thunderstorm, Drizzle or Rain, umbrella value is true
		int weatherGrp = (int) (weatherId / 100) * 100;
		if (weatherGrp == WeatherConditionEnum.THUNDERSTORM.getStatusCode()
				|| weatherId == WeatherConditionEnum.DRIZZLE.getStatusCode()
				|| weatherId == WeatherConditionEnum.RAIN.getStatusCode()) {
			umbrella = true;
		}

		if (statusCode == HttpStatus.OK.value()) {
			return new WeatherDataBean(0, cityId, name, countryCode, temp, umbrella, pressure, weatherId, timestamp,
					description);
		}
		return null;
	}

	/**
	 * @param cityName
	 * @return only 'cityName' and 'cityName,countryCode' are valid cases, for rest
	 *         all cases 'cityName,countryCode' case will be considered.
	 */
	public static String sanitizeCityName(String cityName) {
		String[] cityDetails = cityName.split(",");
		if (cityDetails.length != 1) {
			String city = cityDetails[0];
			String countryCode = cityDetails[cityDetails.length - 1].toLowerCase();
			return city + "," + countryCode;
		}
		return cityName;
	}

}
