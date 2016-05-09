package com.cloudrecommend;

import org.apache.pig.ExecType;
import org.apache.pig.PigServer;
import org.apache.pig.data.Tuple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

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
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((reader.readLine()) != null) {}
            p.waitFor();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
