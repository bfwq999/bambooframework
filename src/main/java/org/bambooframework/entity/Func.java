package org.bambooframework.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bambooframework.common.BaseBean;

public class Func extends BaseBean implements Serializable {

	private static final long serialVersionUID = -5501431910297146689L;

	private Integer id;
	
	/** 父菜单id */
	private Integer parentId;

	/** 编码,权限key,如：user:add */
	private String code;

	/** 名称 */
	private String name;

	/** */
	private String url;

	/** 类型：M：菜单；A：权限 */
	private String type;

	/** 状态：0：正常；1：停用； */
	private String status;

	/** 排序 */
	private Integer sort;
	
	private List<Func> children = new ArrayList<Func>(0);

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<Func> getChildren() {
		return children;
	}

	public void setChildren(List<Func> children) {
		this.children = children;
	}
}
