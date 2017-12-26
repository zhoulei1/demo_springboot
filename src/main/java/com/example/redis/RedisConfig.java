package com.example.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;


@Configuration
public class RedisConfig {
	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		//默认JedisConnectionFactory
		/**StringRedisTemplate 默认StringRedisSerializer,
		 * RedisTemplate默认JdkSerializationRedisSerializer
		 * 可选JacksonJsonRedisSerializer,OxmSerializer
		 * 遵循存取共模板(保证serializer一致性)
		 * 
		 * */
		return new StringRedisTemplate(connectionFactory);
	}
}
