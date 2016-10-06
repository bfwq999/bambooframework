package org.bambooframework.entity;

import java.io.Serializable;

import org.bambooframework.common.BaseBean;

public class Position extends BaseBean implements Serializable {

	private static final long serialVersionUID = -2223735767190104454L;

	private Integer id;
	
	/** 机构ID */
	private String orgId;
	
	/** 岗位名称 */
	private String name;
	
	/** 岗位描述 */
	private String comment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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
