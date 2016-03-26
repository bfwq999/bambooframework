package org.bambooframework.dao;

import java.util.List;
import java.util.Set;

import org.bambooframework.common.IBaseDao;
import org.bambooframework.entity.Func;

public interface IFuncDao extends IBaseDao<Func> {
	
	/**
	 * 用户菜单
	 * @author lei
	 * @param userName 账户名
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	List<Func> findMenusOfUser(String userName);
	
	/**
	 * 查询所有菜单
	 * @author lei
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	List<Func> findAllMenus();
	
	/**
	 * 查询用户权限
	 * @author lei
	 * @param userName 账户名
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	Set<String> findPermissionsOfUser(String userName);
	
	/**
	 * 查询所有权限
	 * @author lei
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	Set<String> findAllPermissions();
}

