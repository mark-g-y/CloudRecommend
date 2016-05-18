package com.cloudrecommend.models;

import com.cloudrecommend.db.MongoConfig;
import com.mongodb.*;

import java.util.ArrayList;

public class Task implements Comparable {

    private String group;
    private long delayBetweenExec;
    private long execTime;

    private MongoConfig mongoConfig = MongoConfig.getInstance();;

    public Task(String group, long delayBetweenExec) {
        this(group, delayBetweenExec, System.currentTimeMillis() - delayBetweenExec);
    }

    public Task(String group, long delayBetweenExec, long lastRun) {
        this.group = group;
        this.delayBetweenExec = delayBetweenExec;
        this.execTime = lastRun + delayBetweenExec;
    }

    public String getGroup() {
        return group;
    }

    public long getExecTime() {
        return execTime;
    }

    public void updateExecTime() {
        execTime += delayBetweenExec;
        System.out.println(mongoConfig);
        Mongo mongo = new Mongo(mongoConfig.host(), mongoConfig.port());
        DB db = mongo.getDB("cloudrecommend");
        DBCollection sites = db.getCollection("site");
        BasicDBObject query = new BasicDBObject().append("uid", group);
        BasicDBObject update = new BasicDBObject().append("$set", new BasicDBObject("last_run", System.currentTimeMillis()));
        sites.update(query, update);
        mongo.close();
    }

    @Override
    public int compareTo(Object o) {
        Task oTask = (Task)o;
        if (execTime < oTask.getExecTime()) {
            return -1;
        } else if (execTime > oTask.getExecTime()) {
            return 1;
        }
        return 0;
    }

    public static ArrayList<Task> getAllFromDb(String mongoHost, int mongoPort) {
        Mongo mongo = new Mongo(mongoHost, mongoPort);
        DB db = mongo.getDB("cloudrecommend");
        DBCollection sites = db.getCollection("site");
        DBCursor cursor = sites.find();
        ArrayList<Task> tasks = new ArrayList<Task>();
        while(cursor.hasNext()) {
            DBObject site = cursor.next();
            String uid = (String)site.get("uid");
            long delayBetweenRuns = delayToLong((String)site.get("delay_between_runs"));
            long lastRun = (Long)site.get("last_run");
            tasks.add(new Task(uid, delayBetweenRuns, lastRun));
        }
        cursor.close();
        mongo.close();
        return tasks;
    }

    private static long delayToLong(String delay) {
        if ("daily".equals(delay)) {
            return 1000 * 60 * 60 * 24;
        } else if ("weekly".equals(delay)) {
            return 1000 * 60 * 60 * 24 * 7;
        } else if ("biweekly".equals(delay)) {
            return 1000 * 60 * 60 * 24 * 14;
        }
        return Long.MAX_VALUE;
    }
}
