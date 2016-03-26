package org.bambooframework.dao;

import org.bambooframework.entity.Position;
import org.bambooframework.entity.Role;
import org.bambooframework.utils.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleDaoTest extends TestBase {
	@Autowired
	IRoleDao roleDao;
	
	@Test
	public void test_crud(){
		Role role = new Role();
		role.setName("测试");
		
		roleDao.insert(role);
		
		role = roleDao.get(role.getId());
		role.setComment("测试");
		
		roleDao.update(role);
		
	}
}
