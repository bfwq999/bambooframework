package org.bambooframework.config;

import java.io.IOException;
import java.util.Map;

import org.bambooframework.web.vo.QueryParam;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 查询参数的序列化
 * @author lei
 * @date 2015年8月29日
 * @Description:
 */
public class QueryParamSerializer extends JsonSerializer<QueryParam> {

	@Override
	public void serialize(QueryParam value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		Map<String,Object> map = value.getInnerMap();
		provider.defaultSerializeValue(map, jgen);
	}

}
