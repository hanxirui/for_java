package com.hxr.javatone.rpc.distribution.registry;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hxr.javatone.rpc.distribution.Constant;

//第五步：实现服务注册
//
//使用 ZooKeeper 客户端可轻松实现服务注册功能， ServiceRegistry 代码如下
//注意：首先需要使用 ZooKeeper 客户端命令行创建 /registry 永久节点，用于存放所有的服务临时节点。
public class ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);
    private final CountDownLatch latch = new CountDownLatch(1);
    private final String registryAddress;
    public ServiceRegistry(final String registryAddress) {
      this.registryAddress = registryAddress;
    }
    public void register(final String data) {
      if (data != null) {
        ZooKeeper zk = connectServer();
        if (zk != null) {
          createNode(zk, data);
        }
      }
    }
    private ZooKeeper connectServer() {
      ZooKeeper zk = null;
      try {
        zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
          @Override
          public void process(final WatchedEvent event) {
            if (event.getState() == Event.KeeperState.SyncConnected) {
              latch.countDown();
            }
          }
        });
        latch.await();
      } catch (IOException | InterruptedException e) {
        LOGGER.error("", e);
      }
      return zk;
    }
    private void createNode(final ZooKeeper zk, final String data) {
      try {
        byte[] bytes = data.getBytes();
        String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        LOGGER.debug("create zookeeper node ({} => {})", path, data);
      } catch (KeeperException | InterruptedException e) {
        LOGGER.error("", e);
      }
    }
  }
