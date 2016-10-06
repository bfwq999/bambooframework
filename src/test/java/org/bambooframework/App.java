package org.bambooframework;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bambooframework.common.CodeConstants;
import org.bambooframework.sql.parse.DataMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PascalCaseStrategy;

public class App {
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		String jsonData = "{\"a\":{\"B\":\"c\"}}";
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PascalCaseStrategy.LOWER_CASE);
		Map<String, Object> map;
		map = objectMapper.readValue(jsonData,
				DataMap.class);
		System.out.println(map);
	}
}
