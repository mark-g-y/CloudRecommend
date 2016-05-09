
package com.cloudrecommend.taskreceiver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class TaskReceiver {

    private Server server;

    public TaskReceiver(int port) {
        server = new Server(port);
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(TaskServlet.class, "/task");
        server.setHandler(handler);
    }

    public void start() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
    
}