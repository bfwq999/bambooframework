package org.bambooframework.sql.parse;
/**
 * 引用对象
 * @author lei
 * @date 2016年6月15日
 * @Description:
 */
public class DataRef{
	Param dataMap; //引用目标对象
	String colName; //目标字段
	
	public DataRef(Param dataMap, String colName) {
		this.dataMap = dataMap;
		this.colName = colName;
	}
	public Object getValue(){
		if(dataMap == null){
			throw new RuntimeException("引用未初始化");
		}
		String[] colNames = colName.split("\\.");
		if(colNames.length == 1){
			return dataMap.get(colName);
		}
		int i=-1; 
		while(++i < colNames.length-1){
			Object o = dataMap.get(colNames[i]);
			if(dataMap == null){
				return null;
			}
			if(o instanceof Param){
				dataMap = (Param) o;
			}else{
				return null;
			}
		}
		return dataMap.get(colNames[i]);
	}
	public Param getDataMap() {
		return dataMap;
	}
}