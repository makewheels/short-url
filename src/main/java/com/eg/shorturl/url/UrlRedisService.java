package com.eg.shorturl.url;

import com.alibaba.fastjson.JSON;
import com.eg.shorturl.bean.Url;
import com.eg.shorturl.redis.RedisKey;
import com.eg.shorturl.redis.RedisService;
import com.eg.shorturl.redis.RedisTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UrlRedisService {
    @Resource
    private RedisService redisService;


    public Url getUrl(String shortId) {
        String json = (String) redisService.get(RedisKey.shortId(shortId));
        return JSON.parseObject(json, Url.class);
    }

    public void setUrl(String shortId, Url url) {
        redisService.set(RedisKey.shortId(shortId),
                JSON.toJSONString(url), RedisTime.SIX_HOURS);
    }
}
