package com.sheledon.common.netty.serialize;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
public interface Serializer {
    byte[] serialize(Object obj);
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
