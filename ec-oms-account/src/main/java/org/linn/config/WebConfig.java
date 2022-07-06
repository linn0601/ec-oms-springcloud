package org.linn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.linn.resp.JsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	// 覆盖掉原有的MappingJackson2HttpMessageConverter
	@Bean
	public HttpMessageConverter<Object> customJsonHttpMessageConverter(ObjectMapper objectMapper){

		JsonHttpMessageConverter messageConverter = new JsonHttpMessageConverter(objectMapper);
		messageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		return messageConverter;
	}
}
