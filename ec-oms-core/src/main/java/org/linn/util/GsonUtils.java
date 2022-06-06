package org.linn.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * GSON 相关操作工具类。
 */
public final class GsonUtils {

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final Gson simpleGson;

	private static final Gson gson;

	static {

		ExclusionStrategy serializationExclusionStrategy = new ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				return f.getAnnotation(JsonIgnore.class) != null;
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		};

		gson = new GsonBuilder()
						 .setDateFormat(DATETIME_FORMAT)
						 .registerTypeAdapter(LocalDateTime.class, getLocalDateTimeJsonSerializer())
						 .registerTypeAdapter(LocalDateTime.class, getLocalDateTimeJsonDeserializer())
						 .registerTypeAdapter(LocalDate.class, getLocalDateJsonSerializer())
						 .registerTypeAdapter(LocalDate.class, getLocalDateJsonDeserializer())
						 .addSerializationExclusionStrategy(serializationExclusionStrategy)
						 .create();

		simpleGson = new GsonBuilder()
						.registerTypeAdapter(LocalDateTime.class, getLocalDateTimeJsonSerializer())
						.registerTypeAdapter(LocalDateTime.class, getLocalDateTimeJsonDeserializer())
						.registerTypeAdapter(LocalDate.class, getLocalDateJsonSerializer())
						.registerTypeAdapter(LocalDate.class, getLocalDateJsonDeserializer())
						.addSerializationExclusionStrategy(serializationExclusionStrategy)
						.create();
	}

	private GsonUtils(){
	}

	public static Gson getSimpleGson(){
		return simpleGson;
	}

	public static Gson getGson(){
		return gson;
	}

	private static JsonSerializer<LocalDateTime> getLocalDateTimeJsonSerializer() {
		return (LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) -> {
			if (Objects.isNull(src)) {
				return JsonNull.INSTANCE;
			}
			else {
				return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
			}
		};
	}

	private static JsonDeserializer<LocalDateTime> getLocalDateTimeJsonDeserializer() {
		return (JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) -> {
			if (json instanceof JsonPrimitive) {

				String datetime = json.getAsJsonPrimitive().getAsString();
				if (StringUtils.isEmpty(datetime)) {
					return null;
				}
				else {
					return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(DATETIME_FORMAT));
				}
			}
			else {
				return null;
			}
		};
	}

	private static JsonSerializer<LocalDate> getLocalDateJsonSerializer() {
		return (LocalDate src, Type typeOfSrc, JsonSerializationContext context) -> {
			if (Objects.isNull(src)) {
				return JsonNull.INSTANCE;
			}
			else {
				return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
			}
		};
	}

	private static JsonDeserializer<LocalDate> getLocalDateJsonDeserializer() {
		return (JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) -> {

			if (json instanceof JsonPrimitive) {

				String date = json.getAsJsonPrimitive().getAsString();
				if (StringUtils.isEmpty(date)) {
					return null;
				}
				else {
					return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
				}
			}
			else {
				return null;
			}
		};
	}

}
