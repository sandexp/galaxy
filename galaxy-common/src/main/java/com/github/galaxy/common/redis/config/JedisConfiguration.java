package com.github.galaxy.common.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.time.Duration;

@Configuration
public class JedisConfiguration extends RedisTemplateConfiguration {

	private static Logger logger= LoggerFactory.getLogger(JedisConfiguration.class);

	@Value("${spring.redis.database}")
	private int database;

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.jedis.pool.max-active}")
	private int maxActive;

	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;

	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.jedis.pool.min-idle}")
	private int minIdle;

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxActive);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setMinIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		return jedisPoolConfig;
	}


	@Bean
	public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
		JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration
				.builder()
				.usePooling()
				.poolConfig(jedisPoolConfig)
				.and()
				.readTimeout(Duration.ofMillis(timeout))
				.build();
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		return new JedisConnectionFactory(
				redisStandaloneConfiguration,
				jedisClientConfiguration);
	}

	@Bean
	public RedisTemplate<Serializable, Serializable> jedisTemplate(JedisConnectionFactory factory){
		RedisTemplate<Serializable, Serializable> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setConnectionFactory(factory);
		return template;
	}

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory factory){
		return RedisCacheManager.create(factory);
	}

	public RedisTemplate getTemplate(){
		return jedisTemplate(jedisConnectionFactory(jedisPoolConfig()));
	}
}
