package am.hanxirui.bootdemo.elasticsearch.one;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 枚举工具类
 */
public class EnumUtils {

    // 枚举字符串转枚举类型的转换方法
    private static String FROM_STRING = "fromString";

    /**
     * 供
     * 解析通用查询参数（分页参数、排序参数）
     * DataGridUtils.parseQueryInfo方法调用
     *
     * 根据类、字段名称、字段值（枚举字符），若字段为枚举，返回枚举值，否则直接返回字段值
     * @param entityCls 类，如com.linewell.ccip.servicesbase.bean.log.OrgLog.class
     * @param fieldName 字段名称，如com.linewell.ccip.servicesbase.bean.log.type.OperType
     * @param fieldVal 字段（枚举字符串），如INSERT
     * @return 若字段为枚举，返回枚举值，否则直接返回字段值
     */
    public static Object getValByField(Class<?> entityCls, String fieldName, Object fieldVal) {
        Object obj = null;
        try {
            // 字段类型
            Class<?> fieldCls = getFieldType(entityCls, fieldName);
            // 字段类型是否为枚举类型
            boolean isEnumCls = fieldCls.isEnum();

            // 是枚举类
            if (isEnumCls) {
                obj = getEnum(fieldCls, (String)fieldVal);
            } else {
                obj = fieldVal;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 根据枚举字符串获取枚举值
     * @param fieldCls	枚举类
     * @param fieldVal	枚举字符串
     * @return			枚举值
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static Enum<?> getEnum(Class<?> fieldCls, String fieldVal)
            throws IllegalAccessException, InvocationTargetException {
        Enum<?> enumCls = null;
        // 全部的方法
        Method[] methods = fieldCls.getMethods();
        // 方法不为空
        if (null != methods && 0 < methods.length) {
            // 方法名称
            String metName = null;
            // 遍历全部方法
            for (Method method : methods) {
                metName = method.getName();
                // 枚举类的字符串转枚举的方法
                if (FROM_STRING.equalsIgnoreCase(metName)) {
                    enumCls = (Enum<?>) method.invoke(fieldCls, fieldVal);
                    break;
                }
            }
        }
        return enumCls;
    }

    /**
     * 根据类、属性名获取其属性类型
     * @param cls
     * @param findFieldName
     * @return
     */
    private static Class<?> getFieldType(Class<?> cls, String findFieldName) {
        // 字段类型
        Class<?> fieldCls = null;
        try {
            // 获取该类自身所声明的属性，没有获取父类声明的属性
            List<Field> fields = getFields(cls);
            // 属性不为空
            if (null != fields) {
                // 是否找到属性
                boolean isFind = false;
                // 属性名
                String fieldName = "";
                // 变量属性数组
                for (Field field : fields) {
                    fieldName = field.getName();
                    // 类自身找到属性获取其属性类型
                    if (findFieldName.equalsIgnoreCase(fieldName)) {
                        isFind = true;
                        fieldCls = field.getType();
                        break;
                    }
                }
                // 类自身没有找到属性获取其属性类型，查找其父类声明的属性
                if (false == isFind) {
                    Field supField = cls.getSuperclass().getDeclaredField(findFieldName);
                    fieldCls = supField.getType();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return fieldCls;
    }

    private static List<Field> getFields(Class<?> objClass) {
        Field[] fields = objClass.getDeclaredFields();
        List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(Arrays.asList(fields));
        while (null != objClass) {
            fieldList.addAll(Arrays.asList(objClass.getDeclaredFields()));
            objClass = objClass.getSuperclass();
        }
        return fieldList;
    }
}



