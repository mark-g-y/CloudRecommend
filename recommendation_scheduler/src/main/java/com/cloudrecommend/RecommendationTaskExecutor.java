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
                shellExec("sh -c \"python affinity_generator.py\"");
                //<TODO> change from local to mapreduce
                shellExec("python run.py");
                //shellExec("sh -c \"pig -x local -param filename=" + hdfsUri + task.getGroup() + " recommend.pig\"");
//                try {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    //params.put("param", "filename=" + hdfsUri + task.getGroup());
//                    //params.put("x", "local");
//                    //params.put("filename", hdfsUri + task.getGroup());
//                    params.put("filename", "input.txt");
//                    Properties props = new Properties();
//                    props.setProperty("fs.default.name", hdfsUri);
//                    props.setProperty("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
//                    props.setProperty("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
//                    PigServer pigServer = new PigServer(ExecType.LOCAL, props);
//                    //pigServer.registerScript("recommend.pig2", params);
//                    pigServer.registerQuery("inp = load '/ecommercesiteA' using PigStorage(' ');");
//                    //pigServer.registerQuery("dump inp;");
//                    pigServer.store("inp", "/testing123");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                r.run();
            }
        });
        t.start();
    }

    private void shellExec(String command) {
        try {
            System.out.println("executing command - " + command);
            Process p = Runtime.getRuntime().exec(command);
            System.out.println("execute command began");
            String foo;
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((foo = reader.readLine()) != null) {
                System.out.println(foo);
            }
            p.waitFor();
            System.out.println("done executing");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
