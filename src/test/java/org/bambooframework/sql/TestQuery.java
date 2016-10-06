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

public class TestQuery extends TestSqlBase {
	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthService authSerivce;
	
	
	@Test
	public void test_query() throws Exception{
		DataMap data = DataParser.parserJson(FileUtils.getData("query-single.json"));
		CommandExecutor exe = new CommandExecutor(data);
		DataMap tableDataMap = exe.execute();
		
		List<DataMap> list = (List<DataMap>) tableDataMap.get("ORG");
		assertTrue(list.size()==1);
		DataMap org  = (DataMap) list.get(0).get("T_ORG");
		DataMap parent = (DataMap) org.get("parent_id_");
		assertEquals(org.get("id_"), "00001");
		assertEquals(parent.get("id_"), "0");
	}
	
	@Test
	public void test_query2() throws Exception{
		DataMap data = DataParser.parserJson(FileUtils.getData("query-single-fk.json"));
		CommandExecutor exe = new CommandExecutor(data);
		DataMap tableDataMap = exe.execute();
		
		List<DataMap> list = (List<DataMap>) tableDataMap.get("ORG");
		assertTrue(list.size()==1);
		DataMap org  = (DataMap) list.get(0).get("T_ORG");
		DataMap parent = (DataMap) org.get("parent_id_");
		assertEquals(org.get("id_"), "00001");
		//assertEquals(parent.get("id_"), "0");
		assertEquals(parent.get("name_"), "根机构");
	}
	@Test
	public void test_query3() throws Exception{
		DataMap data = DataParser.parserJson(FileUtils.getData("query-one2many.json"));
		CommandExecutor exe = new CommandExecutor(data);
		DataMap tableDataMap = exe.execute();
		
		List<DataMap> list = (List<DataMap>) tableDataMap.get("ORG");
		assertTrue(list.size()==1);
		DataMap org  = (DataMap) list.get(0).get("T_ORG");
		DataMap parent = (DataMap) org.get("parent_id_");
		assertEquals(org.get("id_"), "00001");
		//assertEquals(parent.get("id_"), "0");
		assertEquals(parent.get("name_"), "根机构");
		
		List<DataMap> poses = (List<DataMap>) org.get("T_POSITION");
		assertTrue(poses.size()>0);
		DataMap t = (DataMap) poses.get(0).get("ORG_ID_");
		assertEquals("00001",t.get("ID_"));
	}
	
	@Test
	public void test_queryref() throws Exception{
		DataMap data = DataParser.parserJson(FileUtils.getData("query-ref.json"));
		CommandExecutor exe = new CommandExecutor(data);
		DataMap tableDataMap = exe.execute();
		
		List<DataMap> list = (List<DataMap>) tableDataMap.get("ORG");
		assertTrue(list.size()==1);
		DataMap org  = (DataMap) list.get(0).get("T_ORG");
		DataMap parent = (DataMap) org.get("parent_id_");
		assertEquals(org.get("id_"), "0");
	}
}
