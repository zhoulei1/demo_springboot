package com.example.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.model.User;
import com.example.service.UserService;
import com.example.vo.CustomDataModel;
import com.example.vo.UserVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@Controller 
@RequestMapping
@Api(tags="c",description="c")
public class HelloController3 {

	@Autowired
	UserService userService;
	
	@ApiOperation(value="获取xxx用户信息",notes = "first api",response=UserVo.class,httpMethod= "POST")
	@ApiImplicitParams(value={
			@ApiImplicitParam(name="name",value="名称",required=true,paramType ="query",dataType="String"),
			@ApiImplicitParam(name="date",value="日期",required=true,paramType ="query",dataType="java.util.Date")
	})
	//@ApiModelProperty  在类中使用
	//@ApiParam 单个进行说明
	@RequestMapping(value = "/say2")
	@ResponseBody
	public UserVo say() {
		UserVo vo = new UserVo();
		return vo;
	}
	
}
