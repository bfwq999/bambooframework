package org.bambooframework.dao;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDaoEngines {

	@BeforeClass
	public static void initDaoEngine() {
		DaoEngines.init();
	}

	@AfterClass
	public static void destroyDaoEngine() {
		DaoEngines.destroy();
	}

	@Test
	public void testDaoEngineInfo() {
		List<DaoEngineInfo> daoEngineInfos = DaoEngines.getDaoEngineInfos();
		Assert.assertEquals(1, daoEngineInfos.size());
		
		DaoEngineInfo daoEngineInfo = daoEngineInfos.get(0);
		Assert.assertNull(daoEngineInfo.getException());
		Assert.assertNotNull(daoEngineInfo.getName());
		Assert.assertNotNull(daoEngineInfo.getResourceUrl());

	    DaoEngine daoEngine = DaoEngines.getDaoEngine(DaoEngines.NAME_DEFAULT);
	    Assert.assertNotNull(daoEngine);
	}
}
