package org.bambooframework.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TestBase {
	@BeforeClass
	public static void initDaoEngine() {
		DaoEngines.init();
	}

	@AfterClass
	public static void destroyDaoEngine() {
		DaoEngines.destroy();
	}
}
