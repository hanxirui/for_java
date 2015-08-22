package com.hxr.javatone.algorithms;

/**
 * 问题：设编号为1、2……n的几个小孩围坐一圈，约定编号为k（1=<k<=n）的小孩从1开始报数，数到m的那个出列，他的下一位又从1开始报数，数到m的那个人又出列，直到所有人出列为止，由此产生一个出队编号的序列。
 * 我们现在用一个双向环形链表来解这一问题。
 */
public class DoubleCycleLink {

    public static void main(final String[] args) {

        CycleLink cl = new CycleLink(5); // 构造环形链表
        cl.print();
        cl.setK(1); // 设置从第几个小孩开始数数
        cl.setM(3); // 设置数几下
        cl.play(); // 开始游戏

    }

}

class Child {

    int no;
    Child nextChild;
    Child previousChild;

    public Child(final int no) {

        this.no = no;

    }
}

class CycleLink {

    Child first;
    Child cursor;
    int length;

    // 从第几个小孩开始数
    private int k = 1;
    // 数几下
    private int m = 1;

    // 构造函数
    public CycleLink(final int len) {

        this.length = len;
        for (int i = 1; i <= length; i++) {

            Child ch = new Child(i);
            if (i == 1) {
                first = ch;
                cursor = ch;

            } else if (i < length) {

                cursor.nextChild = ch;
                ch.previousChild = cursor;
                cursor = ch;

            } else {

                cursor.nextChild = ch;
                ch.previousChild = cursor;
                cursor = ch;
                ch.nextChild = first;
                first.previousChild = ch;

            }
        }
    }

    // 打印链表
    public void print() {

        cursor = first;
        do {
            System.out.print(cursor.no + "<<");
            cursor = cursor.nextChild;

        } while (cursor != first);
        System.out.println();
    }

    // 开始游戏
    public void play() {

        Child temp;
        cursor = first;
        // 先找到第k个小孩
        while (cursor.no < k) {
            cursor = cursor.nextChild;
        }
        while (length > 1) {
            // 数m下
            for (int i = 1; i < m; i++) {

                cursor = cursor.nextChild;
            }
            System.out.println("小孩" + cursor.no + "出局了！");
            // 找到前一个小孩
            temp = cursor.previousChild;
            // 如果是单向链表
            // temp=cursor;
            // do{
            // temp=temp.nextChild;
            // }while(temp.nextChild!=cursor);
            temp.nextChild = cursor.nextChild;
            cursor.nextChild.previousChild = temp;
            cursor = cursor.nextChild;
            length--;
        }
        System.out.println("最后一个出局的小孩是" + cursor.no);

    }

    public void setK(final int k) {
        this.k = k;
    }

    public void setM(final int m) {
        this.m = m;
    }

}