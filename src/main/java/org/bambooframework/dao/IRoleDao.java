package org.bambooframework.dao;

import java.util.Set;

import org.bambooframework.common.IBaseDao;
import org.bambooframework.entity.Role;

/**
 * 角色表dao
 * @author lei
 * @date 2015年9月12日
 * @Description:
 */
public interface IRoleDao extends IBaseDao<Role> {

	/**
	 * 查询用户角色
	 * @author lei
	 * @param userId
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	Set<String> findRolesOfUser(String userId);
	
	/**
	 * 查询用户所有角色
	 * @author lei
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	Set<String> findAllRoles();
}
