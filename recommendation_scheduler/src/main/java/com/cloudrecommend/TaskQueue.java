
package com.cloudrecommend;

import com.cloudrecommend.models.Task;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskQueue extends PriorityBlockingQueue<Task> {

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