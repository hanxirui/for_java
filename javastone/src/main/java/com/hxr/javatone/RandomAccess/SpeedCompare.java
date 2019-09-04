package com.hxr.javatone.RandomAccess;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 2017/6/18
 * Time: 下午4:54
 */
public class SpeedCompare {

	private static void Loop(List<Integer> list) {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < list.size(); i++) {
			list.get(i);
		}
		System.out.println(list.getClass().getSimpleName() + "使用普通for循环遍历时间为"
				+ (System.currentTimeMillis() - startTime) + "ms");

		startTime = System.currentTimeMillis();


        Iterator iterator = list.iterator();
        while(iterator.hasNext()){  //  it equals  for (Integer i : list)
            iterator.next();
        }


		System.out.println(list.getClass().getSimpleName() + "使用foreach循环遍历时间为"
				+ (System.currentTimeMillis() - startTime) + "ms");
	}

	private static void RefactorLoop(List<Integer> list) {
		long startTime = System.currentTimeMillis();
		if (list instanceof RandomAccess) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i);
			}
			System.out.println(list.getClass().getSimpleName() + "使用普通for循环遍历时间为"
					+ (System.currentTimeMillis() - startTime) + "ms");

		} else {
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) { // it equals for (Integer i : list)
				iterator.next();
			}

			System.out.println(list.getClass().getSimpleName() + "使用foreach循环遍历时间为"
					+ (System.currentTimeMillis() - startTime) + "ms");

		}
	}

    private static int SIZE = 111111;

	public static void main(String[] args) {
		List<Integer> arrayList = new ArrayList<Integer>(SIZE);
		List<Integer> linkedList = new LinkedList<Integer>();

		for (int i = 0; i < SIZE; i++) {
			arrayList.add(i);
			linkedList.add(i);
		}

		Loop(arrayList);
		Loop(linkedList);

        RefactorLoop(arrayList);
        RefactorLoop(linkedList);
		System.out.println();
	}
}
