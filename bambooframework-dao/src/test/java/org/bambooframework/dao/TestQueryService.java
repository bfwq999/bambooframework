package org.bambooframework.dao;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestQueryService {
	@BeforeClass
	public static void initDaoEngine() {
		DaoEngines.init();
	}

	@AfterClass
	public static void destroyDaoEngine() {
		DaoEngines.destroy();
	}
	@Test
	public void testGet(){
		Map data = DaoEngines.getDefaultDaoEngine().getQueryService().get("T_ORG", 1);
	}
}
