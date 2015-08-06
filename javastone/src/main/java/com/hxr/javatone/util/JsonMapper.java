package com.hxr.javatone.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 * 
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 * 
 */
public class JsonMapper {

  private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

  private final ObjectMapper mapper;
  
  public static void main(final String[] args) {
      UserVo zhangsan = new UserVo();
      Address address = new Address();
      address.setCity("tianjin");
      address.setCode(300130);
      zhangsan.setAddress(address);
      zhangsan.setAge(33);
      zhangsan.setName("zhangsan");
      //bean2json
      String json = JsonMapper.toLogJson(zhangsan);
      System.out.println("对象转换为json：" + json);

      //json2bean，需要注意：Student类和Teacher类必须有一个空的构造方法

      //JsonMapper提供了很多创建Mapper的方法，不是非要用buildNonDefaultMapper，你可以对比几种方法的转换时间，挑个最快的
      UserVo student2 = JsonMapper.buildNonDefaultMapper().fromJson(json, UserVo.class);
      System.out.println("json转换成对象：" + student2);

}

  public JsonMapper(final Inclusion inclusion) {
    mapper = new ObjectMapper();
    //设置输出时包含属性的风格
    mapper.setSerializationInclusion(inclusion);
    //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //禁止使用int代表Enum的order()來反序列化Enum,非常危險
    mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
  }

  /**
   * 创建输出全部属性到Json字符串的Mapper.
   */
  public static JsonMapper buildNormalMapper() {
    return new JsonMapper(Inclusion.ALWAYS);
  }

  /**
   * 创建只输出非空属性到Json字符串的Mapper.
   */
  public static JsonMapper buildNonNullMapper() {
    return new JsonMapper(Inclusion.NON_NULL);
  }

  /**
   * 创建只输出初始值被改变的属性到Json字符串的Mapper.
   */
  public static JsonMapper buildNonDefaultMapper() {
    return new JsonMapper(Inclusion.NON_DEFAULT);
  }

  /**
   * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper.
   */
  public static JsonMapper buildNonEmptyMapper() {
    return new JsonMapper(Inclusion.NON_EMPTY);
  }

  /**
   * 如果对象为Null, 返回"null".
   * 如果集合为空集合, 返回"[]".
   */
  public String toJson(final Object object) {

    try {
      return mapper.writeValueAsString(object);
    } catch (IOException e) {
      throw NestedException.wrap(e);
    }
  }

  /**
   * 如果JSON字符串为Null或"null"字符串, 返回Null.
   * 如果JSON字符串为"[]", 返回空集合.
   * 
   * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
   * @see #constructParametricType(Class, Class...)
   */
  public <T> T fromJson(final String jsonString, final Class<T> clazz) {
    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }

    try {
      return mapper.readValue(jsonString, clazz);
    } catch (IOException e) {
      throw NestedException.wrap(e);
    }
  }

  /**
   * 如果JSON字符串为Null或"null"字符串, 返回Null.
   * 如果JSON字符串为"[]", 返回空集合.
   * 
   * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
   * @see #constructParametricType(Class, Class...)
   */
  @SuppressWarnings("unchecked")
  public <T> T fromJson(final String jsonString, final JavaType javaType) {
    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }

    try {
      return (T) mapper.readValue(jsonString, javaType);
    } catch (IOException e) {
      throw NestedException.wrap(e);
    }
  }
  
  @SuppressWarnings("unchecked")
  public <T> T fromJson(final String jsonString, final Class<?> parametrized, final Class<?>... parameterClasses) {
    return (T) this.fromJson(jsonString, constructParametricType(parametrized, parameterClasses));
  }
  
  @SuppressWarnings("unchecked")
  public <T> List<T> fromJsonToList(final String jsonString, final Class<T> classMeta){
    return (List<T>) this.fromJson(jsonString,constructParametricType(List.class, classMeta));
  }
  
  @SuppressWarnings("unchecked")
  public <T> T fromJson(final JsonNode node, final Class<?> parametrized, final Class<?>... parameterClasses) {
    JavaType javaType = constructParametricType(parametrized, parameterClasses);
    try {
      return (T) mapper.readValue(node, javaType);
    } catch (IOException e) {
      throw NestedException.wrap(e);
    }
  }
  
  @SuppressWarnings("unchecked")
  public <T> T pathAtRoot(final String json, final String path, final Class<?> parametrized, final Class<?>... parameterClasses){
    JsonNode rootNode = parseNode(json);
    JsonNode node = rootNode.path(path);
    return (T) fromJson(node, parametrized, parameterClasses);
  }
  
  @SuppressWarnings("unchecked")
  public <T> T pathAtRoot(final String json, final String path, final Class<T> clazz){
    JsonNode rootNode = parseNode(json);
    JsonNode node = rootNode.path(path);
    return (T) fromJson(node, clazz);
  }

  /**
   * 構造泛型的Type如List<MyBean>, 则调用constructParametricType(ArrayList.class,MyBean.class)
   *             Map<String,MyBean>则调用(HashMap.class,String.class, MyBean.class)
   */
  public JavaType constructParametricType(final Class<?> parametrized, final Class<?>... parameterClasses) {
    return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
  }

  /**
   * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
   */
  @SuppressWarnings("unchecked")
  public <T> T update(final T object, final String jsonString) {
    try {
      return (T) mapper.readerForUpdating(object).readValue(jsonString);
    } catch (JsonProcessingException e) {
      logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
    } catch (IOException e) {
      logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
    }
    return null;
  }

  /**
   * 輸出JSONP格式數據.
   */
  public String toJsonP(final String functionName, final Object object) {
    return toJson(new JSONPObject(functionName, object));
  }

  /**
   * 設定是否使用Enum的toString函數來讀寫Enum,
   * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
   * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
   */
  public void setEnumUseToString(final boolean value) {
    mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, value);
    mapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, value);
  }

  /**
   * 取出Mapper做进一步的设置或使用其他序列化API.
   */
  public ObjectMapper getMapper() {
    return mapper;
  }
  
  public JsonNode parseNode(final String json){
    try {
      return mapper.readValue(json, JsonNode.class);
    } catch (IOException e) {
      throw NestedException.wrap(e);
    }
  }
  
  /**
   * 输出全部属性
   * @param object
   * @return
   */
  public static String toNormalJson(final Object object){
    return new JsonMapper(Inclusion.ALWAYS).toJson(object);
  }
  
  /**
   * 输出非空属性
   * @param object
   * @return
   */
  public static String toNonNullJson(final Object object){
    return new JsonMapper(Inclusion.NON_NULL).toJson(object);
  }
  
  /**
   * 输出初始值被改变部分的属性
   * @param object
   * @return
   */
  public static String toNonDefaultJson(final Object object){
    return new JsonMapper(Inclusion.NON_DEFAULT).toJson(object);
  }
  
  /**
   * 输出非Null且非Empty(如List.isEmpty)的属性
   * @param object
   * @return
   */
  public static String toNonEmptyJson(final Object object){
    return new JsonMapper(Inclusion.NON_EMPTY).toJson(object);
  }
  
  public void setDateFormat(final String dateFormat){
    mapper.setDateFormat(new SimpleDateFormat(dateFormat));
  }
  
  public static String toLogJson(final Object object){
    JsonMapper jsonMapper = new JsonMapper(Inclusion.NON_EMPTY);
    jsonMapper.setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);
    return jsonMapper.toJson(object);
  }

}
class UserVo{
    String name;
    int age;
    Address address;
    /**
     * @return name - {return content description}
     */
    public String getName() {
        return name;
    }
    /**
     * @param name - {parameter description}.
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * @return age - {return content description}
     */
    public int getAge() {
        return age;
    }
    /**
     * @param age - {parameter description}.
     */
    public void setAge(final int age) {
        this.age = age;
    }
    /**
     * @return address - {return content description}
     */
    public Address getAddress() {
        return address;
    }
    /**
     * @param address - {parameter description}.
     */
    public void setAddress(final Address address) {
        this.address = address;
    }
}
class Address{
    int code;
    String city;
    /**
     * @return code - {return content description}
     */
    public int getCode() {
        return code;
    }
    /**
     * @param code - {parameter description}.
     */
    public void setCode(final int code) {
        this.code = code;
    }
    /**
     * @return city - {return content description}
     */
    public String getCity() {
        return city;
    }
    /**
     * @param city - {parameter description}.
     */
    public void setCity(final String city) {
        this.city = city;
    }
}
