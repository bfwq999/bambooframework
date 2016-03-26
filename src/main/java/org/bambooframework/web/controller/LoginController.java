package org.bambooframework.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.bambooframework.common.CommonUtils;
import org.bambooframework.common.HttpStatusException;
import org.bambooframework.entity.Func;
import org.bambooframework.entity.User;
import org.bambooframework.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 
 * 登录
 * @author lei
 * @date 2015年9月3日
 * @Description:
 */
@RestController
public class LoginController {
	@Autowired
	AuthService authService;
	
	/**
	 * 登录
	 * @author lei
	 * @param req
	 * @param userName
	 * @param password
	 * @return
	 * @date 2015年9月4日
	 * @Description:
	 */
	@RequestMapping(value="/login")
	public void login(HttpServletRequest req,String userName,String password,boolean rememberMe){
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
		token.setRememberMe(rememberMe);
		subject.login(token);
	}
	
	/**
	 * 获取当前登录用户
	 * @author lei
	 * @return
	 * @date 2015年9月4日
	 * @Description:
	 */
	@RequestMapping(value="/authentication")
	public Map<String,Object> getAuthenticationInfo(){
		Map<String,Object> map = new HashMap<String,Object> ();
		Subject subject = SecurityUtils.getSubject();
		if(!subject.isAuthenticated()){
			throw new HttpStatusException(HttpServletResponse.SC_UNAUTHORIZED, "没有登录");
		}
		String username = (String)subject.getPrincipal();
		User user = new User();
		user.setLoginName(username);
		map.put("user", user);
		
		Set<String> permissions;
		List<Func> menus;
		if(CommonUtils.isAdminAccount(username)){
			permissions = authService.findAllPermissions();
			menus = authService.findAllMenus();
		}else{
			permissions = authService.findPermissionsOfUser(username);
			menus = authService.findMenusOfUser(username);
		}
		map.put("permisssions", permissions);
		map.put("menus", menus);
		return map;
	}
	
	@RequestMapping(value="/logout")
	public void logout(){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
	}
}
