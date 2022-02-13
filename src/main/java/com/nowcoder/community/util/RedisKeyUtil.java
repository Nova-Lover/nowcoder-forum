package com.nowcoder.community.util;

/**
 * RedisKey生成工具
 * @author Alex
 * @version 1.0
 * @date 2022/2/13 15:42
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_TYPE="like:entity";

    private RedisKeyUtil(){

    }

    /**
     * 生成某个实体的赞的RedisKey
     *  like:entity:entityType:entityId  ==> set(userId)
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_TYPE + SPLIT + entityType + SPLIT + entityId;

    }
}
