
package com.cloudrecommend;

import java.util.concurrent.PriorityBlockingQueue;

public class TaskQueue extends PriorityBlockingQueue<com.cloudrecommend.models.Task> {

    private static TaskQueue taskQueue;
    
    private TaskQueue() {
    }

    public static TaskQueue getInstance() {
        if (taskQueue == null) {
            taskQueue = new TaskQueue();
        }
        return taskQueue;
    }
}