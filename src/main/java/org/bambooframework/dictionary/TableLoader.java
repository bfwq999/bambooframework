package org.bambooframework.dictionary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.bambooframework.common.PinyinUtil;
import org.springframework.util.Assert;

public class TableLoader implements Loader {
	
	/** 缓存 */
	Map<String,List<Map<String, String>>> cache;

	/** 数据源 */
	private DataSource dataSource;

	/** 表名 */
	private String tableName;

	/** 字典名称 */
	private String nameColumn;

	/** 文字对应的字段 */
	private String textColumn;

	/** 值对应的字段 */
	private String valueColumn;

	/** 父值对应的字段 */
	private String parentColumn;

	/** 过滤对应的字段 */
	private String filterColumn;

	/** 排序字段 */
	private String sortColumn;

	public TableLoader(Map<String, List<Map<String, String>>> cache,
			DataSource dataSource, String tableName, String nameColumn,
			String textColumn, String valueColumn, String parentColumn) {
		super();
		this.cache = cache;
		this.dataSource = dataSource;
		this.tableName = tableName;
		this.nameColumn = nameColumn;
		this.textColumn = textColumn;
		this.valueColumn = valueColumn;
		this.parentColumn = parentColumn;
	}

	public TableLoader(DataSource dataSource, String tableName,
			String nameColumn, String textColumn, String valueColumn,
			String parentColumn, String filterColumn, String sortColumn) {
		super();
		this.dataSource = dataSource;
		this.tableName = tableName;
		this.nameColumn = nameColumn;
		this.textColumn = textColumn;
		this.valueColumn = valueColumn;
		this.parentColumn = parentColumn;
		this.filterColumn = filterColumn;
		this.sortColumn = sortColumn;
	}

	@Override
	public List<Map<String, String>> load(String codeName, Map<String, String> args) {
		List<Map<String,String>> codes = cache.get(codeName);
		Assert.notNull(codes, codeName+"字典不存在");
		
		if(args!=null && args.size()>0){
			List<Map<String,String>> newCodes = new ArrayList<Map<String,String>>();
			
			String args_parent = (String)args.get("parent");
			Integer args_filter = null;
			if(args.get("filter")!=null){
				args_filter = Integer.parseInt(args.get("filter"));
			}
			
			for(Map<String,String> code:codes){
				if(args_parent!=null && !args_parent.equals(code.get(parentColumn))){
					//与父id不匹配
					continue;
				}
				if(args_filter!=null){
					String filter = code.get("filter");
					if(filter==null || filter.length()<args_filter){
						//库中的过滤值不存在或小于指定的长度
						continue;
					}
					if(filter.charAt(args_filter-1) == '0'){
						//指定的位数值不是1
						continue;
					}
				}
				//没有被过滤
				newCodes.add(code);
			}
			return newCodes;
		}
		return codes;
	}
	
	/**
	 * 加载所有字典数据到缓存中
	 * @author lei
	 * @date 2015年9月19日
	 * @Description:
	 */
	@PostConstruct
	public void loadAllCodes(){
		//所有字典
		cache = new HashMap<String, List<Map<String,String>>>();
		
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(tableName);
		sql.append(" order by ");
		sql.append(nameColumn);
		if (sortColumn != null) {
			sql.append(",");
			sql.append(sortColumn);
		}
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql.toString());
			
			if(rs.next()){
				List<Map<String, String>> codes = new ArrayList<Map<String,String>>();
				String preCodeName = rs.getString(nameColumn); //前一个字典名
				while(true){
					Map<String, String> code = new HashMap<String, String>();
					String name = rs.getString(nameColumn);
					String text = rs.getString(textColumn);
					code.put("name", name);
					code.put("text", text);
					code.put("value", rs.getString(valueColumn));
					code.put("filter", rs.getString(filterColumn));
					code.put("parent", rs.getString(parentColumn));
					code.put("fullPinyin", PinyinUtil.getFullPinyin(text));
					code.put("halfPinyin", PinyinUtil.getHalfPinyin(text));
					
					if (!name.equals(preCodeName) ) {
						//与前一个字典名比,不是同一个code
						//将前一个字典数据放入allCodes中
						cache.put(preCodeName, codes);
						//重新new一个list放一下类字典的
						codes = new ArrayList<Map<String,String>>();
						preCodeName = name;
					}
					codes.add(code);
					
					if(!rs.next()){
						//没有下一个字典
						//将最后一类字典放入allCodes中
						cache.put(preCodeName, codes);
						break;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeConnection(conn, stmt, rs);
		}
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTextColumn(String textColumn) {
		this.textColumn = textColumn;
	}

	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}

	public void setParentColumn(String parentColumn) {
		this.parentColumn = parentColumn;
	}

	public void setFilterColumn(String filterColumn) {
		this.filterColumn = filterColumn;
	}

	@Override
	public void cleanCache() {
		loadAllCodes();
	}

	@Override
	public void cleanCache(String codeName) {
		cleanCache();
	}

	public Map<String, List<Map<String, String>>> getCache() {
		return cache;
	}
}
