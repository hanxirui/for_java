package com.hxr.javatone.memory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/** @Described：堆溢出测试 @VM args:-verbose:gc -Xms20M -Xmx20M -XX:+PrintGCDetails
 * 
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=c:/memoryLeakDemo.hprof
-XX:+PrintGCDetails 
-XX:+PrintGCDateStamps 
-XX:+PrintGCApplicationConcurrentTime
-XX:+PrintGCApplicationStoppedTime  
Java 堆内存的OutOfMemoryError异常是实际应用中最常见的内存溢出异常情况。出现Java 堆内
存溢出时，异常堆栈信息“java.lang.OutOfMemoryError”会跟着进一步提示“Java heap

space”。

要解决这个区域的异常，一般的手段是首先通过内存映像分析工具（如Eclipse

Memory Analyzer）对dump 出来的堆转储快照进行分析，重点是确认内存中的对象是

否是必要的，也就是要先分清楚到底是出现了内存泄漏（Memory Leak）还是内存溢

出（Memory Overflow）。

如果是内存泄漏，可进一步通过工具查看泄漏对象到GC Roots 的引用链。于是就

能找到泄漏对象是通过怎样的路径与GC Roots 相关联并导致垃圾收集器无法自动回收

它们的。掌握了泄漏对象的类型信息，以及GC Roots 引用链的信息，就可以比较准确

地定位出泄漏代码的位置。

如果不存在泄漏，换句话说就是内存中的对象确实都还必须存活着，那就应当检查

虚拟机的堆参数（-Xmx 与-Xms），与机器物理内存对比看是否还可以调大，从代码上

检查是否存在某些对象生命周期过长、持有状态时间过长的情况，尝试减少程序运行期

的内存消耗。
 */

public class HeapOutOfMemory {

    /** @param args @Author YHJ create at 2011-11-12 下午07:52:18
     */

    public static void main(final String[] args) {

        List<TestCaseBuffer> cases = new ArrayList<TestCaseBuffer>();
        int i = 1;
        while (true) {

            cases.add(new TestCaseBuffer());
            System.out.println(i++);
        }

    }

}

class TestCaseBuffer {

    ByteBuffer b = ByteBuffer.allocate(1024 * 1000);

}
