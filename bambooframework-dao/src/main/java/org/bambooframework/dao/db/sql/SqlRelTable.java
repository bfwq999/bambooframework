package org.bambooframework.dao.db.sql;

import java.util.ArrayList;
import java.util.List;

import org.bambooframework.core.util.StringUtils;
import org.bambooframework.dao.db.metadata.Column;
import org.bambooframework.dao.exception.ParseSqlDataException;

/**
 * 关联表字段
 * @author lei
 * @date 2016年11月26日
 * @Description:
 */
public class SqlRelTable extends SqlTable{
	
	OrderBy orderBy;
	
	SqlWhereGroup where;
	
	/**
	 * 
	 * @param source
	 * <pre>
	 * 格式：
	 * 1. "表名"
	 * 2. {table:"",columns:[],alias:"",where:[],order:[]}
	 * </pre>
	 * @param parent
	 */
	public SqlRelTable(String source, SqlTable parent) {
		super(source, parent);
	}
	public SqlRelTable(DataMap source, SqlTable parent) {
		super(source, parent);
	}

	@Override
	public String toSql() {
		return null;
	}

	@Override
	protected void compile() {
		super.compile();
		List<DataMap> whereData;
		List<Object> orders = null;
		if(this.source instanceof String){
			whereData =  new ArrayList<DataMap>();
		}else{
			whereData =  (List<DataMap>) ((DataMap)this.source).get("where");
			orders =  (List<Object>) ((DataMap)this.source).get("orders");
		}
		//加上两表关联条件
		Column relColumn = this.getTable().getForeignColumn(((SqlTable)this.parent).getTable().getPrimaryKey());
		whereData.add(new DataMap()
							.put("logic", "and")
							.put("left", relColumn.getName())
							.put("sign", "=")
							.put("right", ((SqlTable)this.parent).getAlias()+"."+relColumn.getForeignKey().getName()));
		this.where = new SqlWhereGroup(whereData, this, this);
		
		if(orders != null){
			this.orderBy = new OrderBy(orders);
		}
	}

	@Override
	protected void link() {
		super.link();
		this.where.link();
		this.orderBy.link();
	}
	
	/**
	 * <pre>
	 * [{column:字段,type:"asc"}]
	 * </pre>
	 * @author lei
	 * @date 2016年11月26日
	 * @Description:
	 */
	class OrderBy extends SqlBuilder {
		List<SqlColumnable> column;
		List<String> type;

		public OrderBy(List<Object> source) {
			super(source, SqlRelTable.this, SqlRelTable.this);
		}

		@Override
		public String toSql() {
			return null;
		}

		@Override
		protected void compile() {
		}

		@Override
		protected void link() {
			List<Object> source =  (List<Object>) this.source;
			column = new ArrayList<SqlColumnable>(source.size());
			type = new ArrayList<String>(source.size());
			
			for(Object order:source){
				if(order instanceof String){
					String type = null;
					String colName = null;
					
					String[] orderData = ((String) order).split("\\s");
					if(orderData.length==1){
						colName = orderData[0];
						type = "asc";
					}else if(orderData.length==2){
						colName = orderData[0];
						type = orderData[1];
					}else{
						throw new ParseSqlDataException("字段{0}格式不对", (String)order);
					}
					
					this.column.add(SqlRelTable.this.getColumn(colName));
					this.type.add(type);
				}else{
					DataMap orderData = (DataMap) order;
					Object column = orderData.get("column");
					String type = (String) orderData.get("type");
					if(StringUtils.isEmpty(type)){
						type = "asc";
					}
					if(column instanceof String){
						SqlColumnable col = SqlRelTable.this.getColumn((String) column);
						if(col == null){
							throw new ParseSqlDataException("字段{0}不存在", (String)column);
						}
						this.column.add(col);
						this.type.add(type);
					}else{
						DataMap dataMap = (DataMap)column;
						//不是字段
						String func = (String) dataMap.get("func");
						if(func != null){
							//函数
							
						}else{
							Object tableName  = dataMap.get("table");
							if(tableName != null){
								//查询语句
								this.column.add(new Query(dataMap, SqlRelTable.this, SqlRelTable.this)); 
							}else{
								throw new ParseSqlDataException("语法错误:{0}", dataMap.toString());
							}
						}
					}
					
				}
				
			}
		}
	}
	

}
