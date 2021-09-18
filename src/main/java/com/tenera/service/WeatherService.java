package com.tenera.service;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tenera.model.HistoricalResponse;
import com.tenera.model.WeatherDataBean;
import com.tenera.model.WeatherResponse;

@Service
public class WeatherService {
	private static Logger logger = LoggerFactory.getLogger(WeatherService.class);

	@Value("${openweather.api.baseurl}")
	private String apiBaseUrl;

	@Value("${openweather.api.key}")
	private String apiKey;

	@Autowired
	RestTemplate restTemplate;

	public WeatherResponse getCurrentWeatherData(String cityName) {
		logger.info("Entering getCurrentWeatherData for cityNmae: "+cityName);
		// make API call to get weatherData
		WeatherDataBean weatherBean = getCurrentWeather(cityName);
		
		// persist the data in store

		// return the response
		logger.info("Exiting getCurrentWeatherData");
		return null;
	}

	public HistoricalResponse getHistoricalWeatherData(String cityName) {
		logger.info("Entering getHistoricalWeatherData for cityNmae: "+cityName);
		// query the store for previously queried data

		// calculate for transformations

		// return the response
		logger.info("Exiting getHistoricalWeatherData");
		return null;
	}

	private WeatherDataBean getCurrentWeather(String cityName) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl).queryParam("q", cityName)
				.queryParam("appid", apiKey);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);

		return parseResponse(cityName, response);
	}

	private WeatherDataBean parseResponse(String cityName, HttpEntity<String> response) {
		// creating an object of JSONObject and casting the object into JSONObject type
		JSONObject jsonObject = (JSONObject) JSONValue.parse(response.getBody());
		
		// getting values form JSONObject and casting values into corresponding types
		int cityId = (int) jsonObject.get("id");
		String name = (String) jsonObject.get("name");
		int statusCode = (int) jsonObject.get("cod");
		JSONObject mainNode = (JSONObject) jsonObject.get("main");
		double temp = (double) mainNode.get("temp");
		int pressure = (int) mainNode.get("pressure");
		JSONObject weather = (JSONObject) jsonObject.get("weather");
		int weatherId = (int) weather.get("id");
		String description = (String) weather.get("description");

		if (statusCode == HttpStatus.OK.value()) {
			return new WeatherDataBean(cityId, name, temp, pressure, weatherId, description);
		}
		return null;
	}

}
