package org.bambooframework.entity;

import java.io.Serializable;

import org.bambooframework.common.BaseBean;

public class Role extends BaseBean implements Serializable {

	private static final long serialVersionUID = 3629632276236656453L;

	private Integer id;
	/** 角色编码 */
	private String code;

	/** 角色名称 */
	private String name;

	/** 描述 */
	private String comment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
