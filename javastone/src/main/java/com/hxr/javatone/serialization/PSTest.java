package com.hxr.javatone.serialization;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.XmlIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.hxr.javatone.serialization.Person.Gender;


/**
 * {测试bean和xml间的转换效率}
 * <br>
 *  
 * <p>
 * Create on : 2015年8月7日<br>
 * <p>
 * </p>
 * <br>
 * @author hanxirui<br>
 * @version javastone v1.0
 * <p>
 *<br>
 * <strong>Modify History:</strong><br>
 * user     modify_date    modify_content<br>
 * -------------------------------------------<br>
 * <br>
 */
public class PSTest {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        Person person = new Person();
        person.setName("John Doe");
        person.setMotto("Speed kills!");
        person.setGender(Gender.MALE);
        person.setDate(new Date());
        
        List<User> userlist = new ArrayList<User>();
        for(int i =0 ;i<10; i++){
            User user = new User();
            user.setEmail("cndone@gmail"+i);
            user.setFirstName("bie"+i);
            user.setLastName("哈哈"+i);
            userlist.add(user);
        }
        person.setUserlist(userlist);
        
        long begin = System.nanoTime();
        Schema<Person> schema = RuntimeSchema.getSchema(Person.class);  
         byte[] xml = XmlIOUtil.toByteArray(person, schema);
         String str = "";
        try {
            str = new String(xml, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         System.out.println(str);
         long mid = System.nanoTime(); 
         System.out.println(mid-begin);
         
         Person f = new Person();  
         XmlIOUtil.mergeFrom(xml, f, schema);
         System.out.println(f.getName());
         System.out.println( new SimpleDateFormat("yyyy-MM-dd").format(f.getDate()));
         long end = System.nanoTime(); 
         System.out.println(end-mid);
         
         
    }

}

