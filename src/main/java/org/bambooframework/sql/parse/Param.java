package org.bambooframework.sql.parse;

import java.util.List;

public interface Param {
	
	public void setParent(Param param);
	
	public void setName(String name);
	/**
	 * 名称
	 * @author lei
	 * @return
	 * @date 2016年6月24日
	 * @Description:
	 */
	public String getName();
	
	/**
	 * 父参数名
	 * @author lei
	 * @return
	 * @date 2016年6月24日
	 * @Description:
	 */
	public Param parent();
	
	/**
	 * 路径
	 * @author lei
	 * @return
	 * @date 2016年6月24日
	 * @Description:
	 */
	public List<Param> getPaths();
	
	/**
	 * 依赖
	 * @author lei
	 * @param ref
	 * @date 2016年6月24日
	 * @Description:
	 */
	public void addDependBy(Param ref);
	
	/**
	 * 是否存在依赖
	 * @author lei
	 * @param ref
	 * @return
	 * @date 2016年6月24日
	 * @Description:
	 */
	public boolean dependBy(Param ref);
	
	/**
	 * 获取参数
	 * @author lei
	 * @param name
	 * @return
	 * @date 2016年6月24日
	 * @Description:
	 */
	public Object get(String name);
	
	/**
	 * 获取第几个
	 * @author lei
	 * @param index
	 * @return
	 * @date 2016年6月24日
	 * @Description:
	 */
	public DataMap get(int index);
	
}
