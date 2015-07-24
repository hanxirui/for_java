package com.hxr.javatone.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证失败
 * 常量池内存溢出探究 
 * <br>
 * args : -XX:PermSize=10M -XX:MaxPermSize=10M
 * <p>
 * Create on : 2015年7月23日<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author hanxirui<br>
 * @version javastone v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class ConstantOutOfMemory {
    public static void main(final String[] args) throws Exception {

        try {

            List<String> strings = new ArrayList<String>();

            int i = 0;

            while (true) {

                strings.add(String.valueOf(i++).intern());

            }

        } catch (Exception e) {

            e.printStackTrace();

            throw e;

        }

    }

}
