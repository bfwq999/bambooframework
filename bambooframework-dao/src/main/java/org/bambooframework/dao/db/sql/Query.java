package org.bambooframework.dao.db.sql;

import java.util.ArrayList;
import java.util.List;

import org.bambooframework.core.util.StringUtils;
import org.bambooframework.dao.exception.ParseSqlDataException;

public class Query extends SqlBuilder implements SqlTableable, SqlColumnable {
	
	String id;

	String alias;

	SqlTableable table;
	
	List<SqlColumnable> columns;

	List<JoinTable> joinTables;

	SqlWhereGroup where;

	GroupBy group;

	OrderBy order;
	
	String database;
	
	Integer pageNo;

	Integer pageSize;
	
	List<SqlResultColumn> resultColumns;

	/** <pre>
	 * 格式:
	 * {
	 *  	table:{
	 *  		name:"",
	 *  		alias:"",
	 *  		columns:[colname,{name:"",alias:""},{alias:"",func:""}]
	 *  	} 或 {查询语句},
	 *  	database:"",
	 *  	alias:"",
	 *  	join:[{
	 *  		jointype:"left",
	 *  		table:表,
	 *  		on:{},
	 *  	}],
	 *  	columns:[],
	 *  	where:[{left:col1,sign:"=",right:"1"}
	 *  			,{logic="and",where:{left:col1,sign:"=",right:"1"}}]
	 *  	order:[{col:col1,type:"asc"},{col:col1,type:"asc"}]
	 *  	group:["col1","col2",{func:"",args:[]}]
	 *  	pageno:1
	 *  	pagesize:1
	 * }
	 * </pre> */
	public Query(DataMap source) {
		this(source, null,null);
	}
	public Query(DataMap source,SqlBuilder parent,SqlTableable scope){
		super(source,parent,scope);
	}

	/**
	 * 编译各表数据，不处理引用
	 * @author lei
	 * @date 2016年11月24日
	 * @Description:
	 */
	protected void compile() {
		joinTables = new ArrayList<Query.JoinTable>(0);
		columns = new ArrayList<SqlColumnable>(0);
		
		DataMap source = (DataMap)this.source;
		
		this.database = (String)source.get("database");
		
		//解析
		Object pageno = source.get("pageno");
		if(pageno!=null){
			if(pageno instanceof String){
				this.pageNo = Integer.parseInt((String) pageno);
			}else{
				this.pageNo = ((Number) pageno).intValue();
			}
		}
		Object pagesize = source.get("pagesize");
		if(pagesize!=null){
			if(pagesize instanceof String){
				this.pageNo =  Integer.parseInt((String) pagesize);
			}else{
				this.pageNo = ((Number) pagesize).intValue();
			}
		}
		
		this.alias = (String) source.get("alias");
		
		Object tableData = source.get("table");
		this.table = newTable(tableData);
		List<DataMap> joins = (List<DataMap>) source.get("join");
		if(joins != null){
			for(DataMap join:joins){
				this.joinTables.add(new JoinTable(join));
			}
		}
		
		//编译where
		this.where = new SqlWhereGroup((List<DataMap>) source.get("where"),this,this); 
		
	}
	@Override
	protected void link() {
		DataMap source = (DataMap) this.source;
		
		//先连接表是查询语句的
		if(this.table instanceof Query){
			((Query)this.table).link();
		}
		for(JoinTable joinTable:this.joinTables){
			if(joinTable.getTable() instanceof Query){
				((Query)joinTable.getTable()).link();
			}
		}
		
		//再先连接实体表
		if(this.table instanceof SqlTable){
			((SqlTable)this.table).link();
		}
		for(JoinTable joinTable:this.joinTables){
			joinTable.link();
		}
		
		this.where.link();
		
		//List<DataMap> order = (List<DataMap>) source.get("order");
		//List<DataMap> group = (List<DataMap>) source.get("group");

	}
	
	protected SqlTableable newTable(Object tableData){
		if(tableData == null){
			throw new ParseSqlDataException("表名不能为空");
		}
		if(tableData instanceof String){
			tableData = ((String) tableData).trim();
			if("".equals(tableData)){
				throw new ParseSqlDataException("表名不能为空");
			}
			return new SqlTable((String)tableData,this);
		}else if(tableData instanceof DataMap){
			DataMap tableDataMap = ((DataMap) tableData);
			if(tableDataMap.get("table") != null){
				//查询语句
				return new Query(tableDataMap,this,null);
			}else{
				return new SqlTable(tableDataMap, this);
			}
		}
		throw new ParseSqlDataException("格式不正确："+tableData);
	}

	@Override
	public String toSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		List<SqlResultColumn> rcols = this.getColumns();
		for(int i=0; i<rcols.size(); i++){
			if(i>0){
				sql.append(",");
			}
			SqlResultColumn rcol = rcols.get(i);
			sql.append(rcol.getColumn().getTable().getAlias())
			.append(".")
			.append(rcol.getColumn().getName());
			sql.append(" as ")
			.append(rcol.getColumn().getId());
		}
		sql.append(" from ");
		sql.append(this.table.getName()).append(" as ").append(this.table.getAlias());
		for(JoinTable joinTb:this.joinTables){
			sql.append(joinTb.toSql());
		}
		sql.append(" where ");
		sql.append(this.where.toSql());
		return sql.toString();
	}
	
	/**
	 * group
	 * @author lei
	 * @date 2016年11月27日
	 * @Description:
	 */
	class GroupBy extends SqlBuilder{
		
		List<SqlColumnable> columns;
		
		public GroupBy(List<Object> source) {
			super(source,  Query.this, Query.this);
		}

		@Override
		protected void compile() {
		}

		@Override
		protected void link() {
		}
		
		
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
			super(source, Query.this, Query.this);
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
					this.column.add(Query.this.findColumn(colName, Query.this));
					this.type.add(type);
				}else{
					DataMap orderData = (DataMap) order;
					Object column = orderData.get("column");
					String type = (String) orderData.get("type");
					if(StringUtils.isEmpty(type)){
						type = "asc";
					}
					if(column instanceof String){
						SqlColumnable col = Query.this.findColumn((String)column, Query.this);
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
								this.column.add(new Query(dataMap, Query.this, Query.this)); 
							}else{
								String  colName = (String) dataMap.get("column");
								this.column.add(Query.this.getColumn(tableName+"."+colName));
							}
						}
					}
					
				}
				
			}
		}
	}
	
	class JoinTable extends SqlBuilder {
		SqlTableable table;

		SqlJoinType joinType;

		SqlWhereGroup on;
		
		public JoinTable(DataMap source){
			super(source, Query.this, Query.this);
		}
		
		public SqlTableable getTable() {
			return table;
		}

		public SqlJoinType getJoinType() {
			return this.joinType;
		}


		public SqlWhereGroup getOn() {
			return on;
		}

		@Override
		public String toSql() {
			StringBuffer sql = new StringBuffer();
			sql.append(" ")
			.append(this.getJoinType().getJoinTypeValue())
			.append(" ");
			if(this.getTable() instanceof Query){
				sql.append("(")
				.append(this.getTable().toSql())
				.append(")");
			}else{
				sql.append(this.getTable().getName());
			}
			sql.append(" as ")
			.append(this.getTable().getAlias())
			.append(" on ")
			.append(this.getOn().toSql());
			return sql.toString();
		}

		@Override
		protected void compile() {
			DataMap source = (DataMap) this.source;
			String type = (String) source.get("jointype");
			if(StringUtils.isEmpty(type)){
				type = SqlJoinType.INNER.toString();
			}
			type = type.toUpperCase();
			DataMap table = (DataMap) source.get("table");
			this.table  = newTable(table);
			this.joinType = SqlJoinType.valueOf(type);
			this.on = new SqlWhereGroup((List<DataMap>) source.get("on"),Query.this,Query.this);
		}

		@Override
		protected void link() {
			DataMap source = (DataMap) this.source;
			//先解析表中是查询语句
			((SqlBuilder)this.table).link();
			this.on.link();
		}

	}

	public String getAlias() {
		return this.alias;
	}


	@Override
	public String getDatabase() {
		return database;
	}
	
	@Override
	public String getName() {
		return null;
	}
	@Override
	public SqlTableable getTable() {
		return null;
	}
	
	protected SqlTableable getSqlTable(String name){
		if(name.equals(this.table.getAlias())){
			return this.table;
		}
		if(this.joinTables==null){
			return null;
		}
		for(JoinTable joinTable:joinTables){
			if(name.equals(joinTable.getTable().getAlias())){
				return joinTable.getTable();
			}
		}
		return null;
	}
	
	public SqlColumnable getColumn(String colName){
		
		return null;
	}
	
	@Override
	public SqlColumnable findColumn(String colName,Sqlable caller) {
		String[] colNames =  colName.split("\\.");
		if(colNames.length==2){
			String tableName = colNames[0];
			SqlTableable sqlTable = getSqlTable(tableName);
			if(sqlTable == null){
				return null;
			}
			if(caller == sqlTable){
				return null;
			}
			SqlColumnable col = sqlTable.findColumn(colNames[1],this);
			if(col!=null && this.getParent() == caller){
				//外层取里层字段
				return new SqlResultColumn(this, col);
			}
			return col;
		}
		
		//字段名只有一个
		SqlColumnable col = this.table.findColumn(colName,this);
		if(col == null){
			for(JoinTable joinTable:this.joinTables){
				if(caller == joinTable.getTable()){
					continue;
				}
				col = joinTable.getTable().findColumn(colName,this);
				if(col != null){
					break;
				}
			}
		}
		if(this.getParent() == caller){
			//外层取里层字段
			return new SqlResultColumn(this, col);
		}
		return col;
	}
	public List<JoinTable> getJoinTables() {
		return joinTables;
	}
	public SqlWhereGroup getWhere() {
		return where;
	}
	public GroupBy getGroup() {
		return group;
	}
	public OrderBy getOrder() {
		return order;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	@Override
	public List<SqlResultColumn> getColumns() {
		if(resultColumns!=null){
			return resultColumns;
		}
		resultColumns = new ArrayList<SqlResultColumn>();
		for(SqlColumnable col:this.columns){
			resultColumns.add(new SqlResultColumn(this,col));
		}
		for(SqlColumnable col:this.table.getColumns()){
			resultColumns.add(new SqlResultColumn(this,col));
		}
		for(JoinTable joinTable:this.joinTables){
			for(SqlColumnable col:joinTable.getTable().getColumns()){
				resultColumns.add(new SqlResultColumn(this,col));
			}
			
		}
		return resultColumns;
	}
	@Override
	public String getId() {
		if(id == null){
			id = generateId();
		}
		return id;
	}
	
	int id_ = 0;
	public String generateId(){
		return "ARG_"+(++id_);
	}
}
