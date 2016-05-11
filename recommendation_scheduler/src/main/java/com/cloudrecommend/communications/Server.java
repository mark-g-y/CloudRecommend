package com.cloudrecommend.communications;

import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
                        int id = assignClientId();
                        HandlerThread thread = new HandlerThread(socket.accept(), receiver);
                        connections.put(Integer.toString(id), thread);
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
