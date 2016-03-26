package org.bambooframework.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bambooframework.common.Pagination;
import org.bambooframework.common.QueryResult;
import org.bambooframework.entity.Org;
import org.bambooframework.entity.Position;
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
public class OrgController {
	
	@Autowired
	AuthService authService;
	
	/**
	 * 根据主键获取机构信息
	 * @author lei
	 * @param orgid
	 * @return
	 * @date 2015年9月19日
	 * @Description:
	 */
	@RequestMapping(value="/org/{orgid}",method=RequestMethod.GET)
	public Org get(@PathVariable("orgid") String orgid){
		return authService.get(Org.class, orgid);
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
	@RequestMapping(value="/orgs",method=RequestMethod.GET)
	public QueryResult list(QueryParam queryParam,Pagination pagination){
		List<Org> orgs =  authService.find(Org.class, queryParam.getInnerMap(), pagination);
		return new QueryResult(orgs, pagination);
	}
	
	@RequestMapping(value="/orgs/{orgId}/positions",method=RequestMethod.GET)
	public QueryResult findPositionsOfOrg(@PathVariable("orgId") String orgId,Pagination pagination){
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("orgId", orgId);
		List<Position> positions = authService.find(Position.class,condition, pagination);
		return new QueryResult(positions,pagination);
	}
	@RequestMapping(value="/orgs/{orgId}/users",method=RequestMethod.GET)
	public QueryResult findEmployeesOfOrg(@PathVariable("orgId") String orgId,Pagination pagination){
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("orgId", orgId);
		List<User> employees = authService.find(User.class,condition, pagination);
		return new QueryResult(employees,pagination);
	}
	
	@RequiresPermissions("org:add")
	@ResponseStatus(value=HttpStatus.CREATED)
	@RequestMapping(value="/orgs",method=RequestMethod.POST)
	public Org insertOrg(@RequestBody Org org){
		authService.insertOrg(org);
		return org;
	}
	
	@RequiresPermissions("org:edit")
	@RequestMapping(value="/orgs/{orgid}",method=RequestMethod.PUT)
	public Org updateOrg(@PathVariable("orgid") String orgid,@RequestBody Org org){
		authService.updateOrg(org);
		return org;
	}
	@RequiresPermissions("org:delete")
	@RequestMapping(value="/orgs/{orgid}",method=RequestMethod.DELETE)
	public void deleteOrg(@PathVariable("orgid") String orgid){
		authService.deleteOrg(orgid);
	}
}
