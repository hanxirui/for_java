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

    private static final String[] IMAGE_TYPES = new String[] { ".png", ".gif", ".jpg", ".swf", ".fla", ".psd", ".PNG", ".GIF", ".JPG" };
    private static final String[] FILE_TYPES = new String[] { "mxml", "as", "css" };
    
    private static final String IMAGE_TARGET_PATH = "/Users/hanxirui/Documents/workspace/workshop_bmc/RIIL_Flex_Project/trunk/sources/ALLIMAGE/";
    private static final String flexProjectPath = "/Users/hanxirui/Documents/workspace/workshop_bmc/RIIL_Flex_Project/trunk/sources/Business/RiilBusinessTopology/src/";
    public static void main(String[] args) {
        
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
                try {
                    for (String imgType : IMAGE_TYPES) {
                        if (tempString.indexOf(imgType) > 0) {
                            int t_shuangyinhao_endindex = tempString.indexOf("\"", tempString.indexOf(imgType));
                            int t_danyinhao_endindex = tempString.indexOf("\'", tempString.indexOf(imgType));
                            int t_endIndex = 0;
                            if (t_shuangyinhao_endindex > 0 && t_danyinhao_endindex > 0) {
                                t_endIndex = t_shuangyinhao_endindex > t_danyinhao_endindex ? t_danyinhao_endindex
                                        : t_shuangyinhao_endindex;
                            } else {
                                t_endIndex = t_shuangyinhao_endindex > t_danyinhao_endindex ? t_shuangyinhao_endindex
                                        : t_danyinhao_endindex;
                            }

                            String t_tempPath = tempString.substring(0, t_endIndex);

                            int t_shuangyinhao_beginindex = t_tempPath.lastIndexOf("\"");
                            int t_danyinhao_beginindex = t_tempPath.lastIndexOf("\'");
                            int t_beginIndex = t_shuangyinhao_beginindex < t_danyinhao_beginindex ? t_danyinhao_beginindex
                                    : t_shuangyinhao_beginindex;

                            String t_imgPath = t_tempPath.substring(t_beginIndex+1);
                            
//                            System.out.println("Image Path -------------- " + t_imgPath);
                            String t_srcImagePath = flexProjectPath+t_imgPath;
                            String t_targetImagePath = IMAGE_TARGET_PATH+t_imgPath;
                            copyImage(t_srcImagePath,t_targetImagePath);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("File ------------------ " + flexFile.getAbsolutePath());
                    System.err.println("line " + line + ": " + tempString);
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
            createFilePath(target.substring(0,target.lastIndexOf("/")));
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
    
    /** 
     * 创建文件夹（根据路径级联创建，如果目录的上一级目录不存在则按路径创建） 
     */  
    private static boolean createFilePath(String path) {  
        StringBuffer returnStr = new StringBuffer();
        boolean bool = false;
        // 根据符号"/"来分隔路径  
        String[] paths = path.split("/");  
        int length = paths.length;  
        for (int i = 0; i < length; i++) {  
            returnStr.append(paths[i]);  
            File file = new File(returnStr.toString());  
            if (!file.isDirectory()) {  
                bool = file.mkdir();  
            }  
            returnStr.append("/");  
        }  
        return bool;  
    }  
}
