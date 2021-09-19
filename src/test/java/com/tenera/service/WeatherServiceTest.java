package com.tenera.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.manas.mapper.WeatherBeanToHIstoricalResponseMapper;
import com.manas.mapper.WeatherBeanToWeatherResponseMapper;
import com.tenera.common.WeatherUtility;
import com.tenera.dao.WeatherDao;
import com.tenera.model.HistoricalResponse;
import com.tenera.model.WeatherDataBean;
import com.tenera.model.WeatherResponse;

public class WeatherServiceTest {

	@InjectMocks
	WeatherService service;

	@Mock
	WeatherDao dao;
	
	@Mock
	WeatherUtility utility;

	@Spy
	RestTemplate restTemplate;
	
	@Spy
	WeatherBeanToWeatherResponseMapper weatherResponseMapper;

	@Spy
	WeatherBeanToHIstoricalResponseMapper hIstoricalResponseMapper;
	
	private WeatherDataBean weatherDataBean;

	@Before
	public void initMocks() {
		weatherDataBean = new WeatherDataBean(1000, 2950159, "Berlin", "DE", 20.5, false, 1008, 801, 1632060361,
				"few clouds");
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(utility, "apiBaseUrl", "https://api.openweathermap.org/data/2.5/weather");
		ReflectionTestUtils.setField(utility, "apiKey", "1a2f50e0f20224a456b20e4e8fd979dc");
		ReflectionTestUtils.setField(utility, "apiUnit", "metric");
		ReflectionTestUtils.setField(hIstoricalResponseMapper, "mapper", weatherResponseMapper);
	}

	@Test
    public void testGetCurrentWeatherData(){
    	Mockito.when(dao.insertData(Mockito.any(WeatherDataBean.class))).thenReturn(weatherDataBean);
    	Mockito.when(utility.getCurrentWeather(Mockito.anyString())).thenReturn(weatherDataBean);
    	WeatherResponse result = service.getCurrentWeatherData("Berlin");
        Assert.assertNotNull(result);
    }

	@Test
	public void testGetHistoricalData() {
		List<WeatherDataBean> weatherBeanList = new ArrayList<>();
		weatherBeanList.add(weatherDataBean);
		Mockito.when(dao.getHistoricalData(Mockito.any(String.class))).thenReturn(weatherBeanList);
		HistoricalResponse result = service.getHistoricalWeatherData("Berlin");
		Assert.assertNotNull(result);
	}
//
//	@Test
//	public void testGetAlertsById() {
//		Mockito.when(dao.getAlertsById(Mockito.any(UUID.class))).thenReturn(sensorList);
//		String result = service.getAlertsById(UUID.randomUUID());
//		Assert.assertNotNull(result);
//	}
//
//	@Test
//	public void testGetMetricsById() {
//		Mockito.when(dao.getMetricsById(Mockito.any(UUID.class))).thenReturn(sensorList);
//		MetricsResponse result = service.getMetricsById(UUID.randomUUID());
//		Assert.assertNotNull(result);
//		Assert.assertEquals(0, 2000, result.getAvgLast30Days());
//		Assert.assertEquals(0, 2100, result.getMaxLast30Days());
//	}
//
//	@Test(expected = NoRecordsFoundException.class)
//	public void testGetStausByIdWhenNoData() {
//		Mockito.when(dao.getStausById(Mockito.any(UUID.class))).thenReturn(null);
//		service.getStausById(UUID.randomUUID());
//	}

}
