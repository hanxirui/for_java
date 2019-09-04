package com.hxr.javatone.DeadLockSample;/** * Created with IntelliJ IDEA. * Project: java * Author: Kevin * Date: 16/4/15 * Time: 下午11:16 */public class DeadLockApp {    public static class CustomizeThreadFirst extends Thread {        private ResourceManager resourceManger;//资源管理类的私有引用，通过此引用可以通过其相关接口对资源进行读写        /**         *         * 创建一个新的实例 CustomizeThread.         * @param resourceManger         */        public CustomizeThreadFirst(ResourceManager resourceManger){            this.resourceManger = resourceManger;        }        /**         * 重写 java.lang.Thread 的 run 方法         */        public void run(){            synchronized(resourceManger.getResourceB()){                System.out.println( "线程1拿到了资源 resourceB 的对象锁");                try {                    sleep(1000);                    System.out.println("线程1拿到了资源 resourceB 的对象锁  等待A拿到造成死锁");                } catch (InterruptedException e) {                    e.printStackTrace();                }                synchronized(resourceManger.getResourceA()){                    System.out.println( "线程1拿到了资源 resourceA 的对象锁");                    resourceManger.doSomeThing();                }            }        }    }    public static class CustomizeThreadSecond extends Thread {        private ResourceManager resourceManger;//资源管理类的私有引用，通过此引用可以通过其相关接口对资源进行读写        /**         *         * 创建一个新的实例 CustomizeThread.         * @param resourceManger         */        public CustomizeThreadSecond(ResourceManager resourceManger){            this.resourceManger = resourceManger;        }        public void run(){            synchronized(resourceManger.getResourceA()){                System.out.println( "线程2拿到了资源 resourceB 的对象锁");                synchronized(resourceManger.getResourceB()){                    System.out.println("线程2拿到了资源 resourceA 的对象锁");                    resourceManger.doSomeThing();                }            }        }    }    public static void main(String[] args) {        ResourceManager resourceManager = new ResourceManager();        CustomizeThreadFirst customizedThread0 = new CustomizeThreadFirst(resourceManager);        CustomizeThreadSecond customizedThread1 = new CustomizeThreadSecond(resourceManager);        customizedThread0.start();        customizedThread1.start();    }}