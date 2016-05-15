
package com.cloudrecommend;

import com.cloudrecommend.communications.*;
import com.cloudrecommend.db.MongoConfig;
import com.cloudrecommend.models.Task;

public class RecommendationScheduler {

    public RecommendationScheduler() {
    }

    public void start(int serverPort) {
        Server messageServer = new Server(serverPort, new MessageReceiverImpl());
        messageServer.start();
        TaskAssignmentThread.runInstance(messageServer);
    }

    public static void main(String[] arg) {
        int serverPort = Integer.parseInt(arg[0]);
        String mongoHost = arg[1];
        int mongoPort = Integer.parseInt(arg[2]);

        MongoConfig.getInstance().init(mongoHost, mongoPort);

        RecommendationScheduler recommendationScheduler = new RecommendationScheduler();
        recommendationScheduler.start(serverPort);
    }
}