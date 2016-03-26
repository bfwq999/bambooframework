package org.bambooframework.entity;

import java.io.Serializable;

import org.bambooframework.common.BaseBean;

public class Org extends BaseBean implements Serializable {

	private static final long serialVersionUID = 6247290343324135848L;

	/** 机构ID */
	private String id;
	
	/** 父机构id */
	private String parentId;
	
	/** 机构名称 **/
	private String name;
	
	/** 机构编码 */
	private String code;
	
	/** 排序 */
	private Integer sort;
	
	/** 机构简称 */
	private String shortName;
	
	/** 状态 */
	private String status;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Org [id=" + id + ", parentId=" + parentId + ", name=" + name
				+ ", code=" + code + ", sort=" + sort + ", shortName="
				+ shortName + ", status=" + status + "]";
	}
}
