
package com.cloudrecommend;

import com.cloudrecommend.taskreceiver.TaskReceiver;

public class RecommendationScheduler {

    private final TaskQueue queue;
    private RecommendationTaskExecutor executor;

    public RecommendationScheduler(String hdfsUri) {
        queue = TaskQueue.getInstance();
        executor = new RecommendationTaskExecutor(hdfsUri);
    }

    public void start() {
        while (true) {
            try {
                final Task task = queue.take();
                System.out.println("Took task " + task.getGroup());
                executor.execute(task, new Runnable() {
                    @Override
                    public void run() {
                        task.updateExecTime();
                        queue.add(task);
                    }
                });
                Thread.sleep(5000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] arg) {
        String hdfsUri = arg[0];
        int taskReceiverPort = Integer.parseInt(arg[1]);

        TaskReceiver taskReceiver = new TaskReceiver(taskReceiverPort);
        taskReceiver.start();

        RecommendationScheduler recommendationScheduler = new RecommendationScheduler(hdfsUri);
        recommendationScheduler.start();
    }
}