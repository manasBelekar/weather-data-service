package com.tenera.integration;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.tenera.model.HistoricalResponse;
import com.tenera.model.WeatherResponse;
import com.tenera.task.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class WeatherControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	HttpHeaders headers = new HttpHeaders();

	@Sql({ "data.sql" })
	@Test
	public void testGetCurrentWeather() throws Exception {
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<WeatherResponse> response = restTemplate.exchange(createURLWithPort("/current", "Berlin"),
				HttpMethod.GET, entity, WeatherResponse.class);

		HttpStatus actual = response.getStatusCode();

		assertEquals(actual, HttpStatus.OK);
	}

	@Sql({ "data.sql" })
	@Test
	public void testGetHistorical() throws Exception {
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<HistoricalResponse> response = restTemplate.exchange(createURLWithPort("/history", "Berlin"),
				HttpMethod.GET, entity, HistoricalResponse.class);

		HttpStatus actual = response.getStatusCode();

		assertEquals(actual, HttpStatus.OK);
		assertEquals(16.5, response.getBody().getAvgTtemp(), 0);
	}

	private URI createURLWithPort(String apiName, String location) throws URISyntaxException {
		return new URI("http://localhost:" + port + "/api/v1/weather" + apiName + "?location=" + location);
	}

}
