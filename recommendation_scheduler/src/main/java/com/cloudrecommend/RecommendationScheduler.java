
package com.cloudrecommend;

import com.cloudrecommend.communications.*;

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

        RecommendationScheduler recommendationScheduler = new RecommendationScheduler();
        recommendationScheduler.start(serverPort);
    }
}