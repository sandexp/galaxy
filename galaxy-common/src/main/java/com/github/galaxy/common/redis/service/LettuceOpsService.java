package com.github.galaxy.common.redis.service;

import com.github.galaxy.common.redis.config.LettuceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Provide lettuce client to connect with redis service.
 */
@Slf4j
@Service
public class LettuceOpsService extends RedisOpsService{

}
