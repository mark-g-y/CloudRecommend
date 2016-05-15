package com.cloudrecommend.communications;


import com.cloudrecommend.AvailableWorkersQueue;
import com.cloudrecommend.models.Task;
import com.cloudrecommend.TaskQueue;
import com.cloudrecommend.jsonutil.JSON;
import org.json.JSONObject;

public class MessageReceiverImpl implements MessageReceiver {

    private TaskQueue taskQueue;
    private AvailableWorkersQueue workersQueue;

    public MessageReceiverImpl() {
        taskQueue = TaskQueue.getInstance();
        workersQueue = AvailableWorkersQueue.getInstance();
    }

    @Override
    public void receive(String clientId, String messageStr, MessageSender sender) {
        JSONObject message = JSON.create(messageStr);
        System.out.println("received message " + message);
        if ("new_task".equals(JSON.getString(message, "message"))) {
            String group = JSON.getString(message, "group");
            long delayBetweenExec = JSON.getLong(message, "delayBetweenExec");
            taskQueue.add(new Task(group, delayBetweenExec));
        } else if ("worker_ready".equals(JSON.getString(message, "message"))) {
            workersQueue.add(clientId);
        }
    }
}
