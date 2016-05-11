package com.cloudrecommend.communications;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HandlerThread extends Thread implements MessageSender {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private MessageReceiver messageReceiver;

    public HandlerThread(Socket socket, MessageReceiver messageReceiver) {
        super();
        try {
            this.messageReceiver = messageReceiver;
            this.socket = socket;
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while((message = (String)ois.readObject()) != null) {
                messageReceiver.receive(message, this);
            }
            ois.close();
            oos.close();
            socket.close();
        } catch(Exception e) {
        }
        close();
    }

    private void close() {
        try {
            ois.close();
            oos.close();
            socket.close();
        } catch(Exception e) {
        }
    }

    @Override
    public void sendMessage(String message) {
        try {
            oos.writeObject(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
