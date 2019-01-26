package com.proptiger.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager(RedisTemplate<Object,String> redisTemplate) {
		return new RedisCacheManager(redisTemplate);
	}
	
	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.afterPropertiesSet();
		return jedisConnectionFactory;
	}
	
	@Bean
	public RedisTemplate<Object, String> redisTemplate(RedisConnectionFactory redisCF) {
		RedisTemplate<Object, String> redisTemplate = new RedisTemplate<Object, String>();
		redisTemplate.setConnectionFactory(redisCF);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
	
}
