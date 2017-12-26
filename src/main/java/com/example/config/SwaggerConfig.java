package com.example.config;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Configuration
//@EnableSwagger2 // 必须存在 扫描的API Controller package name 也可以直接扫描class
				// (basePackageClasses)
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		List<ResponseMessage> responseMessages = new ArrayList<>();
/*		responseMessages.add(new ResponseMessage(HttpStatus.SC_INTERNAL_SERVER_ERROR, "操作失败", null,
				new HashMap<String, ModelReference>(0)));*/
		Set<String> produces = new HashSet<>();
		produces.add("application/json");
		Set<String> consumes = new HashSet<>();
		produces.add("application/json");
		produces.add("application/x-www-form-urlencoded");
		Docket build = new Docket(DocumentationType.SWAGGER_2)
				.directModelSubstitute(java.util.Date.class,Date.class)
				.produces(produces)
				.consumes(consumes)
				//.ignoredParameterTypes(UserVo.class)
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, responseMessages)
				.globalResponseMessage(RequestMethod.POST, responseMessages)
				.apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.example.demo.controller")).paths(PathSelectors.any())
				.build();
		return build;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("api 接口说明").description("demo api").version("1.0")
				.termsOfServiceUrl("www.baidu.com").contact(new Contact("zl", "www.baidu.com", "123456@qq.com"))
				.license("©i am license").licenseUrl("www.baidu.com")
				// .extensions(Collections.emptyList())
				.build();
	}
}
