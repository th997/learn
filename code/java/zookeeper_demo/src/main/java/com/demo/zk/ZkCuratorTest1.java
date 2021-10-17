package com.demo.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ZkCuratorTest1 {

    private static CuratorFramework getZkClient() {
        String zkServerAddress = "172.16.16.21:2181,172.16.16.22:2181,172.16.16.23:2181";
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkServerAddress)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;
    }

    public static void main(String[] args) {
        CuratorFramework zkClient = getZkClient();
        String lockPath = "/lock";
       // InterProcessLock lock = new InterProcessSemaphoreMutex(zkClient, lockPath);//排它锁
        InterProcessLock lock = new InterProcessMutex(zkClient, lockPath); // 可重入排它锁
        // InterProcessReadWriteLock lock = new InterProcessReadWriteLock(zkClient, lockPath);//读写锁
        //模拟50个线程抢锁
        for (int i = 0; i < 50; i++) {
            new Thread(new TestThread(i, lock)).start();
        }
    }


    static class TestThread implements Runnable {
        private Integer threadFlag;
        private InterProcessLock lock;

        public TestThread(Integer threadFlag, InterProcessLock lock) {
            this.threadFlag = threadFlag;
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                lock.acquire();
                lock.acquire();
                System.out.println("第" + threadFlag + "线程获取到了锁");
                //等到1秒后释放锁
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    lock.release();
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}