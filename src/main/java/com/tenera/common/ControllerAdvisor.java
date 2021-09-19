package com.tenera.common;

import java.time.Clock;
import java.time.ZonedDateTime;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ControllerAdvice
public class ControllerAdvisor {
	private static Logger logger = LoggerFactory.getLogger(ControllerAdvisor.class);

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ RestClientException.class })
	public ResponseEntity<Object> handleCityNotFoundException(Exception cityNotFoundException,
			HttpServletRequest request) {
		logger.error("City not Found", cityNotFoundException);
		return new ResponseEntity<>(
				new ApiError(ZonedDateTime.now(Clock.systemUTC()), HttpStatus.NOT_FOUND.value(), "City Not Found."),
				HttpStatus.NOT_FOUND);
	}

}

@Getter
@Setter
@AllArgsConstructor
class ApiError {

	private final ZonedDateTime timestamp;
	private final Object code;
	private final String message;
}
