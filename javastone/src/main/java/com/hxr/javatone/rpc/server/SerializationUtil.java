package com.hxr.javatone.rpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
//编写一个 SerializationUtil 工具类，使用 Protostuff 实现序列化
//以上了使用 Objenesis 来实例化对象，它是比 Java 反射更加强大。
//
//注意：如需要替换其它序列化框架，只需修改 SerializationUtil 即可。当然，更好的实现方式是提供配置项来决定使用哪种序列化方式。
public class SerializationUtil {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
    private static Objenesis objenesis = new ObjenesisStd(true);
    private SerializationUtil() {
    }
    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(final Class<T> cls) {
      Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
      if (schema == null) {
        schema = RuntimeSchema.createFrom(cls);
        if (schema != null) {
          cachedSchema.put(cls, schema);
        }
      }
      return schema;
    }
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(final T obj) {
      Class<T> cls = (Class<T>) obj.getClass();
      LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
      try {
        Schema<T> schema = getSchema(cls);
        return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
      } catch (Exception e) {
        throw new IllegalStateException(e.getMessage(), e);
      } finally {
        buffer.clear();
      }
    }
    public static <T> T deserialize(final byte[] data, final Class<T> cls) {
      try {
        T message = objenesis.newInstance(cls);
        Schema<T> schema = getSchema(cls);
        ProtostuffIOUtil.mergeFrom(data, message, schema);
        return message;
      } catch (Exception e) {
        throw new IllegalStateException(e.getMessage(), e);
      }
    }
  }