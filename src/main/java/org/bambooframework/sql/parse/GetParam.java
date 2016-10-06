package org.bambooframework.sql.parse;

import java.util.ArrayList;
import java.util.List;

public class GetParam extends ParamMap{
	private String result;
	private String where;
	private String groupBy;
	private String orderBy;
	private String pageNo;
	private String pageSize;
	private List<Object> params = new ArrayList<Object>(0);
	
	private String database;

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public List<Object> getParams() {
		return params;
	}
	public void addParam(Object param){
		params.add(param);
	}
}
