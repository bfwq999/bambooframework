package org.bambooframework.web.controller;

import org.bambooframework.sql.CommandExecutor;
import org.bambooframework.sql.parse.DataMap;
import org.bambooframework.sql.parse.DataParser;
import org.bambooframework.sql.parse.RootParamMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** 
 * 登录
 * @author lei
 * @date 2015年9月3日
 * @Description:
 */
@RestController
public class DefaultController {
	@RequestMapping(value="/get")
	@ResponseBody
	public DataMap get(@RequestBody String data){
		RootParamMap dataMap = DataParser.parserJson(data);
		CommandExecutor exe = new CommandExecutor(dataMap);
		DataMap dm= exe.execute();
		return (DataMap) dm.values().iterator().next();
	}
}
