package com.tenera.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.tenera.model.WeatherDataBean;

public class WeatherUtilityTest {

	@InjectMocks
	private WeatherUtility utility;

	@Mock
	RestTemplate restTemplate;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(utility, "apiBaseUrl", "https://api.openweathermap.org/data/2.5/weather");
		ReflectionTestUtils.setField(utility, "apiKey", "1a2f50e0f20224a456b20e4e8fd979dc");
		ReflectionTestUtils.setField(utility, "apiUnit", "metric");
	}

	@Test
	public void testGetCurrentWeather() {
		String sampleResponse = "{\"coord\":{\"lon\":-0.1257,\"lat\":51.5085},\"weather\":[{\"id\":804,\"main\":\"Clouds\","
				+ "\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"base\":\"stations\",\"main\":{\"temp\":285.28,\"feels_like\":284.87,"
				+ "\"temp_min\":283.07,\"temp_max\":287.14,\"pressure\":1020,\"humidity\":89},\"visibility\":10000,\"wind\":{\"speed\":3.09,\"deg\":300}"
				+ ",\"clouds\":{\"all\":100},\"dt\":1632115746,\"sys\":{\"type\":2,\"id\":2019646,\"country\":\"GB\",\"sunrise\":1632116611,"
				+ "\"sunset\":1632161065},\"timezone\":3600,\"id\":2643743,\"name\":\"London\",\"cod\":200}";
		ResponseEntity<String> entity = new ResponseEntity<String>(sampleResponse, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.anyString(),
				Mockito.<HttpMethod> any(),
				Mockito.<HttpEntity<?>> any(),
				Mockito.<Class<String>> any())).thenReturn(entity);
		WeatherDataBean result = utility.getCurrentWeather("Berlin");
	    Mockito.verify(restTemplate, Mockito.times(1))
        .exchange(Mockito.anyString(),
                        Mockito.<HttpMethod> any(),
                        Mockito.<HttpEntity<?>> any(),
                        Mockito.<Class<String>> any());
	    Assert.assertNotNull(result);
	}
	
	@Test
    public void testSanitizeCityName(){
    	String result = WeatherUtility.sanitizeCityName("Berlin,DE");
        Assert.assertNotNull(result);
        Assert.assertEquals(result, "Berlin,de");
    }

}
