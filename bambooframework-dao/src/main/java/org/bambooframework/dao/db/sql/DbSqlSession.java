package org.bambooframework.dao.db.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bambooframework.dao.db.transaction.Transaction;
import org.bambooframework.dao.impl.cfg.TransactionPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbSqlSession {
	private static Logger log = LoggerFactory.getLogger(DbSqlSession.class);
	
	protected Transaction transaction;
	protected TransactionPropagation transactionPropagation;
	Map<String, Database> databaseMap = new HashMap<String, DbSqlSession.Database>(0);
	
	
	public DbSqlSession() {
		super();
	}

	public DbSqlSession(Transaction transaction,TransactionPropagation transactionPropagation) {
		this.transaction = transaction;
		this.transactionPropagation = transactionPropagation;
	}
	
	public void flush() throws SQLException{
		for(Database database:databaseMap.values()){
			database.flush();
		}
	}
	
	public void close() throws SQLException{
		transaction.close();
	}
	
	public Database getDatabase(String database) throws SQLException{
		Database db = databaseMap.get(database);
		if(db == null){
			db = new Database(transaction.getConnection(database));
			databaseMap.put(database, db);
		}
		return db;
	}
	
	public void commit() throws SQLException{
		transaction.commit();
	}
	
	public void rollback() throws SQLException{
		transaction.rollback();
	}
	
	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction,TransactionPropagation transactionPropagation) {
		this.transaction = transaction;
		this.transactionPropagation =  transactionPropagation;
	}
	
	public class Database{
		Connection connection;
		List<Object[]> insertSqls = new ArrayList<Object[]>();
		
		public Database(Connection connection) {
			super();
			this.connection = connection;
		}

		public void excuteUpdate(String insertSql){
			insertSqls.add(new Object[]{insertSql,null});
		}
		public void excuteUpdate(String insertSql,Object[] params){
			insertSqls.add(new Object[]{insertSql,params});
		}
		public void excuteUpdateBatch(String insertSql,List<Object[]> params){
			insertSqls.add(new Object[]{insertSql,params});
		}
		
		public long queryCount(String querySql,Object...params) throws SQLException{
			long result = 0;
			PreparedStatement preStmt = null;
			ResultSet rs = null;
			try {
				preStmt = connection.prepareStatement(querySql);
				for(int i=0;i<params.length; i++){
					preStmt.setObject(i+1, params[i]);
				}
				rs = preStmt.executeQuery();
				if(rs.next()){
					result = rs.getLong(1);
				}
			} finally{
				if(rs!=null){
					try {
						rs.close();
					} catch (SQLException e) {
						//do noting
					}
				}
				if(preStmt!=null){
					try {
						preStmt.close();
					} catch (SQLException e) {
						//do noting
					}
				}
			}
			return result; 
		}
		public Connection getConnection() {
			return connection;
		}
		public void flush() throws SQLException{
			if(insertSqls.size() == 0){
				return;
			}
			PreparedStatement stmt = null;
			try {
				for(Object[] insertSql:insertSqls){
					stmt = connection.prepareStatement((String)insertSql[0]);
					if(insertSql[1] instanceof List){
						for(Object[] params:(List<Object[]>)insertSql[1]){
							for (int i = 0; i < params.length; i++) {
								stmt.setObject(i+1, params[i]);
							}
							stmt.addBatch();
						}
						stmt.executeBatch();
					}else{
						Object[] params = ((Object[])insertSql[1]);
						for (int i = 0; i < params.length; i++) {
							stmt.setObject(i+1, params[i]);
						}
						stmt.execute();
					}
					stmt.close();
				}
			}finally{
				if(stmt!=null){
					try {
						stmt.close();
					} catch (SQLException e) {
						//do nothing
					}
				}
			}
			
			reset();
		}
		public void reset(){
			insertSqls =  new ArrayList<Object[]>();
		}
	}
	
	
}
