package org.bambooframework.sql.parse.entity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bambooframework.sql.parse.DataMap;
import org.bambooframework.sql.parse.Param;

public class QueryParam extends ArrayList<DataMap> implements Param{
	private QueryResultView query;
	private Integer pageNo;
	private Integer pageSize;
	private String database;
	
	public String getName() {
		return name;
	}
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	protected String name;
	
	protected Param parent;

	public Param parent() {
		return parent;
	}

	public List<Param> getPaths() {
		List<Param> paths;
		if(this.parent == null){
			paths = new ArrayList<Param>();
		}else{
			paths = this.parent.getPaths();
		}
		paths.add(this);
		return paths;
	}
	
	Set<Param> dependby = new LinkedHashSet<Param>();
	
	public void addDependBy(Param ref){
		dependby.add(ref);
	}
	
	public boolean dependBy(Param ref){
		return dependby.contains(ref);
	}

	@Override
	public Object get(String name) {
		return null;
	}
	
	public boolean add(DataMap value){
		return super.add(value);
	}

	@Override
	public void setParent(Param param) {
		this.parent = param;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Set<Param> getDependby() {
		return dependby;
	}

	public void setDependby(Set<Param> dependby) {
		this.dependby = dependby;
	}

	public Param getParent() {
		return parent;
	}

	public QueryResultView getQuery() {
		return query;
	}

	public void setQuery(QueryResultView query) {
		this.query = query;
	}
	
}
