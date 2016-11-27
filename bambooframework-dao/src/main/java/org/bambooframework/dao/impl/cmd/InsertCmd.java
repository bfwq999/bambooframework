package org.bambooframework.dao.impl.cmd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bambooframework.core.util.ArrayUtils;
import org.bambooframework.core.util.StringUtils;
import org.bambooframework.dao.db.metadata.Column;
import org.bambooframework.dao.db.metadata.Database;
import org.bambooframework.dao.db.metadata.DatabaseMetaData;
import org.bambooframework.dao.db.metadata.Table;
import org.bambooframework.dao.db.sql.DataMap;
import org.bambooframework.dao.db.sql.DbSqlSession;
import org.bambooframework.dao.db.transaction.TransactionIsolationLevel;
import org.bambooframework.dao.impl.cfg.TransactionPropagation;
import org.bambooframework.dao.impl.interceptor.CommandContext;
import org.bambooframework.dao.impl.interceptor.DbSqlCommand;

public class InsertCmd extends DbSqlCommand<DataMap> {
	/**
	 * 插入数据格式
	 * {
	 * 	id:"",
	 *  pre:[],
	 *  success:[],
	 *  fail:[],
	 *  transactionPropagation:"",
	 *  transactionIsolationLevel:""
	 *  data:[{table:
	 *  		{col1:val,col2:val,table:[]} 或[{col1:val,col2:val,table:[]}],
	 *  		database:""}],
	 *  或
	 *  data:{table:{col1:val,col2:val,table:[]}或[{col1:val,col2:val,table:[]}],
	 *  		database:""}
	 * }
	 */
	protected DataMap insert;
	protected CommandContext commandContext;
	protected DbSqlSession dbSqlSession;
	protected Database database;
	
	public InsertCmd() {
		super();
	}

	public InsertCmd(DataMap insert){
		this.insert = insert;
	}

	@Override
	public void pre(CommandContext commandContext) throws Throwable{
		this.commandContext = commandContext;
		this.dbSqlSession = commandContext.getDbSqlSession();
	}

	@Override
	public void success(CommandContext commandContext) throws Throwable{
	}

	@Override
	public void fail(CommandContext commandContext, Throwable e)
			throws Throwable {
	}

	@Override
	public DataMap doExecute(CommandContext commandContext) throws Throwable{
		Object insertDataObj = insert.get("data");
		DataMap result = new DataMap();
		result.put((String)insert.get("id"),insertDataObj);
		if(insertDataObj instanceof List){
			List<DataMap> insertDatas = (List)insertDataObj;
			commandContext.putCommandContextData((String)insert.get("id"),insertDatas);
			for(DataMap insertData:insertDatas){
				insertTable(insertData);
			}
		}else{
			DataMap insertData = (DataMap)insertDataObj;
			commandContext.putCommandContextData((String)insert.get("id"),insertData);
			insertTable(insertData);
		}
		return result;
	}

	private void insertTable(DataMap insertData)
			throws SQLException {
		String dataSourceName = (String) insertData.get("database");
		if(dataSourceName == null){
			dataSourceName =  commandContext.getDaoEngineConfiguration().getDefaultDataSourceName();
		}
		for(Map.Entry<String, Object> entry:insertData.entrySet()){
			if(entry.getValue() instanceof DataMap || entry.getValue() instanceof List){
				Table table = DatabaseMetaData.getTable(dataSourceName, entry.getKey());
				if(table != null){
					Object colVals = entry.getValue();
					if(colVals instanceof List){
						insertTable(null,null,table, (List<DataMap>)colVals);
					}else{
						insertTable(table, (DataMap)colVals);
					}
				}
			}
		}
		
	}
	
	private void insertTable(Table table,DataMap colVals) throws SQLException{
		StringBuffer sql = new StringBuffer();
		
		Collection<Column> columns = table.getColumns();
		
		String[] keys = new String[columns.size()];
		Object[] vals = new Object[columns.size()];
		
		int i = 0;
		for(Column col:columns){
			keys[i] = col.getName();
			Object val = null;
			
			val = colVals.get(col.getName());
			if(val == null){
				if(col.isPrimaryKey()){
					val = commandContext.getDaoEngineConfiguration().getIdGenerator().getNextId();
					colVals.put(col.getName(), val);
				}
			}else{
				if(val instanceof DataMap){
					//外键,取外键值
					val = ((DataMap)val).get(col.getForeignKey().getName());
				}
			}
			vals[i] = val;
			i++;
		}
		sql.append("insert into ").append(table.getName())
		.append("(").append(ArrayUtils.toString(keys,","))
		.append(") values(")
		.append(StringUtils.repeat("?", keys.length, ","))
		.append(")");
		dbSqlSession.getDatabase(table.getDatabase().getName()).excuteUpdate(sql.toString(),vals);
		
		//插入1对多关联关系表数据
		for(String key:colVals.keySet()){
			if(table.getColumn(key) == null){
				//不是字段，判断是否是表
				Object val = colVals.get(key);
				if(val instanceof List){
					Table ctable = DatabaseMetaData.getTable(table.getDatabase().getName(), key);
					insertTable(table, colVals, ctable, (List<DataMap>)val);
				}
			}
		}
	}
	
	/**
	 * 插入一对多关联关系表
	 * @author lei
	 * @param mainTable
	 * @param mainData
	 * @param table
	 * @param colValsList
	 * @throws SQLException 
	 * @date 2016年11月20日
	 * @Description:
	 */
	private void insertTable(Table mainTable,DataMap mainData,Table table,List<DataMap> colValsList) throws SQLException{
		StringBuffer sql = new StringBuffer();
		
		Collection<Column> columns = table.getColumns();
		String[] keys = new String[columns.size()];
		
		List<Object[]> valsList = new ArrayList<Object[]>(colValsList.size());
		
		for(DataMap colVals:colValsList){
			Object[] vals = new Object[columns.size()];
			valsList.add(vals);
			int i = 0;
			for(Column col:columns){
				keys[i] = col.getName();
				Object val = null;
				if(col.getValue()!=null){
					//有指定值
					val = col.getValue();
					colVals.put(col.getName(), val);
				}else if(mainTable!=null && col.getForeignKey()!=null && col.getForeignKey() == mainTable.getPrimaryKey()){
					//判断是否是多对1外键,值设置成外键表主键值
					val = mainData.get(col.getForeignKey().getName());
					colVals.put(col.getName(), val);
				}else{
					val = colVals.get(col.getName());
					if(val == null){
						if(col.isPrimaryKey()){
							val = commandContext.getDaoEngineConfiguration().getIdGenerator().getNextId();
							colVals.put(col.getName(), val);
						}else if(col.getDefaultValue() != null){
							//如果值为空且有默认值
							val = col.getDefaultValue();
							colVals.put(col.getName(), val);
						}
					}else if(val instanceof DataMap){
						//外键,取外键值
						val = ((DataMap)val).get(col.getForeignKey().getName());
					}
				}
				
				vals[i] = val;
				i++;
			}
		}
		sql.append("insert into ").append(table.getName())
		.append("(").append(ArrayUtils.toString(keys,","))
		.append(") values(")
		.append(StringUtils.repeat("?", keys.length, ","))
		.append(")");
		dbSqlSession.getDatabase(table.getDatabase().getName()).excuteUpdateBatch(sql.toString(),valsList);
		
		for(DataMap colVals:colValsList){
			//插入1对多关联关系表数据
			for(String key:colVals.keySet()){
				if(table.getColumn(key) == null){
					//不是字段，判断是否是表
					Object val = colVals.get(key);
					if(val instanceof List){
						Table ctable = DatabaseMetaData.getTable(table.getDatabase().getName(), key);
						insertTable(table, colVals, ctable, (List<DataMap>)val);
					}
				}
			}
		}
	}
	
	@Override
	public TransactionPropagation getTransactionPropagation(
			CommandContext commandContext) {
		String propagation = (String) insert.get("TransactionPropagation");
		if(propagation  == null){
			return TransactionPropagation.REQUIRED;
		}
		return TransactionPropagation.valueOf(StringUtils.upperCase(propagation));
	}

	@Override
	public TransactionIsolationLevel getTransactionIsolationLevel(
			CommandContext commandContext) {
		String level = (String) insert.get("TransactionIsolationLevel");
		if(level == null){
			return null;
		}
		return TransactionIsolationLevel.valueOf(StringUtils.upperCase(level));
	}

	@Override
	public void setCommandData(DataMap dataMap) {
		this.insert = dataMap;
	}
}
