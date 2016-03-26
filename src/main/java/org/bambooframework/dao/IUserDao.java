package org.bambooframework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.bambooframework.common.IBaseDao;
import org.bambooframework.common.Pagination;
import org.bambooframework.entity.User;

public interface IUserDao extends IBaseDao<User> {
	
	List<User> selectUsers(Map<String,String> condition);	
	
	List<User> selectUsers(Map<String,String> condition,Pagination pagination);	
	
	List<User> selectUsers(Map<String,String> condition,RowBounds rowBounds);	
	
	/**
	 * 根据用户名获取用户
	 * @author lei
	 * @param username
	 * @return
	 * @date 2015年9月5日
	 * @Description:
	 * 	登录时校验用
	 */
	public User getUserByLoginName(String username);
}
