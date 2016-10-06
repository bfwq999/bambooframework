package org.bambooframework.common;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseBean {
	
	String uuid = UUID.randomUUID().toString();

	public String getUuid() {
		if(getId()!=null){
			return getId().toString();
		}
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public abstract Serializable getId();
}
