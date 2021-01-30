package com.newland.boss.cloud.web.util;

import java.io.IOException;
import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.newland.boss.cloud.web.bds.dao.AppViewDao;
import com.newland.boss.cloud.web.config.AppConfig;

public class ZkUtil {

    private static Log logger = LogFactory.getLog(ZkUtil.class);
    static AppViewDao appViewDao = new AppViewDao();
    public static ZooKeeper zk;

    @PostConstruct
    public void init() throws Exception {
        try {
            String flag = AppConfig.getProperty("zk.switch").toUpperCase();
            if (flag.equals("TRUE")) {
                connectZookeeper();
                zk.addAuthInfo("digest", "admin:fmbs3_adm".getBytes());
            }
        } catch (IOException e) {
            logger.info("init zookeeper fail!");
        }

    }

    /**
     * 连接zookeeper
     *
     * @return
     * @throws IOException
     */
    protected ZooKeeper connectZookeeper() throws IOException {
        if (null == zk) {
            logger.info("connect to zookeeper.");
            String zkHost = AppConfig.getProperty("zk.connect.host");
            String zkTimeout = AppConfig.getProperty("zk.connect.timeout");
            int timeout = Integer.parseInt(zkTimeout);
            zk = new ZooKeeper(zkHost, timeout, new Watcher() {
                public void process(WatchedEvent event) {
                    logger.debug(" receive event : " + event.getType().name());
                }
            });
            logger.info("connect to zookeeper completed.");
        }
        return zk;
    }

    /**
     * 根据端口连接zookeeper
     *
     * @return
     * @throws IOException
     */
    protected ZooKeeper connectZookeeper(String connectHost) throws IOException {
        logger.info("connect to zookeeper.");
        String zkHost = connectHost == null ? AppConfig.getProperty("zk.connect.host") : connectHost;
        logger.info("zkHost" + zkHost);
        String zkTimeout = AppConfig.getProperty("zk.connect.timeout");
        int timeout = Integer.parseInt(zkTimeout);
        zk = new ZooKeeper(zkHost, timeout, new Watcher() {
            public void process(WatchedEvent event) {
                logger.debug(" receive event : " + event.getType().name());
            }
        });
        logger.info("connect to zookeeper completed.");

        return zk;
    }

    /**
     * 关闭zookeeper
     *
     * @return
     */
    public boolean disconnect() {
        if (zk != null) {
            try {
                zk.close();
                zk = null;
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            logger.error("zk is not init");
        }
        return false;
    }

    ;

}
