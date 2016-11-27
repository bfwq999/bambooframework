package org.bambooframework.dao.db.sql;

import java.util.List;

import org.bambooframework.dao.TestBase;
import org.bambooframework.dao.db.sql.Query.JoinTable;
import org.junit.Assert;
import org.junit.Test;


public class TestQuery extends TestBase{

	@Test
	public void testQuery(){
		DataMap queryData = new DataMap();
		queryData.put("database", "datasource1")
		.put("pageno", "1")
		.put("pagesize", 10);
		
		//主表
		DataMap tableData = new DataMap();
		String col1 = "NAME_";
		DataMap col2 = new DataMap().put("name", "CODE_").put("alias", "ORGCODE");
		tableData.put("name", "T_ORG")
		.add("columns", col1)
		.add("columns", col2);
		queryData.put("table", tableData);
		
		//关联表
		DataMap join1 = new DataMap();
		DataMap joinTable1 = new DataMap();
		joinTable1.put("name", "T_ORG")
		.put("alias", "PARENT")
		.add("columns", "NAME_");
		join1.put("table", joinTable1);
		join1.put("jointype", "left");
		join1.add("on", new DataMap().put("left", "T_ORG.PARENT_ID_").put("right", "PARENT.ID_").put("sign", "="));
		queryData.add("join", join1);
		
		//子查询
		DataMap join2 = new DataMap();
		DataMap joinquery2 = new DataMap();
		DataMap tableData2 = new DataMap();
		String col2_1 = "COMMENT_";
		DataMap col2_2 = new DataMap().put("name", "NAME_").put("alias", "POS_NAME");
		tableData2.put("name", "T_POSITION")
		.add("columns", col2_1)
		.add("columns", col2_2);
		joinquery2.put("table", tableData2)
		.put("alias", "POS")
		.add("where", new DataMap().put("left", "NAME_").put("SIGN", "LIKE").put("RIGHT", "'测试岗位1'"));
		
		join2.put("table", joinquery2)
		.put("jointype", "left")
		.add("on",  new DataMap().put("left", "T_ORG.ID_").put("right", "POS.ORG_ID_").put("sign", "="));
		
		queryData.add("join", join2);
		//查询条件
		queryData.add("where",new DataMap().put("left", "1").put("sign", "=").put("right", "T_ORG.ID_"))
		.add("where", new DataMap()
							.put("logic", "and")
							.add("where", 
									new DataMap().put("left", "T_ORG.NAME_").put("right", "'测试机构1'").put("sign", "="))
							.add("where", 
									new DataMap().put("logic", "or").put("left", "T_ORG.NAME_").put("right", "'测试机构2'").put("sign", "="))
						     .add("where", new DataMap().put("left", "POS.NAME_").put("SIGN", "IS").put("RIGHT", "NULL"))
				);
		
		Query query = new Query(queryData);
		
		Assert.assertNotNull(query.findColumn("T_ORG.NAME_",query));
		Assert.assertNotNull(query.findColumn("POS.POS_NAME",query));
		
		//第一个join
		JoinTable joinTable = query.getJoinTables().get(0);
		Assert.assertEquals(SqlJoinType.LEFT, joinTable.getJoinType());
		Assert.assertEquals("PARENT_ID_", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getLeft().getName());
		Assert.assertEquals("T_ORG", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getLeft().getTable().getAlias());
		Assert.assertEquals("PARENT", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getRight().getTable().getAlias());
		Assert.assertEquals("ID_", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getRight().getName());
		Assert.assertEquals("=", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getSign());
		
		//第二个join
		joinTable = query.getJoinTables().get(1);
		Query joinTable2 = (Query) joinTable.getTable();
		Assert.assertEquals(SqlJoinType.LEFT, joinTable.getJoinType());
		Assert.assertEquals("ID_", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getLeft().getName());
		Assert.assertEquals("T_ORG", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getLeft().getTable().getAlias());
		Assert.assertEquals("POS", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getRight().getTable().getAlias());
		Assert.assertEquals("ORG_ID_", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getRight().getName());
		Assert.assertEquals("=", 
				((SqlWhere)joinTable.getOn().getSqlWheres().get(0)).getSign());
		
		Assert.assertEquals("1", ((SqlWhere)query.getWhere().getSqlWheres().get(0)).getLeft().toSql());
		Assert.assertEquals(SqlLogicType.AND, query.getWhere().getSqlLogicTypes().get(1));
		Assert.assertEquals("'测试机构2'",((SqlWhere) ((SqlWhereGroup)query.getWhere().getSqlWheres().get(1)).getSqlWheres().get(1)).getRight().toSql());
		Assert.assertEquals("NULL",((SqlWhere) ((SqlWhereGroup)query.getWhere().getSqlWheres().get(1)).getSqlWheres().get(2)).getRight().toSql());
		Assert.assertEquals("POS_NAME",((SqlWhere) ((SqlWhereGroup)query.getWhere().getSqlWheres().get(1)).getSqlWheres().get(2)).getLeft().getName());
		
		System.out.println(query.toSql());
	}
}
