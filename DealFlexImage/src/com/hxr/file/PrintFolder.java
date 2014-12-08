package com.hxr.file;

import java.io.File;

public class PrintFolder {
    public  StringBuffer strDir = new StringBuffer();
    private int fileCount = 0;
    private int dirCount = 0;
    public static void main(String[] args) {
        PrintFolder pf = new PrintFolder();
        FileNameFilter filter = new FileNameFilter() {
            
            @Override
            public boolean accept(String filename) {
               return false;
            }
        };
        pf.travelDir(new File("/Users/hanxirui/Documents/workspace/workshop_bmc/workspace/resources"), 0, ' ', filter);
        System.out.println(pf.strDir.toString());
    }
    /**
     * 参数：root:要遍历的文件，
         *spanToLeft：距离左边的空白或者分隔符数目，
         *spanChar：空白字符， 
         *filter：根据条件选择文件
     * 
     */
    private void travel(File root, int spanToLeft, char spanChar,
            FileNameFilter filter) {
        spanToLeft += 20;
        if (root.isFile()) {
            String name = root.getName();
            if (filter.accept(name)) {
                for (int k = 0; k < spanToLeft; k++) {
                    strDir.append(spanChar);
                }
                strDir.append(root.getName() + "\r\n");
                fileCount++;
            }
        } else if (root.isDirectory()) {
            for (int k = 0; k < spanToLeft; k++) {
                strDir.append(spanChar);
            }
            strDir.append(root.getName() + "\r\n");
            dirCount++;
            File[] children = root.listFiles();
            if (children == null)
                return;
            for (int i = 0; i < children.length; i++) {
                travel(children[i], spanToLeft, spanChar, filter);
            }
        }
    }
    
    /**
     * {method description}.
     * @param root
     * @param spanToLeft
     * @param spanChar
     * @param filter
     */
    public void travelDir(File root, int spanToLeft, char spanChar,
            FileNameFilter filter){
        if (root == null)
            return;
        if (!root.exists()) {
            System.err.println("file " + root.getName() + " does not exist!");
            return;
        }
        travel(root, spanToLeft, spanChar, filter);}
}
