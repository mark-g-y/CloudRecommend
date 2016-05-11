package com.cloudrecommend;


import com.cloudrecommend.communications.Server;
import com.cloudrecommend.jsonutil.JSON;
import com.cloudrecommend.models.Task;
import org.json.JSONObject;

public class TaskAssignmentThread extends Thread {

    private Server messageServer;
    private TaskQueue taskQueue;
    private AvailableWorkersQueue workersQueue;

    public TaskAssignmentThread(Server messageServer) {
        this.taskQueue = TaskQueue.getInstance();
        this.workersQueue = AvailableWorkersQueue.getInstance();
        this.messageServer = messageServer;

        //<TODO> debug only
        taskQueue.add(new Task("B", 3600000));
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Task task = taskQueue.take();
                System.out.println("Took task " + task.getGroup());
                final String workerId = workersQueue.take();
                System.out.println("Took worker " + workerId);
                JSONObject message = new JSONObject();
                JSON.put(message, "message", "new_task");
                JSON.put(message, "group_id", task.getGroup());
                messageServer.sendMessage(workerId, message.toString());
                task.updateExecTime();
                taskQueue.add(task);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void runInstance(Server messageServer) {
        TaskAssignmentThread t = new TaskAssignmentThread(messageServer);
        t.start();
    }

}
