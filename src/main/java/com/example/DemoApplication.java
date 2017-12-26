package com.example;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication(scanBasePackages = "com.example.*")
@EnableCaching
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
        String[] beanNames = run.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        //获取所有beans
        for (String beanName : beanNames) {
            //System.out.println(beanName);
        }
        StringRedisTemplate template = run.getBean(StringRedisTemplate.class);
        System.out.println(template.getValueSerializer().getClass());
       template.opsForValue().set("hello", "t命令行窗口中使用的代码页是中文或者美国的");
       System.out.println(template.opsForValue().get("hello"));
	}
}
