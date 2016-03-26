package org.bambooframework.dictionary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {
	/**
	 * 关闭连接
	 * @author lei
	 * @param conn
	 * @param pstmt
	 * @param rs
	 * @date 2015年9月19日
	 * @Description:
	 */
	public static void closeConnection(Connection conn,Statement stmt,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {}
		}
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {}
		}
	}
}
