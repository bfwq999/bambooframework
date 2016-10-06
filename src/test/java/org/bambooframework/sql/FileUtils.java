package org.bambooframework.sql;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileUtils {
	public static String getData(String fileName) throws IOException {
		InputStream is = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			is = FileUtils.class.getResourceAsStream("testdata/" + fileName);
			byte[] buff = new byte[1024];
			int len;
			while ((len = is.read(buff)) != -1) {
				baos.write(buff, 0, len);
			}
			return baos.toString();
		} finally{
			if(is!=null){
				is.close();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		String jsonData = getData("insert-single.json");
		System.out.println(jsonData);
		
	}
}
