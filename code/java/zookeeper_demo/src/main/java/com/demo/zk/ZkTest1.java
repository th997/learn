package com.demo.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ZkTest1 {

    //https://blog.csdn.net/qq_35190492/article/details/105352672

    private static final String connectString = "172.16.16.21:2181,172.16.16.22:2181,172.16.16.23:2181";

    private static final int sessionTimeout = 3000;

    private static volatile ZooKeeper zk = null;

    private ZooKeeper getZk() {
        if (zk == null) {
            synchronized (this) {
                if (zk == null) {
                    try {
                        Watcher watcher = new Watcher() {
                            public void process(WatchedEvent event) {
                                System.out.println(event.toString());
                            }
                        };
                        zk = new ZooKeeper(connectString, sessionTimeout, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return zk;
    }

    public void printAll(String path) throws KeeperException, InterruptedException {
        byte[] data = this.getZk().getData(path, false, new Stat());
        System.out.println(path + "\t=\t" + new String(data));
        List<String> list = this.getZk().getChildren(path, false);
        for (String ch : list) {
            String chPath = path.endsWith("/") ? (path + ch) : (path + "/" + ch);
            printAll(chPath);
        }
    }

    public boolean lock(String lockPath) {
        try {
            getZk().create(lockPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            return true;
        } catch (KeeperException e) {
            // locked
            e.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public boolean unlock(String lockPath) {
        try {
            getZk().delete(lockPath, -1);
            return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public boolean lock1(String lockPath) {
        try {
            String lock = getZk().create(lockPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> locks = getZk().getChildren(lockPath, false);
            Collections.sort(locks);
            return lock.equals(locks.get(0));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args) throws Exception {
        ZkTest1 test = new ZkTest1();
        // 打印所有节点
        test.printAll("/");
        // 分布式锁
        String lockPath = "/lock";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            //System.out.println(test.unlock(lockPath));
            System.out.println(test.lock1(lockPath));
        }
        System.out.println("lock time=" + (System.currentTimeMillis() - start));
        test.printAll(lockPath);
        //System.out.println(test.getZk().getChildren("/lock", false));

    }


}