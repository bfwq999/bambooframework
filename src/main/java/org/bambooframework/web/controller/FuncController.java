package org.bambooframework.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bambooframework.common.CodeConstants;
import org.bambooframework.common.CommonUtils;
import org.bambooframework.common.Pagination;
import org.bambooframework.common.QueryResult;
import org.bambooframework.entity.Func;
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

/**
 * 权限管理
 * 
 * @author lei
 * @date 2015年9月12日
 * @Description:
 */
@RestController
public class FuncController {

	@Autowired
	AuthService authService;

	@RequestMapping(value = "/funcs", method = RequestMethod.GET)
	public QueryResult list(QueryParam queryParam, Pagination pagination) {
		Map<String,Object> map = queryParam.getInnerMap();
		if (queryParam.get("type") != null) {
			// 将type转成数组
			queryParam
					.put("type", ((String) queryParam.get("type")).split(","));
		}
		List<Func> funcs = authService.find(Func.class,
				queryParam.getInnerMap(), pagination);
		return new QueryResult(funcs,pagination);
	}

	/**
	 * 查询用户关联的菜单
	 * 
	 * @author lei
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	@RequestMapping(value = "/users/{userId}/menu", method = RequestMethod.GET)
	public List<Func> listMenuOfUser() {
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		List<Func> funcs;
		if(CommonUtils.isAdminAccount(username)){
			funcs = authService.findAllMenus();
		}else{
			funcs = authService.findMenusOfUser(username);
		}
		return funcs;

	}
	
	@RequiresPermissions("func:add")
	@ResponseStatus(value=HttpStatus.CREATED)
	@RequestMapping(value="/funcs",method=RequestMethod.POST)
	public Func addFunc(Func func){
		authService.insert(func);
		return func;
	}
	
	@RequiresPermissions("func:edit")
	@RequestMapping(value="/funcs/{funcId}",method=RequestMethod.PUT)
	public Func updateOrg(@PathVariable("funcId") String funcId,@RequestBody Func func){
		authService.update(func);
		return func;
	}
}
