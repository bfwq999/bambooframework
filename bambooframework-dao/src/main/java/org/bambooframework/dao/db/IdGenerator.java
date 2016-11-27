package org.bambooframework.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.bambooframework.core.exception.BambooException;

public class IdGenerator{
	
	private  DataSource dataSource;
	
	private static final int DEFAULT_CAPACITY = 50;
	private  int currVal = 0; //当前位置
	private  int lastVal = -1; //最大位置
	
	public IdGenerator(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	public synchronized Integer getNextId(){
		if(currVal>lastVal){
			grow();
		}
		return currVal++;
	}
	/**  扩充数据 */
	private  void grow(){
		Connection conn;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
		int lval = 0;
		int val = 0;
		try {
			conn = dataSource.getConnection();
			preStmt = conn.prepareStatement("SELECT VALUE_ FROM T_PROPERTY WHERE ID_=?");
			preStmt.setString(1, "nextid");
			rs = preStmt.executeQuery();
			
			
			
			if(rs.next()){
				String  value = rs.getString("VALUE_");
				val = Integer.valueOf(value);
				lval = val+DEFAULT_CAPACITY; //保留DEFAULT_CAPACITY个
			}
		}catch (Exception e) {
			throw new BambooException(e);
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(preStmt != null){
				try {
					preStmt.close();
				} catch (SQLException e) {
				}
			}
		}
		try {
			preStmt = conn.prepareStatement("UPDATE T_PROPERTY SET VALUE_=? WHERE ID_=?");
			preStmt.setString(1, String.valueOf(lval));
			preStmt.setString(2, "nextid");
			preStmt.executeUpdate();
		} catch (SQLException e1) {
			throw new BambooException(e1);
		}finally{
			if(preStmt != null){
				try {
					preStmt.close();
				} catch (SQLException e) {
				}
			}
		}
		//保证数据库值更新成功后
		currVal = val;
		lastVal = lval-1;
	}
}
