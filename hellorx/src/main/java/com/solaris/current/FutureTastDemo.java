package com.solaris.current;

import java.util.concurrent.*;

/**
 * Created by hanxirui on 2017/6/26.
 */
public class FutureTastDemo {
    public static void main(String[] args) {
        //第一种方式
        ExecutorService executor = Executors.newCachedThreadPool();
        Task task = new Task();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
        executor.submit(futureTask);
//        第二种方式
//        Future<Integer> futureTask = executor.submit(task);
        executor.shutdown();


        //第三种方式，注意这种方式和第一种方式效果是类似的，只不过一个使用的是ExecutorService，一个使用的是Thread
        /*Task task = new Task();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
        Thread thread = new Thread(futureTask);
        thread.start();*/

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");

//        futureTask.cancel(true);

        try {
            System.out.println("task运行结果" + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }
}

class Task implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算");
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 100; i++)
            sum += i;
        return sum;
    }
}


//    没有用到锁
//    private ConcurrentHashMap<String,FutureTask<Connection>>connectionPool = new ConcurrentHashMap<String, FutureTask<Connection>>();
//
//    public Connection getConnection(String key) throws Exception{
//        FutureTask<Connection>connectionTask=connectionPool.get(key);
//        if(connectionTask!=null){
//            return connectionTask.get();
//        }
//        else{
//            Callable<Connection> callable = new Callable<Connection>(){
//                @Override
//                public Connection call() throws Exception {
//                    // TODO Auto-generated method stub
//                    return createConnection();
//                }
//            };
//            FutureTask<Connection>newTask = new FutureTask<Connection>(callable);
//            connectionTask = connectionPool.putIfAbsent(key, newTask);

//            创建connection的动作在放入map之后
//            if(connectionTask==null){
//                connectionTask = newTask;
//                connectionTask.run();
//            }
//            return connectionTask.get();
//        }
//    }
//
//    //创建Connection
//    private Connection createConnection(){
//        return null;
//    }