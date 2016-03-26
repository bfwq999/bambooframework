package org.bambooframework.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.bambooframework.common.Pagination;
import org.bambooframework.entity.User;
import org.bambooframework.utils.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;

public class PaginationTest extends TestBase {
	
	@Autowired
	IUserDao employeeDao;
	
	@Test
	@DatabaseSetup("classpath:db/UserData.xml")
	public void test_selectUsersPagination(){
		//分页
		Map<String,String> condition =  new HashMap<String,String>();
		Pagination pagination = new Pagination();
		pagination.setPageSize(10);
		pagination.setPageNo(2);
		List<User> users = employeeDao.selectUsers(condition,pagination);
		assertEquals(10, users.size());
		assertEquals(24, pagination.getTotal());
	}
	
	@Test
	@DatabaseSetup("classpath:db/UserData.xml")
	public void test_selectUsersNoPagination(){
		//不分页
		Map<String,String> condition =  new HashMap<String,String>();
		List<User> users = employeeDao.selectUsers(condition);
		assertEquals(24, users.size());
	}
	
	@Test
	@DatabaseSetup("classpath:db/UserData.xml")
	public void test_selectUserPaginationInMem(){
		//mybatis自带的内存分页
		Map<String,String> condition =  new HashMap<String,String>();
		RowBounds rowBounds = new RowBounds(10,10);
		List<User> users = employeeDao.selectUsers(condition,rowBounds);
		assertEquals(10, users.size());
	}
}
