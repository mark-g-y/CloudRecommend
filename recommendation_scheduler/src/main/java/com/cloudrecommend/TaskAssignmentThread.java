package com.cloudrecommend;


import com.cloudrecommend.communications.Server;
import com.cloudrecommend.db.MongoConfig;
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
    }

    @Override
    public void run() {
        loadTasksFromDb();
        while (true) {
            try {
                final Task task = taskQueue.take();
                if (task.getExecTime() > System.currentTimeMillis()) {
                    taskQueue.put(task);
                    continue;
                }
                System.out.println("Took task " + task.getSite());
                final String workerId = workersQueue.take();
                System.out.println("Took worker " + workerId);
                JSONObject message = new JSONObject();
                JSON.put(message, "message", "new_task");
                JSON.put(message, "site_id", task.getSite());
                messageServer.sendMessage(workerId, message.toString());
                task.updateExecTime();
                taskQueue.add(task);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadTasksFromDb() {
        TaskQueue queue = TaskQueue.getInstance();
        queue.addAll(Task.getAllFromDb(MongoConfig.getInstance().host(), MongoConfig.getInstance().port()));
    }

    public static void runInstance(Server messageServer) {
        TaskAssignmentThread t = new TaskAssignmentThread(messageServer);
        t.start();
    }

}
