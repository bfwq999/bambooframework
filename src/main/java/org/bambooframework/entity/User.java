package org.bambooframework.entity;

import java.io.Serializable;

import org.bambooframework.common.BaseBean;

public class User extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 291558477581543471L;

	/** id */
	private Integer id;
	
	/** 机构编码 */
	private String orgId;
	
	/** 员工编号 */
	private String code;
	
	/** 姓名 */
	private String name;
	
	/** 性别 */
	private String sex;
	
	/** 邮箱 */
	private String email;
	
	/** 电话 */
	private String telephone;
	
	/** 手机 */
	private String cellphone;
	
	/** 状态，0：正常；1：停用 */
	private String status;
	
	/** 密码 */
	private String password;
	
	/** 账户名 */
	private String loginName;
	
	//扩展
	private String orgName;

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}
