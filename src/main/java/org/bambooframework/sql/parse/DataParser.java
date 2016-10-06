package org.bambooframework.sql.parse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bambooframework.common.CommonUtils;
import org.bambooframework.sql.parse.entity.QueryDataParser;
import org.bambooframework.sql.parse.entity.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataParser {
	private static final Logger log = LoggerFactory.getLogger(DataParser.class);

	/**
	 * 解析json串
	 * 
	 * @author lei
	 * @param jsonData
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @date 2016年6月14日
	 * @Description:
	 */
	@SuppressWarnings("unchecked")
	public static RootParamMap parserJson(String jsonData){
		log.debug(jsonData);
		RootParamMap rootTblDataMap = new RootParamMap();
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map;
		try {
			map = objectMapper.readValue(jsonData,
					HashMap.class);
		} catch (Exception e) {
			log.error("解析json异常",e);
			throw new RuntimeException("解析json异常", e);
		}
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			if("BEFORE".equalsIgnoreCase(key)
					||"OPERATE".equalsIgnoreCase(key)
					||"AFTER".equalsIgnoreCase(key)){
				parseOperate(rootTblDataMap, entry);
			}else {
				rootTblDataMap.put(key, entry.getValue());
			}
		}
		paseRef(rootTblDataMap);
		log.debug(rootTblDataMap.toString());
		return rootTblDataMap;
	}

	private static void parseOperate(RootParamMap rootTblDataMap,
			Map.Entry<String, Object> entry) {
		List<Map<String, Object>> operateMaps = (List<Map<String, Object>>) entry
				.getValue();

		for (Map<String, Object> operateMap : operateMaps) {
			DataMap newOperateMap = new DataMap();
			for (Map.Entry<String, Object> entry2 : operateMap
					.entrySet()) {
				String key2 = entry2.getKey();
				newOperateMap.put(key2, entry2.getValue());
			}
			String type = (String) newOperateMap.get("TYPE");
			if ("insert".equalsIgnoreCase(type)) {
				// 解析表单数据
				InsertParam tblDataMap = new InsertParam();
				tblDataMap.setDatabase((String) newOperateMap.get("database"));
				rootTblDataMap.put(entry.getKey(),(String) newOperateMap.get("ID"),
						tblDataMap);
				parseInsertData(
						(Map<String, Object>) newOperateMap.get("DATA"),
						tblDataMap);
			} else if ("query".equalsIgnoreCase(type)) {
				rootTblDataMap.put(entry.getKey(),(String) newOperateMap.get("ID"),
						new QueryDataParser().parser(newOperateMap));
			}
		}
	}

	/**
	 * 解析数据索引
	 * @author lei
	 * @param tblDataMap
	 * @date 2016年6月16日
	 * @Description:
	 */
	private static void paseRef(ParamMap tblDataMap) {
		for (Map.Entry<String, Object> entry : tblDataMap.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof String) {
				String val = (String) value;
				if (val.startsWith("&")) {
					DataRef ref = parseRef(tblDataMap,val.substring(1));
					tblDataMap.put(entry.getKey(), ref);
					setDepend(tblDataMap, ref.getDataMap());
				}
			}else if(value instanceof QueryParam){
				paseWhereRef((QueryParam)value);
			}else if (value instanceof ParamMap) {
				paseRef((ParamMap) value);
			} else if (value instanceof List) {
				for (ParamMap dataMap : (List<ParamMap>) value) {
					paseRef(dataMap);
				}
			}
		}
	}

	/**
	 * 解析where条件索引
	 * @author lei
	 * @param queryParamMap
	 * @date 2016年6月16日
	 * @Description:
	 */
	private static void paseWhereRef(QueryParam queryParamMap) {
//		String where = queryParamMap.getWhere();
//		StringBuffer newWhere = new StringBuffer();
//		if(where == null){
//			return;
//		}
//		Pattern p = Pattern.compile("\\&.[^\\s]*");
//		Matcher m = p.matcher(where);
//		while(m.find()){
//			String group = m.group();
//			m.appendReplacement(newWhere, "?");
//			DataRef ref = parseRef(queryParamMap, group.substring(1));
//			queryParamMap.addParam(ref);
//			setDepend(queryParamMap, ref.getDataMap());
//		}
//		m.appendTail(newWhere);
//		queryParamMap.setWhere(newWhere.toString());
	}
	private static void paseWhereRef(GetParam getParamMap) {
		String where = getParamMap.getWhere();
		StringBuffer newWhere = new StringBuffer();
		if(where == null){
			return;
		}
		Pattern p = Pattern.compile("\\&.[^\\s]*");
		Matcher m = p.matcher(where);
		while(m.find()){
			String group = m.group();
			m.appendReplacement(newWhere, "?");
			DataRef ref = parseRef(getParamMap, group.substring(1));
			getParamMap.addParam(ref);
			setDepend(getParamMap, ref.getDataMap());
		}
		m.appendTail(newWhere);
		getParamMap.setWhere(newWhere.toString());
	}

	private static DataRef parseRef(Param tblDataMap, String refid) {
		String[] names = refid.split("\\.");
		if (names.length == 1) {
			// 引用当前对象的其它字段
			DataRef df = new DataRef(tblDataMap, names[0]);
			return df;
		} else {
			// 不是当引用当前对象的其它字段
			// 1. 直系
			Param p = tblDataMap;
			while ((p = p.parent()) != null) {
				Object obj = p.get(names[0]);
				if (obj != null && obj instanceof ParamMap) {
					int i = 0;
					// 找到了根节点，从跟节点往下寻找，找到倒数第二节点
					ParamMap pre = (ParamMap) obj;
					while (++i < names.length - 1) {
						obj = ((ParamMap) obj).get(names[i]);
						if(obj !=null && obj instanceof ParamMap){
							pre = (ParamMap) obj;
						}else{
							break;
						}
					}

					// 全部匹配
					DataRef df = new DataRef(pre,CommonUtils.concat( Arrays.copyOfRange(names, i, names.length), "."));
					return df;
				} else {
					break;
				}
				// 找不到，则继续往上找
			}
		}
		return null;
	}

	private static void setDepend(Param sourceDataMap,
			Param targetDataMap) {
		if(sourceDataMap == targetDataMap){
			return;
		}
		List<Param> paths1 = sourceDataMap.getPaths();
		List<Param> paths2 = targetDataMap.getPaths();
		int lenght = Math.min(paths1.size(), paths2.size());
		for (int i = 0; i < lenght; i++) {
			if (paths1.get(i) != paths2.get(i)) {
				paths2.get(i).addDependBy(paths1.get(i));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void parseInsertData(String[] names, Object val,
			ParamMap parent) {
		if (names.length == 1) {
			if (val instanceof List) {
				// 数组
				for (Map<String, Object> nextObj : (List<Map<String, Object>>) val) {
					ParamMap o = new ParamMap();
					parent.add(names[0], o);
					parseInsertData(nextObj, o);
				}
			} else if (val instanceof Map) {
				// 数组
				Object obj = parent.get(names[0]);
				if (obj == null || !(obj instanceof ParamMap)) {
					obj = new ParamMap();
					parent.put(names[0], obj);
				}
				parseInsertData((Map<String, Object>) val, (ParamMap) obj);
			} else {
				// 只有一个且值为简单对象是
				parent.put(names[0], val);
			}
		} else {
			ParamMap child = (ParamMap) parent.get(names[0]);
			if (child == null) {
				child = new ParamMap();
				;
				parent.put(names[0], child);
			}
			parseInsertData(Arrays.copyOfRange(names, 1, names.length), val,
					child);
		}
	}

	private static ParamMap parseInsertData(Map<String, Object> objMap,
			ParamMap parent) {
		/*
		 * {"tbl.col":
		 * '',"tbl2.col":'',"tbl1.col2":{col:'',col2:''},tb3:[{col:''},{col:''}]}
		 * ==》 {tbl:{col:'',col2:{col:'',col2:''},tb2:{col1:''}};
		 */
		for (Map.Entry<String, Object> objEntry : objMap.entrySet()) {
			String[] names = objEntry.getKey().split("\\.");
			parseInsertData(names, objEntry.getValue(), parent);
		}
		return parent;
	}
}
