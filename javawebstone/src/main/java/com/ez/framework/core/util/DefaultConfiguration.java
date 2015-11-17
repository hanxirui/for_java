package com.ez.framework.core.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author liuyang
 *
 */
public class DefaultConfiguration {

    private static final String S_PATTERN_SEP = "\\s*{0}\\s*";
    protected Properties m_properties = new Properties();
    private String m_name;

    /**
     * Constructors.
     * @param propertiesName Properties file name
     */
    public DefaultConfiguration(String propertiesName){
        m_name = propertiesName;
    }
    
    /**
     * Constructors.
     * @param propertiesName Properties file name
     * @param load Is load the properties file.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public DefaultConfiguration(String propertiesName, boolean load) throws FileNotFoundException, IOException{
        m_name = propertiesName;
        if(load){
            load();
        }
    }

    /**
     * Return a name of the current properties file.
     * @return
     */
    public String getName() {
        return m_name;
    }

    /**
     * Get a attribute value.
     * @param key Key
     * @param defaultValue Default value
     * @return
     */
    public String get(String key, String defaultValue) {
        return get(key) == null ? defaultValue : get(key);
    }

    /**
     * Get a attribute value of {@link Boolean}.
     * @param key Key
     * @param defaultValue Default value
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (get(key) == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(get(key));
    }

    /**
     * Get a attribute value of integer.
     * @param key Key
     * @param defaultValue Default value
     * @return
     */
    public int getInt(String key, int defaultValue) {
        if (get(key) == null) {
            return defaultValue;
        }
        return Integer.parseInt(get(key));
    }
    
    /**
     * Get a attribute values of array.
     * @param key Key
     * @param separator Separator
     * @param defaultValue Default value
     * @return
     */
    public String[] getArray(String key, char separator, String[] defaultValue){
        if(get(key) == null){
            return defaultValue;
        }
        return get(key).split(MessageFormat.format(S_PATTERN_SEP, separator));
    }
    
    /**
     * Get a attribute values of {@link Map}.
     * @param key Key
     * @param separator level 1 separator
     * @param separator2 level 2 separator
     * @param defaultValue Default value
     * @return
     */
    public Map<String, String> getMap(String key, char separator, char separator2, Map<String, String> defaultValue){
        if(get(key) == null){
            return defaultValue;
        }
        String[] elems = getArray(key, separator, null);
        Map<String, String> map = new HashMap<String, String>();
        if(elems != null){
            for(String elem : elems){
                String[] _arr = elem.split(MessageFormat.format(S_PATTERN_SEP, separator2));
                map.put(_arr[0], _arr[1]);
            }
            return map;
        }
        return null;
    }

    /**
     * Get a attribute value.
     * @param key
     * @return
     */
    protected String get(String key) {
        return m_properties.getProperty(key);
    }

    /**
     * Load the properties file into memory.
     * @return {@link DefaultConfiguration} instance
     * @throws FileNotFoundException
     * @throws IOException
     */
    public DefaultConfiguration load() throws FileNotFoundException, IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(m_name);
        if (in == null)
            throw new FileNotFoundException(m_name);
        m_properties.load(in);
        return this;
    }
}
