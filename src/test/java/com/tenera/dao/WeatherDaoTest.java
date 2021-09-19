package com.tenera.dao;

import static com.tenera.dao.WeatherDao.GENERIC_DB_OPERATION_ERROR;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

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

}
