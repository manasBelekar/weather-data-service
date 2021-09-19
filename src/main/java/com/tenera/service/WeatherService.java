package com.tenera.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manas.mapper.WeatherBeanToHIstoricalResponseMapper;
import com.manas.mapper.WeatherBeanToWeatherResponseMapper;
import com.tenera.common.WeatherUtility;
import com.tenera.dao.WeatherDao;
import com.tenera.model.HistoricalResponse;
import com.tenera.model.WeatherDataBean;
import com.tenera.model.WeatherResponse;

@Service
public class WeatherService {
	private static Logger logger = LoggerFactory.getLogger(WeatherService.class);

	@Autowired
	WeatherDao weatherDao;
	
	@Autowired
	WeatherUtility utility;

	@Autowired
	WeatherBeanToWeatherResponseMapper weatherResponseMapper;

	@Autowired
	WeatherBeanToHIstoricalResponseMapper hIstoricalResponseMapper;

	/**
	 * @param cityName
	 * @return
	 */
	public WeatherResponse getCurrentWeatherData(String cityName) {
		logger.info("Entering getCurrentWeatherData for cityNmae: " + cityName);
		cityName = WeatherUtility.sanitizeCityName(cityName);
		// make API call to get weatherData
		WeatherDataBean weatherBean = utility.getCurrentWeather(cityName);

		// persist the data in store
		WeatherDataBean weatherObj = weatherDao.insertData(weatherBean);

		// prepare and return the response
		logger.info("Exiting getCurrentWeatherData");
		return weatherResponseMapper.mapSourceToTargetObject(weatherObj, null);
	}

	/**
	 * @param cityName
	 * @return
	 */
	public HistoricalResponse getHistoricalWeatherData(String cityName) {
		logger.info("Entering getHistoricalWeatherData for cityNmae: " + cityName);
		cityName = WeatherUtility.sanitizeCityName(cityName);
		// query the store for previously queried data
		List<WeatherDataBean> weatherDataList = weatherDao.getHistoricalData(cityName);

		// calculate for transformations

		// return the response
		logger.info("Exiting getHistoricalWeatherData");
		return hIstoricalResponseMapper.mapSourceToTargetObject(weatherDataList, null);
	}


}
