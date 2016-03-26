package org.bambooframework.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bambooframework.common.Pagination;
import org.bambooframework.common.QueryResult;
import org.bambooframework.entity.Role;
import org.bambooframework.entity.User;
import org.bambooframework.services.AuthService;
import org.bambooframework.web.vo.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
	
	@Autowired
	AuthService authService;
	
	/**
	 * 根据主键获取机构信息
	 * @author lei
	 * @param roleid
	 * @return
	 * @date 2015年9月19日
	 * @Description:
	 */
	@RequestMapping(value="/role/{roleid}",method=RequestMethod.GET)
	public Role get(@PathVariable("roleid") String roleid){
		return authService.get(Role.class, roleid);
	}
	
	/**
	 * 查询
	 * @author lei
	 * @param queryParam
	 * @param pagination
	 * @return
	 * @date 2015年9月15日
	 * @Description:
	 */
	@RequestMapping(value="/roles",method=RequestMethod.GET)
	public QueryResult list(QueryParam queryParam,Pagination pagination){
		List<Role> roles =  authService.find(Role.class, queryParam.getInnerMap(), pagination);
		return new QueryResult(roles, pagination);
	}
	
	@RequestMapping(value="/roles/{roleId}/users",method=RequestMethod.GET)
	public QueryResult findEmployeesOfRole(@PathVariable("roleId") String roleId,Pagination pagination){
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("roleId", roleId);
		List<User> employees = authService.find(User.class,condition, pagination);
		return new QueryResult(employees,pagination);
	}
	
	@RequiresPermissions("role:add")
	@ResponseStatus(value=HttpStatus.CREATED)
	@RequestMapping(value="/roles",method=RequestMethod.POST)
	public Role insertRole(@RequestBody Role role){
		authService.insert(role);
		return role;
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value="/roles/{roleid}",method=RequestMethod.PUT)
	public Role updateRole(@PathVariable("roleid") String roleid,@RequestBody Role role){
		authService.update(role);
		return role;
	}
	@RequiresPermissions("role:delete")
	@RequestMapping(value="/roles/{roleid}",method=RequestMethod.DELETE)
	public void deleteRole(@PathVariable("roleid") String roleid){
		authService.delete(Role.class, roleid);
	}
}
