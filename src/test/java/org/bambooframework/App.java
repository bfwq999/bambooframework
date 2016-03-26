package org.bambooframework;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bambooframework.common.CodeConstants;

public class App {
	
	public static void main(String[] args) {
		Map<String,Object> consts = new HashMap<String, Object>();
		try {
			Field[] fields = CodeConstants.class.getDeclaredFields();
			for(Field field:fields){
				consts.put(field.getName(),field.get(null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(consts);
	}
}
