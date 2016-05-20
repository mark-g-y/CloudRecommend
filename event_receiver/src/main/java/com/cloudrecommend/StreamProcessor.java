
package com.cloudrecommend;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.json.JSONObject;

public class StreamProcessor implements Runnable {

    private String id;
    private int processId;
    private KafkaStream stream;
    private String hdfsUri;
    private final String HDFS_PARENT_DIR = "cloudrecommend";

    public StreamProcessor(int processId, KafkaStream stream, String hdfsUri) {
        this.processId = processId;
        this.stream = stream;
        this.hdfsUri = "hdfs://" + hdfsUri + "/";

        // unique ID for this process and machine
        id = Long.toString(System.currentTimeMillis()) + Long.toString((int)Math.round((Math.random() * 100))) + processId;
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
            String eventSite = event.getString("site");
            String path = HDFS_PARENT_DIR + "/" + eventSite + "/" + eventSite + "_" + date + "_" + id;

            Configuration conf = new Configuration();
            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
            conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "false");

            FileSystem hdfs = FileSystem.get(new URI(hdfsUri), conf);
            hdfs.mkdirs(new Path(hdfsUri + HDFS_PARENT_DIR));
            hdfs.mkdirs(new Path(hdfsUri + HDFS_PARENT_DIR + "/" + eventSite));
            Path file = new Path(hdfsUri + path);
            if (!hdfs.exists(file)) {
                hdfs.create(file);
                hdfs.close();
                hdfs = FileSystem.get(new URI(hdfsUri), conf);
            }
            OutputStream os = hdfs.append(file);
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
            br.write(eventLogBuilder.toString());
            br.close();
            hdfs.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}