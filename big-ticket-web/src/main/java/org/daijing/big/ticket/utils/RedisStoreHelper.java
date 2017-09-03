package org.daijing.big.ticket.utils;

import com.dianping.beauty.common.redis.FakeRedisClient;
import com.dianping.squirrel.client.StoreKey;
import com.dianping.squirrel.common.exception.StoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 *
 * @param <T>
 */
public class RedisStoreHelper<T> {

    private static final FakeRedisClient storeClient = new FakeRedisClient();

    private static final Logger logger = LoggerFactory.getLogger(RedisStoreHelper.class);

    /**
     * 若缓存中存在则直接缓存；
     * 若没有则根据回调函数查询结果并设置到缓存并返回
     * @param cacheKey 缓存StoreKey
     * @param cacheCallBack 缓存不存在时，回调函数
     * @param <T> 返回结果类型
     * @return 返回结果，可能为null
     */
    public static <T> T get(StoreKey cacheKey, StoreCallBack cacheCallBack,boolean isNullSet){
        Object cacheValue = null;
        boolean fromDB = false;
        try {
            cacheValue = storeClient.get(cacheKey);
        } catch (StoreException e) {
            logger.error("获取缓存异常,StoreKey=" + (cacheKey != null ? cacheKey.toString() : "null val"), e);
            fromDB = true;
        }
        if(null!=cacheValue){
            return (T) cacheValue;
        }else if(fromDB || isNullSet){
            cacheValue = cacheCallBack.getResult(cacheKey);
            if(null!=cacheValue && isNullSet){
                storeClient.asyncSet(cacheKey, cacheValue);
            }
        }
        return (T)cacheValue;
    }


    public static boolean set(StoreKey cacheKey, Object cacheValue) {
        try {
            return storeClient.set(cacheKey, cacheValue);
        } catch (StoreException e) {
            logger.error("set cache failed", e);
            return false;
        }
    }

    public static Future<Boolean> asyncSet(StoreKey cacheKey, Object cacheValue) {
        try {
            return storeClient.asyncSet(cacheKey, cacheValue);
        } catch (StoreException e) {
            logger.error("asyncSet errors, keys = " + cacheKey, e);
        }
        return null;
    }

    public static boolean setList(StoreKey cacheKey, List<? extends Object> cacheList) {
        try {
            if (cacheList == null || cacheList.isEmpty()) {
                logger.error("缓存值为空");
                return false;
            }
            lDelete(cacheKey);
            Long size = storeClient.rpush(cacheKey, cacheList.toArray());
            if (size.intValue() != cacheList.size()) {
                logger.error(String.format("成功刷进缓存的list与当前入参数量不一致!成功:%d个,入参:%d个",size.intValue(), cacheList.size()));
                lDelete(cacheKey);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("setList cache failed. cacheKey=" + cacheKey.toString(), e);
            return false;
        }
    }

    private static boolean lDelete(StoreKey cacheKey) {
        Class fakeRedisClientClass = FakeRedisClient.class;
        try {
            Field lmapField = fakeRedisClientClass.getDeclaredField("lmap");
            lmapField.setAccessible(true);
            Map<StoreKey, List<Object>> lmap = (HashMap<StoreKey, List<Object>>)lmapField.get(storeClient);
            lmap.remove(cacheKey);
        } catch (Exception e) {
            logger.error("反射获取lmap异常", e);
        }
        return true;
    }

    public static  int getListLen(StoreKey cacheKey, StoreCallBack<Integer> cacheCallBack, boolean isNullSet){
        int listLen =0;
        try {
            listLen = storeClient.llen(cacheKey).intValue();
        } catch (StoreException e) {
            logger.error(e.getMessage(),e);
        }
        if(listLen > 0){
            return listLen;
        }else {
            listLen = cacheCallBack.getResult(cacheKey);
        }
        return listLen;
    }

    public static  <T> List<T> getList(StoreKey cacheKey, StoreCallBack<List<T>> cacheCallBack, boolean isNullSet,
                                       int start, int end){
        List<T> cacheList = null;
        boolean fromDB = false;
        try {
            cacheList = (List<T>)(Object)storeClient.lrange(cacheKey, start, end);
            if(null != cacheList && !cacheList.isEmpty())
                return cacheList;
            else if(storeClient.llen(cacheKey) == 0)
                fromDB = true;
        } catch (StoreException e) {
            logger.error("getList error", e);
            fromDB = true;
        }
        if(fromDB){
            cacheList = cacheCallBack.getResult(cacheKey);
        }
        return cacheList;
    }


}
