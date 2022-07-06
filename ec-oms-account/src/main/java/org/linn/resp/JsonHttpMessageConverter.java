package org.linn.resp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class JsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

	public JsonHttpMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper);
	}


}
