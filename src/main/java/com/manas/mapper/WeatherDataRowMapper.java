package com.manas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tenera.model.WeatherDataBean;

public class WeatherDataRowMapper implements RowMapper<WeatherDataBean> {

	@Override
	public WeatherDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		WeatherDataBean weather = new WeatherDataBean();
        weather.setId(rs.getInt(1));
        weather.setTemp(rs.getDouble(2));
        weather.setPressure(rs.getLong(3));
        weather.setUmbrella(rs.getBoolean(4));
        weather.setWeatherGroupId(rs.getInt(5));
        weather.setWeatherDescription(rs.getString(6));
        weather.setCityId(rs.getLong(7));
        weather.setCityName(rs.getString(8));
        weather.setTimestamp(rs.getLong(9));
        return weather;
	}


}
