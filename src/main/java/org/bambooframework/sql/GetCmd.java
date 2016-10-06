package org.bambooframework.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bambooframework.sql.metadata.Column;
import org.bambooframework.sql.metadata.Database;
import org.bambooframework.sql.metadata.Table;
import org.bambooframework.sql.parse.DataMap;
import org.bambooframework.sql.parse.DataRef;
import org.bambooframework.sql.parse.GetParam;

public class GetCmd implements Command {
	protected GetParam dataMap;
	protected Database database;
	protected int colIndex = 0;

	public GetCmd(GetParam dataMap) {
		this.dataMap = dataMap;
	}

	public String getColAlias() {
		return "COL" + (colIndex++);
	}

	public String getTableAlias() {
		return "TABLE" + (colIndex++);
	}

	public void parseResult(String[] colNames, ResultTable parentTable) {
		if (colNames == null || colNames.length == 0) {
			return;
		}
		Table table = parentTable.getTable();

		if ("*".equals(colNames[0])) {
			for (Column col : parentTable.getTable().getColumns()) {
				ResultColumn rcol = new ResultColumn(parentTable, col);
				rcol.setAlias(getColAlias());
				if (col.getForeignKey() != null) {
					Table fkTable = col.getForeignKey().getTable();
					ResultTable resultTable = new ResultTable(fkTable);
					resultTable.setAlias(getTableAlias());
					resultTable.setParent(parentTable);
					parentTable.addForeignTable(col.getName(), resultTable);

					ResultColumn fkrcol = new ResultColumn(resultTable,
							col.getForeignKey());
					fkrcol.setAlias(getColAlias());
					resultTable.addColumns(col.getForeignKey().getName(),
							fkrcol);
				} else {
					parentTable.addColumns(col.getName(), rcol);
				}
			}
		} else {
			Column column = table.getColumn(colNames[0]);
			if (column == null) {
				String[] tblNames = colNames[0].split("\\$");
				table = database.getTable(tblNames[0]);
				if (table == null) {
					// 不是字段，也不是表
					return;
				} else {
					// 是表
					ResultTable resultTable = new ResultTable(table);
					resultTable.setAlias(colNames[0]);
					resultTable.setParent(parentTable);
					parentTable.addOneToMany(colNames[0], resultTable);
					parseResult(
							Arrays.copyOfRange(colNames, 1, colNames.length),
							resultTable);
				}
			} else {
				// 是字段
				if (colNames.length == 1) {
					ResultColumn rcol = new ResultColumn(parentTable, column);
					rcol.setAlias(getColAlias());
					parentTable.addColumns(colNames[0], rcol);
				} else {
					// 字段是对象的，那就是外键了
					if (column.getForeignKey() == null) {
						// 不是有效的外键
						return;
					}
					ResultTable resultTable = new ResultTable(column
							.getForeignKey().getTable());
					resultTable.setAlias(getTableAlias());
					resultTable.setParent(parentTable);
					parentTable.addForeignTable(colNames[0], resultTable);
					parseResult(
							Arrays.copyOfRange(colNames, 1, colNames.length),
							resultTable);
				}
			}
		}
	}

	public Collection<ResultTable> parseResult(String result) {
		Map<String, ResultTable> tblMap = new HashMap<String, ResultTable>();
		for (String colName : result.split(",")) {
			String[] colNames = colName.split("\\.");
			String[] tblNames = colNames[0].split("$");
			ResultTable resultTbl = tblMap.get(colNames[0]);
			if (resultTbl == null) {
				Table table = DatabaseUtils.getTable(tblNames[0]);
				resultTbl = new ResultTable(table);
				resultTbl.setAlias(colNames[0]);
				tblMap.put(colNames[0], resultTbl);
			}
			parseResult(Arrays.copyOfRange(colNames, 1, colNames.length),
					resultTbl);
		}
		return tblMap.values();
	}

	public void buildSimpleSql(StringBuffer resultSb, StringBuffer fromSb,
			ResultTable tbl) {
		for (ResultColumn col : tbl.columns()) {
			if (resultSb.length() > 0) {
				resultSb.append(",");
			}
			resultSb.append(tbl.getAlias()).append(".").append(col.getName())
					.append(" ").append(col.getAlias());
		}
		for (Map.Entry<String, ResultTable> fkEntry : tbl.getForeignTables()
				.entrySet()) {
			String colName = fkEntry.getKey();
			ResultTable fkrtbl = fkEntry.getValue();
			Collection<ResultColumn> cols = fkrtbl.columns();
			if (cols.size() == 1
					&& cols.iterator()
							.next()
							.getName()
							.equals(tbl.getTable().getColumn(colName)
									.getForeignKey().getName())) {
				// 一个字段，且与外键对应的字段名相等
				ResultColumn col = cols.iterator().next();
				if (resultSb.length() > 0) {
					resultSb.append(",");
				}
				resultSb.append(tbl.getAlias()).append("." + colName)
						.append(" ").append(col.getAlias());
			} else {
				fromSb.append(" left join ");
				fromSb.append(fkrtbl.getName()).append(" ")
						.append(fkrtbl.getAlias());
				fromSb.append(" on ");
				fromSb.append(tbl.getAlias()).append(".").append(colName)
						.append(" = ").append(fkrtbl.getAlias()).append(".")
						.append(fkrtbl.getTable().getPrimaryKey().getName());
				buildSimpleSql(resultSb, fromSb, fkrtbl);
			}
		}

		for (ResultTable rtbl : tbl.oneToManys()) {
			fromSb.append(" left join ");
			fromSb.append(rtbl.getName()).append(" ").append(rtbl.getAlias());
			fromSb.append(" on ");
			fromSb.append(tbl.getAlias())
					.append(".")
					.append(tbl.getTable().getPrimaryKey().getName())
					.append(" = ")
					.append(rtbl.getAlias())
					.append(".")
					.append(rtbl
							.getTable()
							.getForeignColumn(
									tbl.getTable().getPrimaryKey()
											.getFullName()).getName());
			buildSimpleSql(resultSb, fromSb, rtbl);
		}
	}

	public StringBuffer buildSimpleSql(ResultTable tbl) {
		StringBuffer resultSb = new StringBuffer();
		StringBuffer fromSb = new StringBuffer();

		fromSb.append(tbl.getName()).append(" ").append(tbl.getAlias());
		buildSimpleSql(resultSb, fromSb, tbl);

		StringBuffer sql = new StringBuffer("select ");
		sql.append(resultSb);
		sql.append(" from ");
		sql.append(fromSb);
		System.out.println(sql);
		return sql;
	}

	@Override
	public void execute() {
		try {
			String result = dataMap.getResult();
			String where = dataMap.getWhere();
			String dbName = dataMap.getDatabase();
			String orderBy = dataMap.getOrderBy();
			database = DatabaseUtils.getDatabase(dbName);

			Collection<ResultTable> tbls = parseResult(result);

			if (tbls.size() == 0) {
				return;
			}

			StringBuffer sql;
			if (tbls.size() == 1) {
				sql = buildSimpleSql(tbls.iterator().next());
				if(where!=null){
					sql.append(" where ").append(where);
				}
				if(orderBy !=null ){
					sql.append(" ").append(orderBy);
				}
			} else {
				sql = buildComplexSql(tbls);
			}

			// sql.append();
			Connection conn = DatabaseUtils.getDataSource(null).getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			 for (int i = 0; i < dataMap.getParams().size(); i++) {
				 Object param = dataMap.getParams().get(i);
				 if(param instanceof DataRef){
					 param = ((DataRef)param).getValue();
				 }
				 pstmt.setObject(i+1,param);
			 }
			ResultSet rs = pstmt.executeQuery();
			if (tbls.size() == 1) {
				while (rs.next()) {
					loadSimpleData(rs, tbls.iterator().next());
				}
			}
			
			rs.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void loadTableData(ResultSet rs, ResultTable tbl,DataMap tblDataMap ) throws SQLException{
		for (ResultColumn col : tbl.columns()) {
			tblDataMap.put(col.getName(), rs.getObject(col.getAlias()));
		}
	}
	private void loadSimpleData(ResultSet rs, ResultTable tbl) throws SQLException {
		if (tbl.oneToManys().size() == 0 && tbl.getForeignTables().size()==0) {
			// 没有子关联,普通
			List<ResultTable> paths = tbl.getPaths();
			if(paths.size() == 1){
				//普通
				DataMap data = new DataMap();
				dataMap.put(tbl.getAlias(), data);
				loadTableData(rs, tbl, data);
			}else{
				//有父类,找父类,从跟路径开始
				Object rootDataMap = dataMap;
				for(ResultTable rt : paths ){
					ResultColumn pkcol = rt.getPrimaryColumn();
					//找到匹配的数据
					if(rootDataMap instanceof List){
						boolean find = false;
						for(DataMap dataMap : (List<DataMap>)rootDataMap){
							DataMap o =  (DataMap) dataMap.get(rt.getId());
							//o 不可能为空
							Object pk1 = o.get(pkcol.getName());
							Object pk2 = rs.getObject(pkcol.getAlias());
							if(pk2.equals(pk1)){
								rootDataMap = o;
								find = true;
								break;
							}
						}
						if(!find){
							DataMap o = new DataMap();
							loadTableData(rs, rt, o);
							DataMap tblDataMap = new DataMap();
							tblDataMap.put(rt.getAlias(),o);
							((List) rootDataMap).add(tblDataMap);
							rootDataMap = o;
						}
					}else if(rootDataMap instanceof DataMap){
						Object obj =  ((DataMap)rootDataMap).get(rt.getId());
						if(obj == null){
							obj = new DataMap();
							loadTableData(rs, rt, (DataMap)obj);
							if(rt.parent==null || rt.parent.isForeign(rt.getId())){
								((DataMap) rootDataMap).put(rt.getId(), obj);
							}else{
								((DataMap) rootDataMap).add(rt.getId(), (DataMap)obj);
							}
						}else if(obj instanceof List){
							boolean find = false;
							for(DataMap dataMap : (List<DataMap>)obj){
								DataMap o =  (DataMap) dataMap.get(rt.getId());
								//o 不可能为空
								Object pk1 = o.get(pkcol.getName());
								Object pk2 = rs.getObject(pkcol.getAlias());
								if(pk2.equals(pk1)){
									rootDataMap = o;
									find = true;
									break;
								}
							}
							if(!find){
								obj = new DataMap();
								loadTableData(rs, rt, (DataMap)obj);
								DataMap tblDataMap = new DataMap();
								tblDataMap.put(rt.getAlias(),(DataMap)obj);
								((List) obj).add(tblDataMap);
							}
						}
						rootDataMap = (DataMap)obj;
					}
					
				}
			}
		} else {
			for (ResultTable rtbl : tbl.oneToManys()) {
				loadSimpleData(rs, rtbl);
			}
			for (Map.Entry<String, ResultTable> entry : tbl.getForeignTables()
					.entrySet()) {
				ResultTable fkrtbl = entry.getValue();
				loadSimpleData(rs, fkrtbl);
			}
		}

	}

	private StringBuffer buildComplexSql(Collection<ResultTable> tbls) {
		return null;
	}

}
