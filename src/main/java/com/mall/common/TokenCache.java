package com.mall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Author: jonny
 * Time: 2017-05-12 23:22.
 */
public class TokenCache {

    public static final int HALF_AN_HOUR = 30;

    public static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);

    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(HALF_AN_HOUR,TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
        @Override
        public String load(String s) throws Exception {
            return "null";
        }
    });

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }


    public static String getValue(String key){
        try {
            String value = localCache.get(key);
            if ("null".equalsIgnoreCase(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return  null;
    }

}
