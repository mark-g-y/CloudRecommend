package com.cloudrecommend.communications;


public interface MessageReceiver {
    public void receive(String clientId, String message, MessageSender sender);
}
