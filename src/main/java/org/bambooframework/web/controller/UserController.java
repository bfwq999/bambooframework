package org.bambooframework.web.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bambooframework.common.Pagination;
import org.bambooframework.common.QueryResult;
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
public class UserController  {
	
	@Autowired
	AuthService authService;
	
	@RequiresPermissions("user:query")
	@RequestMapping(value="/users",method=RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public QueryResult listUser(QueryParam queryParam,Pagination pagination){
		List<User> rows = authService.find(User.class, queryParam.getInnerMap(), pagination);
		return new QueryResult(rows, pagination);
	}
	
	@RequiresPermissions("user:query")
	@RequestMapping(value="/users/{userId}",method=RequestMethod.GET)
	public User getUser(@PathVariable String userId){
		return authService.get(User.class, userId);
	}
	
	@RequiresPermissions("user:add")
	@ResponseStatus(value=HttpStatus.CREATED)
	@RequestMapping(value="/users",method=RequestMethod.POST)
	public User addUser(@RequestBody User user){
		authService.insert(user);
		return user;
	}
	
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value="/users/{userId}",method=RequestMethod.PUT)
	public User updateOrg(@PathVariable("userId") String userId,@RequestBody User user){
		authService.update(user);
		return user;
	}
	
	
	@RequiresPermissions("user:delete")
	@RequestMapping(value="/users/{userId}",method=RequestMethod.DELETE)
	public void deleteUser(String userId){
		authService.delete(User.class,userId);
	}
}
