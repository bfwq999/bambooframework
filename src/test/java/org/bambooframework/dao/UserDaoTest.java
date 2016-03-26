package org.bambooframework.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bambooframework.entity.User;
import org.bambooframework.utils.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends TestBase {
	@Autowired
	IUserDao employeeDao;
	
	@Test
	public void test_crud(){
		User employee = new User();
		employee.setName("测试");
		employee.setLoginName("test");
		employee.setPassword("1");
		employeeDao.insert(employee);
		
		employee = employeeDao.get(employee.getId());
		assertEquals("测试", employee.getName());
		
		employee.setEmail("abc@test.com");
		employeeDao.update(employee);
	}
	
	@Test
	public void test_selectUsers(){
		Map<String, String> condition = new HashMap<String, String>();
		List<User> employees = employeeDao.selectUsers(condition);
		condition.put("name", "测试");
		employees = employeeDao.selectUsers(condition);
		
		assertEquals(22, employees.size());
	}
	
	@Test
	public void test_getUserByName(){
		User user = employeeDao.getUserByLoginName("admin");
		assertNotNull(user);
	}
}
