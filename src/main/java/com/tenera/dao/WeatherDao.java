package com.tenera.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.tenera.mapper.WeatherDataRowMapper;
import com.tenera.model.WeatherDataBean;

@Repository
public class WeatherDao {

	private static Logger logger = LoggerFactory.getLogger(WeatherDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String WEATHER_DATA_TABLE = "public.weather";

	public static final String GENERIC_DB_OPERATION_ERROR = "Error while performing DB operation for Service";

	private static final String WEATHER_DATA_INSERT_QUERY = String.format(
			"INSERT INTO %s (temp, pressure, umbrella, weather_id, weather_desc, city_id, city_name, country_code, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
			WEATHER_DATA_TABLE);

	private static final String WEATHER_GET_HISTORICAL_DATA_BY_CITY_NAME_QUERY = String
			.format("SELECT * FROM %s Where city_name =? ", WEATHER_DATA_TABLE);

	private static final String ORDER_BY_DESC_CLAUSE = " order by id desc";

	private static final String COUNTRY_CODE_FILTER = " AND country_code =? ";

	/**
	 * @param weatherBean
	 * @return weatherBean after insert/ or latest record if duplicate
	 */
	public WeatherDataBean insertData(WeatherDataBean weatherBean) {
		try {
			jdbcTemplate.update(WEATHER_DATA_INSERT_QUERY, ps -> {
				ps.setObject(1, weatherBean.getTemp());
				ps.setObject(2, weatherBean.getPressure());
				ps.setObject(3, weatherBean.isUmbrella());
				ps.setObject(4, weatherBean.getWeatherGroupId());
				ps.setObject(5, weatherBean.getWeatherDescription());
				ps.setObject(6, weatherBean.getCityId());
				ps.setObject(7, weatherBean.getCityName());
				ps.setObject(8, weatherBean.getCountryCode());
				ps.setObject(9, weatherBean.getTimestamp());
			});
			logger.info("Record inserted");
			return weatherBean;
		} catch (DataIntegrityViolationException e) {
			logger.info("Weather record for city '" + weatherBean.getCityName() + "' with timestamp '"
					+ weatherBean.getTimestamp() + "' alredy exists. Hence skipping record insertion");
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(GENERIC_DB_OPERATION_ERROR, e);
		}
		// else return the latest record from DB
		return getHistoricalData(weatherBean.getCityName(), 1).get(0);
	}

	/**
	 * @param cityName
	 * @return list of latest 5 records from DB
	 */
	public List<WeatherDataBean> getHistoricalData(String cityName) {
		return getHistoricalData(cityName, 5);
	}

	/**
	 * @param cityName
	 * @param no       of records to be returned
	 * @return returns list of records for given city
	 */
	private List<WeatherDataBean> getHistoricalData(String cityName, int limit) {
		String[] cityDetails = cityName.split(",");
		String query = "";
		if (cityDetails.length > 1) {
			query = WEATHER_GET_HISTORICAL_DATA_BY_CITY_NAME_QUERY + COUNTRY_CODE_FILTER + ORDER_BY_DESC_CLAUSE
					+ " limit " + limit;
		} else {
			query = WEATHER_GET_HISTORICAL_DATA_BY_CITY_NAME_QUERY + ORDER_BY_DESC_CLAUSE + " limit " + limit;
		}

		try {
			return jdbcTemplate.query(query, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					if (cityDetails.length > 1) {
						ps.setObject(1, cityDetails[0]);
						ps.setObject(2, cityDetails[1].toUpperCase());
					} else {
						ps.setObject(1, cityName);
					}
				}

			}, new WeatherDataRowMapper());
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(GENERIC_DB_OPERATION_ERROR);
		}
	}

}