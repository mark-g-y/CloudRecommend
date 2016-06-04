package com.cloudrecommend;


import com.cloudrecommend.hbase.ZookeeperConfig;
import com.cloudrecommend.servlets.RecommendationForItemServlet;
import com.cloudrecommend.servlets.RecommendationForUserServlet;
import com.cloudrecommend.util.ParseUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Api {

    public static void main(String[] arg) {

        if (arg.length != 3 || !ParseUtils.isInt(arg[0]) || !ParseUtils.isInt(arg[2])) {
            System.out.println("ERROR - arguments are <myPort> <hbase_zookeeper_host> <hbase_zookeeper_port>");
            System.exit(1);
        }

        String zookeeperHost = arg[1];
        int zookeeperPort = Integer.parseInt(arg[2]);
        ZookeeperConfig.getInstance().init(zookeeperHost, zookeeperPort);

        Server server = new Server(Integer.parseInt(arg[0]));
        ServletHandler handler = new ServletHandler();
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
