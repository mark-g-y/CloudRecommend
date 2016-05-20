
package com.cloudrecommend;

import com.cloudrecommend.communications.*;
import com.cloudrecommend.db.MongoConfig;
import com.cloudrecommend.models.Task;
import com.cloudrecommend.util.ParseUtils;

public class RecommendationScheduler {

    public RecommendationScheduler() {
    }

    public void start(int serverPort) {
        Server messageServer = new Server(serverPort, new MessageReceiverImpl());
        messageServer.start();
        TaskAssignmentThread.runInstance(messageServer);
    }

    public static void main(String[] args) {
        if (args.length != 3 || !ParseUtils.isInt(args[0]) || !ParseUtils.isInt(args[2])) {
            System.out.println("ERROR - arguments are <myPort> <mongoDbHost> <mongoDbPort>");
            System.exit(1);
        }
        int serverPort = Integer.parseInt(args[0]);
        String mongoHost = args[1];
        int mongoPort = Integer.parseInt(args[2]);

        MongoConfig.getInstance().init(mongoHost, mongoPort);

        RecommendationScheduler recommendationScheduler = new RecommendationScheduler();
        recommendationScheduler.start(serverPort);
    }
}