package com.example.demo.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.model.User;
import com.example.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@Controller 
@RequestMapping
@Api(tags="test",description="hello test")
public class HelloController {

	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/loginsuccess",method=RequestMethod.POST)
	public String addNewUser () {
		System.out.println("login success");
		return "hello";
	}
	
	@GetMapping("/admin")
	@ResponseBody
//	@RolesAllowed({"admin","user"})
	public String admin() {
	    return "admin-page";
	}
	@RequestMapping(value = "/user")
//	@RolesAllowed({"user"})
	@ResponseBody
	public String adminPage() {
	    return "user-page";
	}
	
	@ApiOperation(value="获取用户信息",notes = "first api",responseReference="http://ip.jsontest.com",httpMethod= "POST")
	@ApiImplicitParams(value={
			@ApiImplicitParam(name="name",value="名称",required=true,paramType ="query",dataType="String"),
			@ApiImplicitParam(name="date",value="日期",required=true,paramType ="query",dataType="java.util.Date")
	})
	//@ApiModelProperty  在类中使用
	//@ApiParam 单个进行说明
	@RequestMapping(value = "/say")
	@ResponseBody
	public User say(String name) {
		return userService.findByUsername(name);
	}
	
	@RequestMapping(value = "/download")
	@ResponseBody
	public void download(HttpServletResponse response) {
		response.setContentType("application/octet-stream");
		response.addHeader("Content-Disposition",
				"attachment;filename=222.txt");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.write("我爱中国");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
