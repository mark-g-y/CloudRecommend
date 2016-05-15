package com.cloudrecommend;


import com.cloudrecommend.hbase.ZookeeperConfig;
import com.cloudrecommend.servlets.EventServlet;
import com.cloudrecommend.servlets.RecommendationForItemServlet;
import com.cloudrecommend.servlets.RecommendationForUserServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Api {

    public static void main(String[] arg) {

        String zookeeperHost = arg[0];
        int zookeeperPort = Integer.parseInt(arg[1]);
        ZookeeperConfig.getInstance().init(zookeeperHost, zookeeperPort);

        Server server = new Server(1739);
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(EventServlet.class, "/event");
        handler.addServletWithMapping(RecommendationForUserServlet.class, "/recommendation/user");
        handler.addServletWithMapping(RecommendationForItemServlet.class, "/recommendation/item");
        server.setHandler(handler);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
