package org.bambooframework.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bambooframework.common.CodeConstants;
import org.bambooframework.entity.User;
import org.bambooframework.entity.Func;
import org.bambooframework.entity.Org;
import org.bambooframework.utils.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FuncDaoTest extends TestBase {
	@Autowired
	IFuncDao funcDao;
	
	@Test
	public void test_crud(){
		Func func = new Func();
		func.setCode("user:query");
		func.setName("用户管理");
		funcDao.insert(func);
		
		func = funcDao.get(func.getId());
		assertEquals("用户管理", func.getName());
		
		func.setUrl("test.html");
		funcDao.update(func);
	}
	
	@Test
	public void test_find(){
		Map<String,Object>  condition = new HashMap<String,Object>();
		condition.put("type", new String[]{CodeConstants.FUNC_TYPE_MENU});
		List<Func> funcs = funcDao.find(condition);
		assertTrue(funcs.size()>0);
	}
}
