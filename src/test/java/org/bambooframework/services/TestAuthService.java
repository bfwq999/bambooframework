package org.bambooframework.services;

import org.bambooframework.entity.Org;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class TestAuthService extends TestBaseService {

	@Autowired
	AuthService authService;
	
	@Test
	@Transactional
	public void test_insertOrg(){
		Org org = new Org();
		org.setParentId("0");
		org.setName("测试");
		org.setCode("123");
		authService.insertOrg(org);
		
		assertEquals("00003", org.getId());
		
		org = new Org();
		org.setParentId("00003");
		org.setName("测试");
		org.setCode("123");
		authService.insertOrg(org);
		
		assertEquals("000030001", org.getId());
		
		org = authService.get(Org.class,org.getId());
		assertEquals("测试", org.getName());
		assertNotNull(org.getSort());
		
		org.setShortName("测试");
		authService.updateOrg(org);
	}
}
