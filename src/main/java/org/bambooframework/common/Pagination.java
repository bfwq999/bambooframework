package org.bambooframework.common;

import org.apache.ibatis.session.RowBounds;

public class Pagination extends RowBounds {
	
	/** 每页记录数 */
	protected int pageSize = NO_ROW_LIMIT;
	
	/** 当前页面码 */
	protected int pageNo = 1;
	
	/** 总记录数 */
	protected int total;

	
	public Pagination() {
		super();
	}

	public Pagination(int pageNo, int pageSize) {
		this.setPageNo(pageNo);
		this.setPageSize(pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1) {
			pageSize = 10;
		}
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		if(pageNo>getTotalPage()){
			//页码不超过最大页数
			pageNo = getTotalPage();
		}
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if(pageNo<1){
			pageNo = 1;
		}
		this.pageNo = pageNo;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public int getOffset() {
		return (pageNo-1) * pageSize;
	}

	@Override
	public int getLimit() {
		return pageSize;
	}
	
	public int getTotalPage(){
		return (int) Math.ceil((double)total/pageSize);
	}
}
