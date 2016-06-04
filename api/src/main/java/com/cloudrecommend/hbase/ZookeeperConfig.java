package com.cloudrecommend.hbase;


public class ZookeeperConfig {

    private static ZookeeperConfig zookeeperConfig;
    private String host;
    private int port;

    private ZookeeperConfig() {}

    public void init(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public static ZookeeperConfig getInstance() {
        if (zookeeperConfig == null) {
            zookeeperConfig = new ZookeeperConfig();
        }
        return zookeeperConfig;
    }
}
