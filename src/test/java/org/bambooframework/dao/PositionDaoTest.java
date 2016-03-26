package org.bambooframework.dao;

import java.util.HashMap;
import java.util.List;

import org.bambooframework.common.Pagination;
import org.bambooframework.entity.Position;
import org.bambooframework.utils.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PositionDaoTest extends TestBase {
	@Autowired
	IPositionDao positionDao;
	
	@Test
	public void test_crud(){
		Position position = new Position();
		position.setName("测试");
	
		positionDao.insert(position);
		
		position = positionDao.get(position.getId());
		position.setComment("测试");
		positionDao.update(position);
	}
	
	@Test
	public void test_find(){
		Position position = new Position();
		position.setName("测试");
	
		positionDao.insert(position);
		
		List<Position> poses  = positionDao.find(new HashMap<String, Object>(),new Pagination(1, 10));
		assertTrue(poses.size()>0);
	}
	
}
