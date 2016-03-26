package org.bambooframework.services;

import org.bambooframework.entity.User;

public interface IUserService {
	public User getUser(String userId);
	
	public void saveUser(User user);
	
	public void deleteUser(String userId);
}
