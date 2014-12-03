package com.hxr.flex.imagefile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final String[] IMAGE_TYPES = new String[] { "png", "gif", "jpg", "PNG", "GIF", "JPG" };
    private static final String[] FILE_TYPES = new String[] { "mxml", "as", "css" };
    
    private static final String IMAGE_TARGET_PATH = "/Users/hanxirui/Documents/workspace/workshop_bmc/RIIL_Flex_Project/trunk/sources/ALLIMAGE";

    public static void main(String[] args) {
        String flexProjectPath = "/Users/hanxirui/Documents/workspace/workshop_bmc/RIIL_Flex_Project/trunk/sources/Alarm/Alarm";
        listFiles(new File(flexProjectPath), Arrays.asList(FILE_TYPES));
    }

    /**
     * @param flexFile
     */
    /**
     * 列出指定目录下的所有指定类型文件..
     * 
     * @param flexFile
     * @param fileTypes - 文件类型
     */
    private static void listFiles(File flexFile, List<String> fileTypes) {

        if (flexFile.isDirectory()) {
            String[] str = flexFile.list();
            for (int i = 0; i < str.length; i++) {

                listFiles(new File(flexFile.getPath() + File.separator + str[i]), fileTypes);
            }
        } else {
            String t_filePath = flexFile.getAbsolutePath();
            if (t_filePath.lastIndexOf(".") > 0) {
                String t_fileType = t_filePath.substring(t_filePath.lastIndexOf(".") + 1, t_filePath.length());

                if (fileTypes.contains(t_fileType)) {
                    listImagePath(flexFile);
                }
            }
        }
    }

    /**
     * 找出所有指定文件中的图片.
     * 
     * @param flexFile
     */
    private static void listImagePath(File flexFile) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(flexFile));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号

                for (String imgType : IMAGE_TYPES) {
                    if (tempString.indexOf(imgType) > 0) {
                        System.out.println("line " + line + ": " + tempString);
                    }
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    private static void copyImage(String src, String target) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(src);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(src); // 读入原文件
                FileOutputStream fs = new FileOutputStream(target);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小

                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制文件-" + src + "-操作出错");
            e.printStackTrace();

        }
    }
}
