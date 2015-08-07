package com.hxr.javatone.serialization;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hxr.javatone.serialization.Person.Gender;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



public class XStreamTest {

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
        
        /**bean 2 XML*/
        long begin = System.nanoTime();
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("Person", Person.class);
        xstream.alias("User", User.class);
        String str = xstream.toXML(person);
         System.out.println(str);
         long mid = System.nanoTime(); 
         System.out.println(mid-begin);
         
         /**XML 2 BEAN*/
         Person f = (Person) xstream.fromXML(str);
         System.out.println(f.getName());
         System.out.println( new SimpleDateFormat("yyyy-MM-dd").format(f.getDate()));
         long end = System.nanoTime(); 
         System.out.println(end-mid);
         
    }

}

