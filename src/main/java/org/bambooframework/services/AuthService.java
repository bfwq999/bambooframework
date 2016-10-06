package org.bambooframework.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bambooframework.common.BaseService;
import org.bambooframework.common.CodeConstants;
import org.bambooframework.common.CommonUtils;
import org.bambooframework.dao.IFuncDao;
import org.bambooframework.dao.IOrgDao;
import org.bambooframework.entity.Func;
import org.bambooframework.entity.Org;
import org.bambooframework.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends BaseService {
	
	@Autowired
	IFuncDao funcDao;
	
	@Autowired
	IOrgDao orgDao;
	
	/**
	 * 查询用户权限
	 * @author lei
	 * @param userId
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	public Set<String> findPermissionsOfUser(String userId){
		return funcDao.findPermissionsOfUser(userId);
	}
	
	/**
	 * 查询所有权限
	 * @author lei
	 * @param userId
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	public Set<String> findAllPermissions(){
		return funcDao.findAllPermissions();
	}
	
	/**
	 * 用户有权操作的菜单
	 * @author lei
	 * @param userId
	 * @return
	 * @date 2015年9月12日
	 * @Description:
	 */
	public List<Func> findMenusOfUser(String userName){
		return funcDao.findMenusOfUser(userName);
	}
	
	public List<Func> findAllMenus(){
		return funcDao.findAllMenus();
	}

	/**
	 * 插入机构
	 * @author lei
	 * @param org
	 * @date 2015年11月8日
	 * @Description:
	 */
	public void insertOrg(Org org){
		if(CommonUtils.isEmpty(org.getParentId())){
			org.setParentId(null);
		}
		//id 格式为00010001,第增加一级,加四位
		String maxId = orgDao.getMaxId(org.getParentId());
		String pre = org.getParentId()==null?"":org.getParentId();
		if(maxId==null){
			maxId = pre+"0001";
		}else{
			maxId = pre+String.format("%04d", Integer.parseInt(maxId.substring(maxId.length()-4))+1);   
		}
		org.setId(maxId);
		Integer sort = orgDao.getMaxSort(org.getParentId());
		if(sort == null){
			sort = 1;
		}else{
			sort += 1;
		}
		org.setSort(sort);
		org.setStatus(CodeConstants.ORG_STATUS_NORMAL);
		this.insert(org);
	}
	/**
	 * 删除机构及子机构
	 * @author lei
	 * @param orgid
	 * @date 2015年11月7日
	 * @Description:
	 */
	public void deleteOrg(String orgid){
		orgDao.deleteCascade(orgid);
	}
	
	/**
	 * 更新机构
	 * @author lei
	 * @param org
	 * @date 2015年11月8日
	 * @Description:
	 */
	public void updateOrg(Org org){
		//先更新排序
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("parentId", org.getParentId());
		condition.put("gtSort", org.getSort()-1);
		List<Org> orgs = this.find(Org.class, condition);
		for(int i=0; i<orgs.size(); i++){
			Org o = orgs.get(i);
			orgDao.updateOrgSort(o.getId(), org.getSort()+1+i);
		}
		this.update(org);
	}
	
	
}
