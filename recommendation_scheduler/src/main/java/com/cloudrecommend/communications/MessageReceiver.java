package com.cloudrecommend.communications;


public interface MessageReceiver {
    public void receive(String message, MessageSender sender);
}
