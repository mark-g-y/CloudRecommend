package com.cloudrecommend.db;

public class MongoConfig {

    private static MongoConfig mongoConfig;
    private String mongoHost;
    private int mongoPort;

    private MongoConfig() {}

    public void init(String mongoHost, int mongoPort) {
        this.mongoHost = mongoHost;
        this.mongoPort = mongoPort;
    }

    public String host() {
        return mongoHost;
    }

    public int port() {
        return mongoPort;
    }

    public static MongoConfig getInstance() {
        if (mongoConfig == null) {
            mongoConfig = new MongoConfig();
        }
        return mongoConfig;
    }
}
