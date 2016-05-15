
package com.cloudrecommend;

import com.cloudrecommend.communications.*;
import com.cloudrecommend.db.MongoDbUtil;

public class RecommendationScheduler {

    public RecommendationScheduler() {
    }

    public void start(int serverPort) {
        Server messageServer = new Server(serverPort, new MessageReceiverImpl());
        messageServer.start();
        TaskAssignmentThread.runInstance(messageServer);
    }

    public void loadTasksFromDb(String mongoHost, int mongoPort) {
        TaskQueue queue = TaskQueue.getInstance();
        queue.addAll(MongoDbUtil.getTasks(mongoHost, mongoPort));
    }

    public static void main(String[] arg) {
        int serverPort = Integer.parseInt(arg[0]);
        String mongoHost = arg[1];
        int mongoPort = Integer.parseInt(arg[2]);

        RecommendationScheduler recommendationScheduler = new RecommendationScheduler();
        recommendationScheduler.loadTasksFromDb(mongoHost, mongoPort);
        recommendationScheduler.start(serverPort);
    }
}