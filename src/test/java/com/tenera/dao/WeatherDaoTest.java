package com.tenera.dao;

import static com.tenera.dao.WeatherDao.GENERIC_DB_OPERATION_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.tenera.mapper.WeatherDataRowMapper;
import com.tenera.model.WeatherDataBean;

public class WeatherDaoTest {
	
    @InjectMocks
    private WeatherDao weatherDao;
	
	@Mock
	private JdbcTemplate jdbcTemplate;

	private WeatherDataBean weatherDataBean;
	
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        weatherDataBean = new WeatherDataBean(1000, 2950159, "Berlin", "DE", 20.5, false, 1008, 801, 1632060361,
				"few clouds");
    }
    
	@Test
	public void testInsertData() {
		Mockito.when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(PreparedStatementSetter.class)))
				.thenReturn(1);
		weatherDataBean = weatherDao.insertData(weatherDataBean);
		Assert.assertNotNull(weatherDataBean);
	}

	@SuppressWarnings("serial")
	@Test
	public void testInsrtDataWithErrorInserting() {
		Mockito.when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(PreparedStatementSetter.class)))
				.thenThrow(new DataAccessException("Exception while inserting") {
				});
		Exception exp = Assert.assertThrows(RuntimeException.class,
				() -> weatherDao.insertData(weatherDataBean));
		Assert.assertEquals(GENERIC_DB_OPERATION_ERROR, exp.getMessage());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testInsertDataWithConflictReading() {
		// weather data with same city id and timestamp will not be re-recorded, combination should
		// be unique. latest record will be returned from DB
		List<WeatherDataBean> records = new ArrayList<>();
		records.add(weatherDataBean);
		Mockito.when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(PreparedStatementSetter.class)))
				.thenThrow(new DataIntegrityViolationException("Unique contraint failed while inserting") {
				});
		Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(PreparedStatementSetter.class),
				Mockito.any(WeatherDataRowMapper.class))).thenReturn(records);
		Assert.assertNotNull(weatherDao.insertData(weatherDataBean));
	}

	@Test
	public void testGetHistoricalData() {
		List<WeatherDataBean> records = new ArrayList<>();
		records.add(weatherDataBean);
		Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(PreparedStatementSetter.class),
				Mockito.any(WeatherDataRowMapper.class))).thenReturn(records);
		List<WeatherDataBean> result = weatherDao.getHistoricalData("Berlin");
		Assert.assertNotNull(result);
	}

	@SuppressWarnings("serial")
	@Test
	public void testGetHistoricalDataFailed() {
		Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(PreparedStatementSetter.class),
				Mockito.any(WeatherDataRowMapper.class))).thenThrow(new DataAccessException("Exception while inserting") {
				});
		Exception exp = Assert.assertThrows(RuntimeException.class, () -> weatherDao.getHistoricalData("Berlin"));
		Assert.assertEquals(GENERIC_DB_OPERATION_ERROR, exp.getMessage());
	}

}
