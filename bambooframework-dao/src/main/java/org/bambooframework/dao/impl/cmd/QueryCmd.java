package org.bambooframework.dao.impl.cmd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bambooframework.core.util.StringUtils;
import org.bambooframework.dao.db.metadata.Database;
import org.bambooframework.dao.db.sql.DataMap;
import org.bambooframework.dao.db.sql.DbSqlSession;
import org.bambooframework.dao.db.sql.Query;
import org.bambooframework.dao.db.transaction.TransactionIsolationLevel;
import org.bambooframework.dao.impl.cfg.TransactionPropagation;
import org.bambooframework.dao.impl.interceptor.CommandContext;
import org.bambooframework.dao.impl.interceptor.DbSqlCommand;

public class QueryCmd extends DbSqlCommand<DataMap> {
	
	/**
	 * 插入数据格式
	 * {
	 * 	id:"",
	 *  pre:[],
	 *  success:[],
	 *  fail:[],
	 *  transactionPropagation:"",
	 *  transactionIsolationLevel:"",
	 *  query:{
	 *  	table:{
	 *  		name:"",
	 *  		alias:"",
	 *  		columns:[colname,{name:"",alias:""},{alias:"",func:""}]
	 *  	} 或 {查询语句},
	 *  	database:"",
	 *  	join:[{
	 *  		jointype:"left",
	 *  		table:{alias:"",
	 *  				columns:[]
	 *  		},
	 *  		on:[],
	 *  	}],
	 *  	where:[{left:col1,sign:"=",right:"1"}
	 *  			,{logic="and",where:{left:col1,sign:"=",right:"1"}}]
	 *  	order:[{col:col1,type:"asc"},{col:col1,type:"asc"}]
	 *  	group:["col1","col2",{func:"",args:[]}]
	 *  	pageno:1
	 *  	pagesize:1
	 *  }
	 * }
	 */
	protected DataMap queryData;
	protected CommandContext commandContext;
	protected DbSqlSession dbSqlSession;
	protected Database database;
	
	public QueryCmd() {
		super();
		
	}

	public QueryCmd(DataMap query) {
		super();
		this.queryData = query;
	}

	@Override
	public void pre(CommandContext commandContext) throws Throwable {
		this.commandContext = commandContext;
		this.dbSqlSession = commandContext.getDbSqlSession();
	}

	@Override
	public void success(CommandContext commandContext) throws Throwable {
	}

	@Override
	public void fail(CommandContext commandContext, Throwable e)
			throws Throwable {
	}

	@Override
	public DataMap doExecute(CommandContext commandContext) throws Throwable {
		Query query = new Query(queryData);
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Connection conn = dbSqlSession.getDatabase(query.getDatabase()).getConnection();
			stmt = conn.createStatement();
			String sql = query.toSql();
			System.err.println(sql);
			rs = stmt.executeQuery(sql);
		}finally{
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	
	@Override
	public TransactionPropagation getTransactionPropagation(
			CommandContext commandContext) {
		String propagation = (String) queryData.get("TransactionPropagation");
		if(propagation  == null){
			return TransactionPropagation.REQUIRED;
		}
		return TransactionPropagation.valueOf(StringUtils.upperCase(propagation));
	}

	@Override
	public TransactionIsolationLevel getTransactionIsolationLevel(
			CommandContext commandContext) {
		String level = (String) queryData.get("TransactionIsolationLevel");
		if(level == null){
			return null;
		}
		return TransactionIsolationLevel.valueOf(StringUtils.upperCase(level));
	}

	@Override
	public void setCommandData(DataMap dataMap) {
		this.queryData = dataMap;
	}
}
