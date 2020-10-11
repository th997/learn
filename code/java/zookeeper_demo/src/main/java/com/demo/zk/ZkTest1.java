package com.demo.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
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
                        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                            public void process(WatchedEvent event) {
                                System.out.println(event.toString());
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return zk;
    }

    private void printAll(String path) throws KeeperException, InterruptedException {
        byte[] data = this.getZk().getData(path, false, new Stat());
        System.out.println(path + "\t=\t" + new String(data));
        List<String> list = this.getZk().getChildren(path, false);
        for (String ch : list) {
            String chPath = path.endsWith("/") ? (path + ch) : (path + "/" + ch);
            printAll(chPath);
        }
    }


    public static void main(String[] args) throws Exception {
        ZkTest1 test = new ZkTest1();
        test.printAll("/");

    }


}