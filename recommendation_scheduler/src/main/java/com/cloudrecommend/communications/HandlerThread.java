package com.cloudrecommend.communications;

import java.io.*;
import java.net.Socket;

public class HandlerThread extends Thread implements MessageSender {

    private String id;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private MessageReceiver messageReceiver;

    public HandlerThread(String id, Socket socket, MessageReceiver messageReceiver) {
        super();
        try {
            this.id = id;
            this.messageReceiver = messageReceiver;
            this.socket = socket;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(), true);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while((message = input.readLine()) != null) {
                System.out.println(id + " received message: " + message);
                messageReceiver.receive(id, message, this);
            }
            close();
        } catch(Exception e) {
        }
        close();
    }

    private void close() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch(Exception e) {
        }
    }

    @Override
    public void sendMessage(String message) {
        try {
            output.println(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
