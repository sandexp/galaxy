package com.github.galaxy.middleware;


import com.github.galaxy.common.redis.service.JedisOpsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisIntegrationSuite {

    @Autowired
    private JedisOpsService service;

    @PostConstruct
    private void init(){
        service.init();
    }

    @Test
    public void testLettuce(){
    }

    @Test
    public void testJedis(){
        System.out.println(service.get("ggg"));
    }
}
