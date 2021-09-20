package com.tenera.mapper;

import org.springframework.stereotype.Component;

import com.tenera.model.WeatherDataBean;
import com.tenera.model.WeatherResponse;

@Component
public class WeatherBeanToWeatherResponseMapper implements ObjectMapper<WeatherDataBean, WeatherResponse> {

    /**
     * @param sourceObject
     * @param targetObject
     * @return
     */
	@Override
	public WeatherResponse mapSourceToTargetObject(WeatherDataBean sourceObject, WeatherResponse targetObject) {
        if (targetObject == null) {
            targetObject = new WeatherResponse();
        }
        targetObject.setTemp(sourceObject.getTemp());
        targetObject.setPressure(sourceObject.getPressure());
        targetObject.setUmbrella(sourceObject.isUmbrella());
        targetObject.setTimestamp(sourceObject.getTimestamp());
        return targetObject;
	}

}
