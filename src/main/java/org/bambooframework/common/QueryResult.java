package org.bambooframework.common;

import java.io.Serializable;
import java.util.List;

/**
 * 查询结果
 * @author lei
 * @date 2015年9月15日
 * @Description:
 */
public class QueryResult implements Serializable {
	private static final long serialVersionUID = 6782883407969582986L;

	private List<?> rows; //数据
	private Pagination pagination; //分页信息
	
	public QueryResult(List<?> rows, Pagination pagination) {
		super();
		this.rows = rows;
		this.pagination = pagination;
	}

	public List<?> getRows() {
		return rows;
	}

	public Pagination getPagination() {
		return pagination;
	}
	
	public boolean isQueryResult() {
		return true;
	}
}
