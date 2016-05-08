
package com.cloudrecommend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;

import org.json.JSONObject;

public class StreamProcessor implements Runnable {

    private int processId;
    private KafkaStream stream;

    public StreamProcessor(int processId, KafkaStream stream) {
        this.processId = processId;
        this.stream = stream;
    }

    @Override
    public void run() {
        consume();
    }

    private void consume() {
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            String message = new String(it.next().message());
            System.out.println(processId + ": " + message);
            logEvent(message);
        }
    }

    private void logEvent(String message) {

        try {
            JSONObject event = new JSONObject(message);

            StringBuilder eventLogBuilder = new StringBuilder();
            eventLogBuilder.append(event.getString("user")).append(" ")
                .append(event.getString("item")).append(" ")
                .append(event.getString("event")).append(" ")
                .append(System.currentTimeMillis()).append("\n");

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(cal.getTime());
            String eventGroup = event.getString("group");
            String path = eventGroup + "/" + date;

            Configuration conf = new Configuration();
            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
            conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "false");
            FileSystem hdfs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
            hdfs.mkdirs(new Path("hdfs://localhost:9000/" + eventGroup));
            Path file = new Path("hdfs://localhost:9000/" + path);
            if (!hdfs.exists(file)) {
                hdfs.create(file);
                hdfs.close();
                hdfs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
            }
            OutputStream os = hdfs.append(file);
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
            br.write(eventLogBuilder.toString());
            br.close();
            hdfs.close();

            //exec(String.format("hadoop fs -mkdir -p %s", eventGroup));
            //exec(String.format("echo '' | hdfs dfs -put - %s", path));
            //exec(String.format("echo \"%s\" | hdfs dfs -appendToFile - %s", eventLogBuilder.toString(), path));

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void exec(String command) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((reader.readLine()) != null) {}
        p.waitFor();
    }

}