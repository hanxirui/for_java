package com.hxr.javatone.serialization;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.hxr.javatone.serialization.Person.Gender;

/**
 * 
 */
public class SerializationDemo {
    private long userTime = 0;

    public List<byte[]> bean2byte(final List<Person> pList) {
        if (pList == null || pList.size() <= 0) {
            return null;
        }
        long start = System.currentTimeMillis();
        List<byte[]> bytes = new ArrayList<byte[]>();
        Schema<Person> schema = RuntimeSchema.getSchema(Person.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        byte[] protostuff = null;
        for (Person p : pList) {
            try {
                protostuff = ProtostuffIOUtil.toByteArray(p, schema, buffer);
                bytes.add(protostuff);
            } finally {
                buffer.clear();
            }
        }
        long end = System.currentTimeMillis();
        this.userTime = end - start;
        return bytes;
    }

    public List<Person> byte2bean(final List<byte[]> bytesList) {
        if (bytesList == null || bytesList.size() <= 0) {
            return null;
        }
        long start = System.currentTimeMillis();
        Schema<Person> schema = RuntimeSchema.getSchema(Person.class);
        List<Person> list = new ArrayList<Person>();
        for (byte[] bs : bytesList) {
            Person product = new Person();
            ProtostuffIOUtil.mergeFrom(bs, product, schema);
            list.add(product);
        }
        long end = System.currentTimeMillis();
        this.userTime = end - start;
        return list;
    }

    public static void main(final String[] args) {
        List<Person> ps = new ArrayList<Person>();
        for (int i = 0; i < 10; i++) {
            Person person = new Person();
            person.setName("John Doe" + i);
            person.setMotto("Speed kills!" + i);
            person.setGender(Gender.MALE);
            person.setDate(new Date());

            List<User> userlist = new ArrayList<User>();
            for (int j = 0; j < 10; j++) {
                User user = new User();
                user.setEmail("cndone@gmail" + j);
                user.setFirstName("bie" + j);
                user.setLastName("哈哈" + j);
                userlist.add(user);
            }
            person.setUserlist(userlist);
            ps.add(person);
        }
        SerializationDemo sz = new SerializationDemo();
        List<byte[]> lb = sz.bean2byte(ps);
        System.out.println("bean2byte use time --- " + sz.userTime);
        List<Person> ps1 = sz.byte2bean(lb);
        System.out.println("bean2byte use time --- " + sz.userTime);
        for (Person t_person : ps1) {
            System.out.println("---" + t_person.getName());
        }
    }
}
