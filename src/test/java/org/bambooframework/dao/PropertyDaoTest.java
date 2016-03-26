package org.bambooframework.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.bambooframework.entity.Property;
import org.bambooframework.utils.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertyDaoTest extends TestBase  {

	@Autowired
	IPropertyDao propertyDao;
	
	@Test
	@Transactional
	public void test_crud(){
		Property property = propertyDao.get("nextid");
		assertNotNull(property);
		property.setValue("2");
		propertyDao.update(property);
	}
	
	@Test
	public void test_selectProperties(){
		Map<String,String> condition = new HashMap<String, String>();
		List<Property> properties = propertyDao.selectProperties(condition);
		assertEquals(1, properties.size());
	}
	
	

}
