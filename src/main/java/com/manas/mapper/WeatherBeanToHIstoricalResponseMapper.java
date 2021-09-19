package com.manas.mapper;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tenera.model.HistoricalResponse;
import com.tenera.model.WeatherDataBean;
import com.tenera.model.WeatherResponse;

@Component
public class WeatherBeanToHIstoricalResponseMapper implements ObjectMapper<List<WeatherDataBean>, HistoricalResponse> {

	@Autowired
	WeatherBeanToWeatherResponseMapper mapper;

	private static DecimalFormat df = new DecimalFormat("#.##");

	/**
	 * @param sourceObject
	 * @param targetObject
	 * @return
	 */
	@Override
	public HistoricalResponse mapSourceToTargetObject(List<WeatherDataBean> sourceObject,
			HistoricalResponse targetObject) {
		if (targetObject == null) {
			targetObject = new HistoricalResponse();
		}

		double avgTemp = sourceObject.stream().mapToDouble(WeatherDataBean::getTemp).average().orElse(0);
		double avgPressure = sourceObject.stream().mapToDouble(WeatherDataBean::getPressure).average().orElse(0);
		targetObject.setAvgTtemp(Double.valueOf(df.format(avgTemp)));
		targetObject.setAvgPressure(avgPressure);
		List<WeatherResponse> history = sourceObject.stream().map(obj -> mapper.mapSourceToTargetObject(obj, null))
				.collect(Collectors.toList());
		targetObject.setHistory(history);
		return targetObject;
	}

}
