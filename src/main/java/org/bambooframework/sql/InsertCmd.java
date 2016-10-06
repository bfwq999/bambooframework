package org.bambooframework.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bambooframework.common.IdGenerator;
import org.bambooframework.sql.metadata.Column;
import org.bambooframework.sql.metadata.Table;
import org.bambooframework.sql.parse.DataRef;
import org.bambooframework.sql.parse.InsertParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertCmd implements Command {
	private static final Logger log = LoggerFactory.getLogger(InsertCmd.class);	
	private InsertParam tblDataMap;
	
	public InsertCmd(InsertParam tblDataMap) {
		this.tblDataMap = tblDataMap;
	}
	public void execute(){
		try {
			for(Map.Entry<String, Object> entry:tblDataMap.sortEntrySet()){
				if(entry.getValue() instanceof InsertParam){
					insertTable((InsertParam) entry.getValue());
				}else if(entry.getValue() instanceof  List){
					for(InsertParam dataMap:(List<InsertParam>)entry.getValue()){
						insertTable((InsertParam) dataMap);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void insertTable(InsertParam tblData) throws SQLException{
		//先插主表数据
		String tblName = tblData.getTableName();
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(tblName);
		Table table = DatabaseUtils.getTable(tblData.getName());
		StringBuffer colstr = new StringBuffer();
		StringBuffer valstr = new StringBuffer();
		List<Object> values = new ArrayList<Object>();
		String idColName = table.getPrimaryKey().getName();
		Object id = tblData.get(idColName);
		if(id == null){
			tblData.put(idColName, IdGenerator.getNextId());
		}
		for(Column col:table.getColumns()){
			Object val = null;
			val = tblData.get(col.getName());
			if(val == null){
				continue;
			}else if(val instanceof DataRef){
				val = ((DataRef)val).getValue();
			}else if(val instanceof InsertParam){
				InsertParam dataMap = (InsertParam) val;
				String name = dataMap.getName();
				Table t = DatabaseUtils.getTable(name);
				////判断当前字段与对象表的关联关系
				Column fkCol = table.getForeignColumn(t.getName() + "." + t.getPrimaryKey().getName());
				if(col.getName().equals(fkCol.getName())){
					//~是主外键
					val = dataMap.get(t.getPrimaryKey().getName());
				}
			}
			if(colstr.length()>0){
				colstr.append(",");
				valstr.append(",");
			}
			colstr.append(col.getName());
			valstr.append("?");
			values.add(val);
		}
		sql.append("(").append(colstr).append(")");
		sql.append("values (").append(valstr).append(")");
		log.debug("sql:"+sql);
		log.debug("params:"+values);
		Connection conn = DatabaseUtils.getDataSource(tblData.getDbName()).getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql.toString());
		for (int i = 0; i < values.size(); i++) {
			pstmt.setObject(i+1, values.get(i));
		}
		pstmt.executeUpdate();
		conn.close();
		
		for(Map.Entry<String, Object> entry:tblData.entrySet()){
			//先把关联关系表数据插入,获取这两表的关联关系字段
			Object val = entry.getValue();
			if(val instanceof List){
				//一对多
				Table fktable = DatabaseUtils.getTable(entry.getKey());
				Column col = fktable.getForeignColumn(table.getName() +"." + table.getPrimaryKey().getName());
				if(col == null){
					return;
				}
				for(InsertParam fkTbleData:(List<InsertParam>)val){
					fkTbleData.put(col.getName(), tblData.get(table.getPrimaryKey().getName()));
					//fkTbleData.put(col.getName(), tblData);
					insertTable(fkTbleData);
				}
			}
		}
	}
	
}
