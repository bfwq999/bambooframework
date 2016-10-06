package org.bambooframework.services.impl;

import org.bambooframework.dao.IUserDao;
import org.bambooframework.entity.User;
import org.bambooframework.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	IUserDao userDao;
	

	@Override
	public User getUser(String userId) {
		return userDao.get(userId);
	}

	@Override
	public void saveUser(User user) {
		if(StringUtils.isEmpty(user.getId())){
			userDao.insert(user);
		}else{
			userDao.update(user);
		}
	}

	@Override
	public void deleteUser(String userId) {
		userDao.delete(userId);
	}
}
