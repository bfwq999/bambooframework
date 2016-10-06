package org.bambooframework.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.bambooframework.entity.Org;
import org.bambooframework.entity.Position;
import org.bambooframework.services.AuthService;
import org.bambooframework.sql.parse.DataMap;
import org.bambooframework.sql.parse.DataParser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestInsert extends TestSqlBase {
	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthService authSerivce;
	
	
	@Test
	public void test_insert() throws Exception{
		DataMap data = DataParser.parserJson(FileUtils.getData("insert-single.json"));
		CommandExecutor exe = new CommandExecutor(data);
		DataMap tableDataMap = exe.execute();
		Object val = getValueInTableDataMap(tableDataMap, "ORG.T_ORG.ID_");
		String orgName = (String) getValueInTableDataMap(tableDataMap, "ORG.T_ORG.name_");
		Org org = authSerivce.get(Org.class, val.toString());
		assertNotNull(org);
		assertEquals(orgName, org.getName());
	}
	@Test
	public void test_insert2() throws Exception{
		DataMap data = DataParser.parserJson(FileUtils.getData("insert-one2many.json"));
		CommandExecutor exe = new CommandExecutor(data);
		DataMap tableDataMap = exe.execute();
		Object val = getValueInTableDataMap(tableDataMap, "ORG.T_ORG.ID_");
		String orgName = (String) getValueInTableDataMap(tableDataMap, "ORG.T_ORG.name_");
		Org org = authSerivce.get(Org.class, val.toString());
		assertNotNull(org);
		assertEquals(orgName, org.getName());
		
		
		List<DataMap>  poses = (List<DataMap>) getValueInTableDataMap(tableDataMap, "ORG.T_ORG.T_POSITION");
		String posName = (String) poses.get(0).get("name_");
		Integer posId = (Integer) poses.get(0).get("id_");
		Position pos = authSerivce.get(Position.class, posId);
		assertNotNull(pos);
		assertEquals(posName, pos.getName());
	}
	@Test
	public void test_insert3() throws Exception{
		DataMap data = DataParser.parserJson(FileUtils.getData("insert-depend.json"));
		DataMap operate = (DataMap)data.get("operate");
		CommandExecutor exe = new CommandExecutor(data);
		
		DataMap tableDataMap = exe.execute();
		Object val = getValueInTableDataMap(tableDataMap, "ORG.T_ORG.ID_");
		String orgName = (String) getValueInTableDataMap(tableDataMap, "ORG.T_ORG.name_");
		Org org = authSerivce.get(Org.class, val.toString());
		assertNotNull(org);
		assertEquals(orgName, org.getName());
		
		List<DataMap>  poses = (List<DataMap>) getValueInTableDataMap(tableDataMap, "ORG.T_POSITION");
		String posName = (String) poses.get(0).get("name_");
		Integer posId = (Integer) poses.get(0).get("id_");
		Position pos = authSerivce.get(Position.class, posId);
		assertNotNull(pos);
		assertEquals(posName, pos.getName());
	}
	@Test
	public void test_insert4() throws Exception{
		DataMap data = DataParser.parserJson(FileUtils.getData("insert-query.json"));
		DataMap operate = (DataMap)data.get("operate");
		DataMap m = (DataMap)operate.get("ORG");
		CommandExecutor exe = new CommandExecutor(data);
		exe.execute();
	}
}
