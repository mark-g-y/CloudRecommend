package com.cloudrecommend.communications;

import java.net.ServerSocket;
import java.util.HashMap;

public class Server {

    private int port;
    private int clientIdCounter = 0;
    private MessageReceiver receiver;
    private HashMap<String, HandlerThread> connections;

    public Server(int port, MessageReceiver receiver) {
        this.port = port;
        this.receiver = receiver;
        this.connections = new HashMap<String, HandlerThread>();
    }

    public void start() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket socket = new ServerSocket(port);
                    while(true) {
                        String id = Integer.toString(assignClientId());
                        HandlerThread thread = new HandlerThread(id, socket.accept(), receiver);
                        thread.start();
                        connections.put(id, thread);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void sendMessage(String clientId, String message) {
        connections.get(clientId).sendMessage(message);
    }

    private synchronized int assignClientId() {
        clientIdCounter++;
        return clientIdCounter;
    }

}
