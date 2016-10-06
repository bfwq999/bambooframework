package org.bambooframework.dao;

import java.util.List;
import java.util.Map;

import org.bambooframework.common.IBaseDao;
import org.bambooframework.entity.Property;

public interface IPropertyDao extends IBaseDao<Property> {

	List<Property> selectProperties(Map<String,String> condition);
}
