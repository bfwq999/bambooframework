package org.bambooframework.entity;

import java.io.Serializable;

import org.bambooframework.common.BaseBean;

public class Property extends BaseBean implements Serializable {
	private static final long serialVersionUID = -7543661428668144975L;
	
	private String id;
	private String value;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
