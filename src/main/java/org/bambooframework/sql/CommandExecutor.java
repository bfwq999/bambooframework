package org.bambooframework.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bambooframework.sql.metadata.Column;
import org.bambooframework.sql.metadata.Table;
import org.bambooframework.sql.parse.DataMap;
import org.bambooframework.sql.parse.GetParam;
import org.bambooframework.sql.parse.InsertParam;
import org.bambooframework.sql.parse.ParamMap;
import org.bambooframework.sql.parse.RootParamMap;
import org.bambooframework.sql.parse.entity.QueryParam;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


public class CommandExecutor {

	private RootParamMap data;

	public CommandExecutor(RootParamMap data) {
		this.data = data;
	}
	
	public DataMap execute(){
 		for(Map.Entry<String,Object> entry :data.sortEntrySet()){
			//将各操作转换成cmd
			Object obj = entry.getValue();
			if(obj instanceof InsertParam){
				new InsertCmd((InsertParam)obj).execute();
			}else if(obj instanceof QueryParam){
				new QueryCmd((QueryParam)obj).execute();
			}else if(obj instanceof GetParam){
				new GetCmd((GetParam)obj).execute();
			}
		}
 		data.clearOthers();
		return data;
	}
	
//	private static List<TableInstance> parseTables(Map<String,Object> data) {
//		List<TableInstance> tblInsts = new ArrayList<TableInstance>();
//		for(Map.Entry<String, Object> tblEntry:data.entrySet()){
//			String tableName = tblEntry.getKey();
//			Table table = DatabaseUtils.getTable(tableName);
//			if(table == null){
//				log.warn(tableName + "不存在"); 
//				continue;
//			}
//			if(tblEntry.getValue() instanceof Map){
//				//单表
//				TableInstance tblInst = new TableInstance();
//				tblInst.setTableName(tableName);
//				parseColumn((Map)tblEntry.getValue(), tblInst);
//				tblInsts.add(tblInst);
//			}else if(tblEntry.getValue() instanceof List){
//				List<Map<String,Object>> tblList = (List)tblEntry.getValue();
//				//多表
//				for(Map<String,Object> tbl:tblList){
//					TableInstance tblInst = new TableInstance();
//					tblInst.setTableName(tableName);
//					parseColumn(tbl, tblInst);
//					tblInsts.add(tblInst);
//				}
//			}else{
//				//格式不对
//				log.warn(tableName + "格式不对"); 
//			}
//		}
//		return tblInsts;
//	}
//	
//	private static void parseColumn(Map<String,Object> colsObj,TableInstance tblInst){
//		for(Map.Entry<String, Object> colEntry:colsObj.entrySet()){
//			String colName = colEntry.getKey();
//			if(colEntry.getValue() instanceof Map){
//				//外键或1对多
//				Column fkCol = DatabaseUtils.getDatabase(tblInst.getDbName()).getTable(tblInst.getTableName())
//				.getColumn(colName).getForeignKey();
//				if(fkCol == null){
//					log.warn(colName +"不是外键");
//					continue;
//				}
//				TableInstance fkTblInst = new TableInstance();
//				fkTblInst.setTableName(fkCol.getTable().getName());
//				tblInst.addForeignColumn(colName, fkTblInst);
//				parseColumn((Map)colEntry.getValue(),fkTblInst);
//			}else if(colEntry.getValue() instanceof List){
//				//1对多关联关系
//				List<Map<String,Object>> list = (List<Map<String, Object>>) colEntry.getValue();
//				for(Map<String,Object> m:list){
//					TableInstance fkTblInst = new TableInstance();
//					fkTblInst.setTableName(colEntry.getKey());
//					tblInst.addOneTonMany(fkTblInst);
//					parseColumn(m,fkTblInst);	
//				}
//			}else{
//				//普通字段
//				tblInst.addColumns(colEntry.getKey(), colEntry.getValue());
//			}
//		}
//	}
}
