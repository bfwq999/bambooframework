package org.bambooframework.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bambooframework.dao.db.sql.DataMap;
import org.bambooframework.dao.db.sql.DbSqlSession;
import org.bambooframework.dao.impl.cfg.TransactionPropagation;
import org.bambooframework.dao.impl.cmd.InsertCmd;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestInsert {
	@BeforeClass
	public static void initDaoEngine() {
		DaoEngines.init();
	}

	@AfterClass
	public static void destroyDaoEngine() {
		DaoEngines.destroy();
	}

	@Test
	public void testSimpleInsert() throws SQLException {
		DataMap insert = new DataMap()
		.put("id", "insert1")
		.put("data", 
				new DataMap()
					.put("T_ORG",new DataMap()
						.put("NAME_", "测试机构1")
						.put("CODE_", "001"))
					);
		DataMap result = DaoEngines.getDefaultDaoEngine().getDaoEngineConfiguration()
				.getCommandExecutor().execute(new InsertCmd(insert));
		DataMap org = (DataMap)((DataMap)result.get("insert1")).get("T_ORG");
		Assert.assertEquals("测试机构1", (String)org.get("name_"));
		Assert.assertNotNull(org.get("id_"));
		
		DbSqlSession dbSqlSession = new DbSqlSession(DaoEngines.getDefaultDaoEngine().getDaoEngineConfiguration()
				.getTransactionFactory().newTransaction(null, false),TransactionPropagation.NOT_SUPPORTED);
		long c = dbSqlSession.getDatabase(DaoEngines.getDefaultDaoEngine().getDaoEngineConfiguration().getDefaultDataSourceName())
		.queryCount("select count(1) from T_ORG where NAME_=?", "测试机构1");
		Assert.assertEquals(1l,c);
	}
	
	@Test
	public void testOneToManyInsert() throws SQLException {
		List<DataMap> positions = new ArrayList<DataMap>();
		for (int i = 0; i < 3; i++) {
			DataMap dataMap = new DataMap();
			dataMap.put("NAME_", "测试岗位"+i);
			positions.add(dataMap);
		}
		DataMap insertDataColVals = new DataMap();
		insertDataColVals.put("NAME_", "测试机构2")
		.put("CODE_", "001")
		.put("T_POSITION", positions);
		DataMap insertData = new DataMap()
		.put("id", "insert2")
		.put("data", new DataMap().put("T_ORG", insertDataColVals));
		   
		DaoEngines.getDefaultDaoEngine().getDaoEngineConfiguration()
		.getCommandExecutor().execute(new InsertCmd(insertData));
		
		
		DbSqlSession dbSqlSession = new DbSqlSession(DaoEngines.getDefaultDaoEngine().getDaoEngineConfiguration()
				.getTransactionFactory().newTransaction(null, false),TransactionPropagation.NOT_SUPPORTED);
		long result = dbSqlSession.getDatabase(DaoEngines.getDefaultDaoEngine().getDaoEngineConfiguration().getDefaultDataSourceName())
				.queryCount("select count(1) from T_ORG where  NAME_=?", "测试机构2");
		Assert.assertEquals(1l,result);
		result = dbSqlSession.getDatabase(DaoEngines.getDefaultDaoEngine().getDaoEngineConfiguration().getDefaultDataSourceName())
				.queryCount("select count(1) from T_POSITION where  NAME_ like ?", "测试岗位%");
		Assert.assertEquals(3l,result);
	}
}
