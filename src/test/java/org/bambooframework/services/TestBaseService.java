package org.bambooframework.services;

import java.util.HashMap;
import java.util.List;

import org.bambooframework.entity.Org;
import org.bambooframework.entity.User;
import org.bambooframework.utils.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBaseService extends TestBase {
	@Autowired
	AuthService authSerivce;
	
	@Test
	public void test_getDaoInterface(){
	//测试获取接口类
		User user = authSerivce.get(User.class, 1);
		assertNotNull(user);
		
		List<Org> orgs = authSerivce.find(Org.class, new HashMap<String,Object>());
		assertTrue(orgs.size()>0);
	}
	
	
	
}
