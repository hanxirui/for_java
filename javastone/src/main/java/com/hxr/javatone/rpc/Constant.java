package com.hxr.javatone.rpc;

//通过 Constant 配置了所有的常量
//注意：首先需要使用 ZooKeeper 客户端命令行创建 /registry 永久节点，用于存放所有的服务临时节点。
public interface Constant {

    int ZK_SESSION_TIMEOUT = 5000;

    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
}