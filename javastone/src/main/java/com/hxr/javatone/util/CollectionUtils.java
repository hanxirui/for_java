package com.hxr.javatone.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

    /**
     * 将一个List按照固定的大小拆成很多个小的List
     * 
     * @param listObj 需要拆分的List
     * @param groupNum 每个List的最大长度
     * @return 拆分后的List的集合
     */
    public static <T> List<List<T>> getSubList(final List<T> listObj, final int groupNum) {
        List<List<T>> resultList = new ArrayList<List<T>>();
        // 获取需要拆分的List个数
        int loopCount =
            (listObj.size() % groupNum == 0) ? (listObj.size() / groupNum) : ((listObj.size() / groupNum) + 1);
        // 开始拆分
        for (int i = 0; i < loopCount; i++) {
            // 子List的起始值
            int startNum = i * groupNum;
            // 子List的终止值
            int endNum = (i + 1) * groupNum;
            // 不能整除的时候最后一个List的终止值为原始List的最后一个
            if (i == loopCount - 1) {
                endNum = listObj.size();
            }
            // 拆分List
            List<T> listObjSub = listObj.subList(startNum, endNum);
            // 保存差分后的List
            resultList.add(listObjSub);
        }
        return resultList;

    }

    /**
     * java list拆分最终确定版
     * 
     * @param args
     */
    public static void main(final String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        int sizes = 122; // sizes是一个动态变量 测试的时候先写死
        for (int i = 1; i <= sizes; i++)
            list.add(i);
    }
}
