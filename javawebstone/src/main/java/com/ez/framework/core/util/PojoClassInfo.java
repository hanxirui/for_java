package com.ez.framework.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.lang.reflect.Type;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import com.ez.framework.core.pojo.IPojo;

public class PojoClassInfo {

    /**
     * <code>EMPTY_STRING_ARRAY</code>-空String对象数组.
     */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * <code>SIMPLE_TYPE_SET</code>-数据类型SET集合.
     */
    private static final Set<Class<?>> SIMPLE_TYPE_SET = new HashSet<Class<?>>();

    /**
     * <code>S_CLASS_INFO_MAP</code>-Class信息集合,用来缓存已存在的类型.
     */
    private static final Map<Class<?>, PojoClassInfo> S_CLASS_INFO_MAP = Collections
            .synchronizedMap(new HashMap<Class<?>, PojoClassInfo>());

    /**
     * <code>m_className</code>-类名称.
     */
    private String m_className;

    /**
     * <code>m_readablePropertyNames</code>-可以读取的属性名称集合.
     */
    private String[] m_readablePropertyNames = EMPTY_STRING_ARRAY;

    /**
     * <code>writeablePropertyNames</code>-可以写的属性名称集合.
     */
    private String[] m_writeablePropertyNames = EMPTY_STRING_ARRAY;

    /**
     * <code>setMethods</code>-Set方法集合.
     */
    private Map<String, Method> m_setMethods = new HashMap<String, Method>();

    /**
     * <code>getMethods</code>-get方法集合.
     */
    private Map<String, Method> m_getMethods = new HashMap<String, Method>();

    /**
     * <code>setTypes</code>-Set数据类型集合.
     */
    private Map<String, Type> m_setTypes = new HashMap<String, Type>();

    /**
     * <code>getTypes</code>-Get方法返回值数据类型集合.
     */
    private Map<String, Type> m_getTypes = new HashMap<String, Type>();

    static {
        SIMPLE_TYPE_SET.add(String.class);
        SIMPLE_TYPE_SET.add(Byte.class);
        SIMPLE_TYPE_SET.add(Short.class);
        SIMPLE_TYPE_SET.add(Character.class);
        SIMPLE_TYPE_SET.add(Integer.class);
        SIMPLE_TYPE_SET.add(Long.class);
        SIMPLE_TYPE_SET.add(Float.class);
        SIMPLE_TYPE_SET.add(Double.class);
        SIMPLE_TYPE_SET.add(Boolean.class);
        SIMPLE_TYPE_SET.add(Date.class);
        SIMPLE_TYPE_SET.add(Class.class);
        SIMPLE_TYPE_SET.add(BigInteger.class);
        SIMPLE_TYPE_SET.add(BigDecimal.class);

        SIMPLE_TYPE_SET.add(Collection.class);
        SIMPLE_TYPE_SET.add(Set.class);
        SIMPLE_TYPE_SET.add(Map.class);
        SIMPLE_TYPE_SET.add(List.class);
        SIMPLE_TYPE_SET.add(HashMap.class);
        SIMPLE_TYPE_SET.add(TreeMap.class);
        SIMPLE_TYPE_SET.add(ArrayList.class);
        SIMPLE_TYPE_SET.add(LinkedList.class);
        SIMPLE_TYPE_SET.add(HashSet.class);
        SIMPLE_TYPE_SET.add(TreeSet.class);
        SIMPLE_TYPE_SET.add(Vector.class);
        SIMPLE_TYPE_SET.add(Hashtable.class);
        SIMPLE_TYPE_SET.add(Enumeration.class);
    }

    /**
     * 构造函数
     * 
     * @param clazz
     *            - 类.
     */
    private <T extends IPojo> PojoClassInfo(Class<T> clazz) {
        m_className = clazz.getName();
        addMethods(clazz);
        m_readablePropertyNames = (String[]) m_getMethods.keySet().toArray(new String[m_getMethods.keySet().size()]);
        m_writeablePropertyNames = (String[]) m_setMethods.keySet().toArray(new String[m_setMethods.keySet().size()]);
    }

    /**
     * Gets the name of the class the instance provides information for
     * 
     * @return The class name
     */
    public String getClassName() {
        return m_className;
    }

    /**
     * Gets the setter for a property as a Method object
     * 
     * @param propertyName
     *            - the property
     * @return The Method
     */
    public Method getSetter(String propertyName) {
        return (Method) m_setMethods.get(propertyName);
    }

    /**
     * Gets the getter for a property as a Method object
     * 
     * @param propertyName
     *            - the property
     * @return The Method
     */
    public Method getGetter(String propertyName) {
        return (Method) m_getMethods.get(propertyName);
    }

    /**
     * Gets the type for a property setter
     * 
     * @param propertyName
     *            - the name of the property
     * @return The Class of the propery setter
     */
    public Class<?> getSetterType(String propertyName) {
        return (Class<?>) m_setTypes.get(propertyName);
    }

    /**
     * Gets the type for a property getter
     * 
     * @param propertyName
     *            - the name of the property
     * @return The Class of the propery getter
     */
    public Class<?> getGetterType(String propertyName) {
        return (Class<?>) m_getTypes.get(propertyName);
    }

    /**
     * Gets an array of the readable properties for an object
     * 
     * @return The array
     */
    public String[] getReadablePropertyNames() {
        return m_readablePropertyNames;
    }

    /**
     * Gets an array of the writeable properties for an object
     * 
     * @return The array
     */
    public String[] getWriteablePropertyNames() {
        return m_writeablePropertyNames;
    }

    /**
     * Check to see if a class has a writeable property by name
     * 
     * @param propertyName
     *            - the name of the property to check
     * @return True if the object has a writeable property by the name
     */
    public boolean hasWritableProperty(String propertyName) {
        return m_setMethods.keySet().contains(propertyName);
    }

    /**
     * Check to see if a class has a readable property by name
     * 
     * @param propertyName
     *            - the name of the property to check
     * @return True if the object has a readable property by the name
     */
    public boolean hasReadableProperty(String propertyName) {
        return m_getMethods.keySet().contains(propertyName);
    }

    /**
     * Tells us if the class passed in is a knwon common type
     * 
     * @param clazz
     *            The class to check
     * @return True if the class is known
     */
    public static boolean isKnownType(Class<?> clazz) {
        if (SIMPLE_TYPE_SET.contains(clazz)) {
            return true;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return true;
        } else if (Map.class.isAssignableFrom(clazz)) {
            return true;
        } else if (List.class.isAssignableFrom(clazz)) {
            return true;
        } else if (Set.class.isAssignableFrom(clazz)) {
            return true;
        } else if (Iterator.class.isAssignableFrom(clazz)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets an instance of ClassInfo for the specified class.
     * 
     * @param clazz
     *            The class for which to lookup the method cache.
     * @return The method cache for the class
     */
    public static <T extends IPojo> PojoClassInfo getInstance(Class<T> clazz) {

        PojoClassInfo t_classInfo = (PojoClassInfo) S_CLASS_INFO_MAP.get(clazz);

        if (t_classInfo == null) {

            synchronized (S_CLASS_INFO_MAP) {

                t_classInfo = new PojoClassInfo(clazz);

                S_CLASS_INFO_MAP.put(clazz, t_classInfo);
            }

        }

        return t_classInfo;

    }

    /**
     * Examines a Throwable object and gets it's root cause
     * 
     * @param t
     *            - the exception to examine
     * @return The root cause
     */
    public static Throwable unwrapThrowable(Throwable t) {
        Throwable t2 = t;
        while (true) {
            // if (t2 instanceof InvocationTargetException) {
            if (t instanceof InvocationTargetException) {
                t2 = ((InvocationTargetException) t).getTargetException();
            } else if (t instanceof UndeclaredThrowableException) {
                t2 = ((UndeclaredThrowableException) t).getUndeclaredThrowable();
            } else {
                return t2;
            }
        }
    }

    /**
     * 将类的属性放入相应的成员变量中.
     * 
     * @param cls
     *            - 类.
     */
    private void addMethods(Class<?> cls) {
        Method[] methods = getAllMethodsForClass(cls);
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            if (name.startsWith("set") && name.length() > 3) {
                if (methods[i].getParameterTypes().length == 1) {
                    name = dropCase(name);
                    if (!m_setMethods.containsKey(name)) {
                        m_setMethods.put(name, methods[i]);
                        m_setTypes.put(name, methods[i].getParameterTypes()[0]);
                    }
                }
            } else if (name.startsWith("get") && name.length() > 3) {
                if (methods[i].getParameterTypes().length == 0) {
                    name = dropCase(name);
                    m_getMethods.put(name, methods[i]);
                    m_getTypes.put(name, methods[i].getReturnType());
                }
            } else if (name.startsWith("is") && name.length() > 2) {
                if (methods[i].getParameterTypes().length == 0) {
                    name = dropCase(name);
                    m_getMethods.put(name, methods[i]);
                    m_getTypes.put(name, methods[i].getReturnType());
                }
            }
            name = null;
        }
    }

    /**
     * 获取某个类的所有方法.
     * 
     * @param cls
     *            - 类
     * @return 方法集合,{@link java.lang.reflect.Method}.
     */
    public Method[] getAllMethodsForClass(Class<?> cls) {
        if (cls.isInterface()) {
            // interfaces only have public methods - so the
            // simple call is all we need (this will also get superinterface
            // methods)
            return cls.getMethods();
        } else {
            // need to get all the declared methods in this class
            // and any super-class - then need to set access appropriatly
            // for private methods
            return getClassMethods(cls);
        }
    }

    /**
     * This method returns an array containing all methods declared in this
     * class and any superclass. We use this method, instead of the simpler
     * Class.getMethods(), because we want to look for private methods as well.
     * 
     * @param cls
     * @return
     */
    private Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap<String, Method>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

            // we also need to look for interface methods -
            // because the class may be abstract
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                addUniqueMethods(uniqueMethods, interfaces[i].getMethods());
            }

            currentClass = currentClass.getSuperclass();
        }

        Collection<Method> methods = uniqueMethods.values();

        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    /**
     * 方法描述
     * 
     * @param uniqueMethods
     * @param methods
     */
    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (int i = 0; i < methods.length; i++) {
            Method currentMethod = methods[i];
            String signature = getSignature(currentMethod);
            // check to see if the method is already known
            // if it is known, then an extended class must have
            // overridden a method
            if (!uniqueMethods.containsKey(signature)) {
                if (canAccessPrivateMethods()) {
                    try {
                        currentMethod.setAccessible(true);
                    } catch (Exception e) {
                        // Ignored. This is only a final precaution, nothing we
                        // can do.
                    }
                }

                uniqueMethods.put(signature, currentMethod);
            }
        }
    }

    /**
     * 获取方法的签名,签名的组成=方法名称+";"+参数类型1+","+参数类型2.....
     * 
     * @param method
     * @return
     */
    private String getSignature(Method method) {
        StringBuffer sb = new StringBuffer();
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();

        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());
        }

        return sb.toString();
    }

    /**
     * 是否能够调用私有方法.
     * 
     * @return true - 可以调用, false - 不可以调用.
     */
    private boolean canAccessPrivateMethods() {
        try {
            System.getSecurityManager().checkPermission(new ReflectPermission("suppressAccessChecks"));
            return true;
        } catch (SecurityException e) {
            return false;
        } catch (NullPointerException e) {
            return true;
        }
    }

    /**
     * 截取字符串.
     * 
     * @param name
     *            - 名称字符串.
     * @return
     */
    private static String dropCase(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            // throw new ProbeException("Error parsing property name '" + name +
            // "'. Didn't start with 'is', 'get' or 'set'.");
        }

        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.US) + name.substring(1);
        }

        return name;
    }
}
