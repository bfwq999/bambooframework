package org.bambooframework.web.controller;

import java.util.List;

import org.bambooframework.common.QueryResult;
import org.bambooframework.entity.Position;
import org.bambooframework.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 岗位管理
 * @author lei
 * @date 2015年9月18日
 * @Description:
 */
@RestController
public class PositionController {
	@Autowired
	AuthService authSerivce;
	
	
	@RequestMapping(value="/positions",method=RequestMethod.GET)
	public QueryResult list(){
		List<Position> positions = authSerivce.find(Position.class,null);
		return new QueryResult(positions,null);
	}
	
	@RequestMapping(value="/positions/{id}",method=RequestMethod.GET)
	public Position get(@PathVariable Integer id){
		return authSerivce.get(Position.class, id);
	}
	
	@RequestMapping(value="/positions",method=RequestMethod.POST)
	public Position insert(@RequestBody Position position){
		authSerivce.insert(position);
		return position;
	}
	
	@RequestMapping(value="/positions/{id}",method=RequestMethod.PUT)
	public Position update(@PathVariable String id,@RequestBody Position position){
		authSerivce.update(position);
		return position;
	}
	
	@RequestMapping(value="/positions/{id}",method=RequestMethod.DELETE)
	public void update(@PathVariable String id){
		authSerivce.delete(Position.class, id);
	}
	
}
