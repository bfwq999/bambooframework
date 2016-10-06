package org.bambooframework.auth.shiro;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.bambooframework.common.CodeConstants;
import org.bambooframework.dao.IFuncDao;
import org.bambooframework.dao.IUserDao;
import org.bambooframework.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	IFuncDao funcDao;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//		Set<String> roles = new HashSet<String>();
//		authorizationInfo.setRoles(roles);
		String userName = (String)principals.getPrimaryPrincipal();
		Set<String> permissions;
		if("admin".equals(userName)){
			Map<String,Object> condition = new HashMap<String, Object>();
			condition.put("type", CodeConstants.FUNC_TYPE_ACT);
			 permissions = funcDao.findAllPermissions();
		}else{
			 permissions = funcDao.findPermissionsOfUser(userName);
		}
		
		authorizationInfo.setStringPermissions(permissions);
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		User user = userDao.getUserByLoginName(username);
		if(user == null){
			throw new UnknownAccountException();
		}
		return new SimpleAuthenticationInfo(username, user.getPassword(), getName());
	}

}
