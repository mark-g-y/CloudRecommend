
package com.cloudrecommend;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import com.cloudrecommend.util.ParseUtils;

public class EventReceiver {

    private static final int DEFAULT_NUM_STREAMS = 1;
    private static final String GROUP_ID = "event_processor";
    private static final String TOPIC = "events";

    private ConsumerConnector consumer;
    private List<KafkaStream<byte[], byte[]>> streams;
    private ExecutorService executor;

    private String hdfsUri;

    public EventReceiver(String zookeeperUri, String hdfsUri, int numStreams) {
        this.hdfsUri = hdfsUri;
        consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeperUri, GROUP_ID));

        executor = Executors.newFixedThreadPool(numStreams);

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, numStreams);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        streams = consumerMap.get(TOPIC);
    }

    public EventReceiver(String zookeeperUri, String hdfsUri) {
        this(zookeeperUri, hdfsUri, DEFAULT_NUM_STREAMS);
    }

    public void start() {
        for (int i = 0; i < streams.size(); i++) {
            executor.submit(new StreamProcessor(i, streams.get(i), hdfsUri));
        }
    }

    private ConsumerConfig createConsumerConfig(String zookeeperUri, String groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeperUri);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
 
        return new ConsumerConfig(props);
    }

    public static void main(String[] arg) {
        int numProcesses = DEFAULT_NUM_STREAMS;
        if (arg.length == 3 && !ParseUtils.isInt(arg[2]) || arg.length != 3 && arg.length != 2) {
            System.out.println("ERROR - arguments are <zookeeper_uri> <hdfs_uri> <numProcesses (optional)>");
            System.out.println("URI arguments are in format <host>:<port>");
            System.exit(1);
        } else if (arg.length == 3) {
            numProcesses = Integer.parseInt(arg[2]);
        }
        String zookeeperUri = arg[0];
        String hdfsUri = arg[1];
        EventReceiver eventReceiver = new EventReceiver(zookeeperUri, hdfsUri, numProcesses);
        eventReceiver.start();   
    }
}