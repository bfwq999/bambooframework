package org.bambooframework.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.bambooframework.common.IBaseDao;
import org.bambooframework.common.Pagination;
import org.bambooframework.entity.Org;

public interface IOrgDao extends IBaseDao<Org> {
	
	public List<Org> selectOrgs(Map<String,Object> object);
	
	public List<Org> selectOrgs(Map<String,Object> object,Pagination pagination);
	
	/**
	 * 更新机构排序
	 * @author lei
	 * @param orgid 机构编码
	 * @param sort 排序
	 * @date 2015年11月8日
	 * @Description:
	 */
	public void updateOrgSort(@Param("id") String orgid,@Param("sort") int sort);
	
	/**
	 * 置机构及其子机构为删除状态
	 * @author lei
	 * @param id
	 * @date 2015年11月8日
	 * @Description:
	 */
	public void deleteCascade(@Param("id") String orgid);
	
	/**
	 * 获取父机构同是parentId的所有机构的最大排序
	 * @author lei
	 * @param parentId 
	 * @return
	 * @date 2015年11月8日
	 * @Description:
	 */
	public Integer getMaxSort(@Param("parentId") String parentId);
	
	/**
	 * 获取父机构同是parentId的所有机构的最大id
	 * @author lei
	 * @param parentId
	 * @return
	 * @date 2015年11月8日
	 * @Description:
	 * 	获取同级中最大的id
	 */
	public String getMaxId(@Param("parentId") String parentId);
}
