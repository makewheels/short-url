package com.eg.shorturl.redis;

/**
 * @Author makewheels
 * @Time 2021.01.30 13:00:45
 */
public class RedisKey {
    private static final String ROOT = "short-url";
    private static final String SHORT_ID = ROOT + ":shortId";

    public static String shortId(String shortId) {
        return SHORT_ID + ":" + shortId;
    }

}
