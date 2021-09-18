package com.tenera.service;

import java.util.List;

import org.json.simple.JSONArray;
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

import com.manas.mapper.WeatherBeanToHIstoricalResponseMapper;
import com.manas.mapper.WeatherBeanToWeatherResponseMapper;
import com.tenera.dao.WeatherDao;
import com.tenera.model.HistoricalResponse;
import com.tenera.model.WeatherConditionEnum;
import com.tenera.model.WeatherDataBean;
import com.tenera.model.WeatherResponse;

@Service
public class WeatherService {
	private static Logger logger = LoggerFactory.getLogger(WeatherService.class);

	@Value("${openweather.api.baseurl}")
	private String apiBaseUrl;

	@Value("${openweather.api.key}")
	private String apiKey;
	
	@Value("${openweather.api.unit}")
	private String apiUnit;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	WeatherDao weatherDao;
	
	@Autowired
	WeatherBeanToWeatherResponseMapper weatherResponseMapper;
	
	@Autowired
	WeatherBeanToHIstoricalResponseMapper hIstoricalResponseMapper;

	public WeatherResponse getCurrentWeatherData(String cityName) {
		logger.info("Entering getCurrentWeatherData for cityNmae: " + cityName);
		// make API call to get weatherData
		WeatherDataBean weatherBean = getCurrentWeather(cityName);

		// persist the data in store
		WeatherDataBean weatherObj = weatherDao.insertData(weatherBean);

		// prepare and return the response
		logger.info("Exiting getCurrentWeatherData");
		return weatherResponseMapper.mapSourceToTargetObject(weatherObj, null);
	}

	public HistoricalResponse getHistoricalWeatherData(String cityName) {
		logger.info("Entering getHistoricalWeatherData for cityNmae: " + cityName);
		// query the store for previously queried data
		List<WeatherDataBean> weatherDataList = weatherDao.getHistoricalData(cityName);

		// calculate for transformations

		// return the response
		logger.info("Exiting getHistoricalWeatherData");
		return hIstoricalResponseMapper.mapSourceToTargetObject(weatherDataList, null);
	}

	private WeatherDataBean getCurrentWeather(String cityName) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl).queryParam("q", cityName)
				.queryParam("appid", apiKey).queryParam("units", apiUnit);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);

		return parseResponse(response.getBody());
	}

	private WeatherDataBean parseResponse(String response) {
		// creating an object of JSONObject and casting the object into JSONObject type
		JSONObject jsonObject = (JSONObject) JSONValue.parse(response);
		boolean umbrella = false;

		// getting values form JSONObject and casting values into corresponding types
		long cityId = (long) jsonObject.get("id");
		String name = (String) jsonObject.get("name");
		long statusCode = (long) jsonObject.get("cod");
		long timestamp = (long) jsonObject.get("dt");
		JSONObject mainNode = (JSONObject) jsonObject.get("main");
		double temp = (double) mainNode.get("temp");
		long pressure = (long) mainNode.get("pressure");
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
			return new WeatherDataBean(0,cityId, name, temp, umbrella, pressure, weatherId, timestamp, description);
		}
		return null;
	}
}
