package com.cloudrecommend;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RecommendationTaskExecutor {

    private String hdfsUri;

    public RecommendationTaskExecutor(String hdfsUri) {
        this.hdfsUri = "hdfs://" + hdfsUri + "/";
    }

    public void execute(final Task task, final Runnable r) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                shellExec("python run_recommendation.py " + hdfsUri + task.getGroup());
                r.run();
            }
        });
        t.start();
    }

    private void shellExec(String command) {
        try {
            System.out.println("executing command - " + command);
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor();
            System.out.println("done executing");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
