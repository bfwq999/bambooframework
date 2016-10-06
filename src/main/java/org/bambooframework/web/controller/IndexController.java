package org.bambooframework.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping(value={"","index"})
	public void toIndex(HttpServletRequest req,HttpServletResponse resp,String url){
		try{
			Subject subject = SecurityUtils.getSubject();
			if(subject.isAuthenticated()){
				req.getRequestDispatcher("/main.html?url="+url).forward(req, resp);
			}else{
				resp.sendRedirect("/login.html"+(url==null?("?url="+url):""));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
