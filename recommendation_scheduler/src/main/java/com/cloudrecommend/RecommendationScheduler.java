
package com.cloudrecommend;

import com.cloudrecommend.taskreceiver.TaskReceiver;

public class RecommendationScheduler {

    private final TaskQueue queue;
    private RecommendationTaskExecutor executor;

    public RecommendationScheduler(String hdfsUri) {
        queue = TaskQueue.getInstance();
        executor = new RecommendationTaskExecutor(hdfsUri);
    }

    private volatile boolean exit = false;

    public void start() {
        queue.add(new Task("ecommercesiteA", 5000));
        while (!exit) {
            try {
                final Task task = queue.take();
                if (task == null) {
                    continue;
                } else if (task.getExecTime() > System.currentTimeMillis()) {
                    queue.add(task);
                }
                System.out.println("Took task " + task.getGroup());
                executor.execute(task, new Runnable() {
                    @Override
                    public void run() {
                        task.updateExecTime();
                        queue.add(task);
                        exit = true;
                    }
                });
                Thread.sleep(120000);
                System.out.println("exiting");
                exit = true;
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] arg) {
        String hdfsUri = arg[0];
        int taskReceiverPort = Integer.parseInt(arg[1]);

        //TaskReceiver taskReceiver = new TaskReceiver(taskReceiverPort);
        //taskReceiver.start();

        RecommendationScheduler recommendationScheduler = new RecommendationScheduler(hdfsUri);
        recommendationScheduler.start();
    }
}