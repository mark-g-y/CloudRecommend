package com.cloudrecommend.db;


import com.cloudrecommend.models.Task;
import com.mongodb.*;

import java.util.ArrayList;

public class MongoDbUtil {

    public static ArrayList<Task> getTasks(String mongoHost, int mongoPort) {
        Mongo mongo = new Mongo(mongoHost, mongoPort);
        DB db = mongo.getDB("recommendengineaas");
        DBCollection sites = db.getCollection("sites");
        DBCursor cursor = sites.find();
        ArrayList<Task> tasks = new ArrayList<Task>();
        while(cursor.hasNext()) {
            DBObject site = cursor.next();
            tasks.add(new Task((String)site.get("uid"), (Long)site.get("delay_between_runs"), (Long)site.get("last_run")));
        }
        cursor.close();
        mongo.close();
        return tasks;
    }
}
