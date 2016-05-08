
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

public class EventReceiver {

    private static final int DEFAULT_NUM_STREAMS = 1;

    private ConsumerConnector consumer;
    private List<KafkaStream<byte[], byte[]>> streams;
    private ExecutorService executor;

    public EventReceiver(String zookeeperAddress, String groupId, String topic, int numStreams) {
        consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeperAddress, groupId));

        executor = Executors.newFixedThreadPool(numStreams);

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, numStreams);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        streams = consumerMap.get(topic);
    }

    public EventReceiver(String zookeeperAddress, String groupId, String topic) {
        this(zookeeperAddress, groupId, topic, DEFAULT_NUM_STREAMS);
    }

    public void start() {
        for (int i = 0; i < streams.size(); i++) {
            executor.submit(new StreamProcessor(i, streams.get(i)));
        }
    }

    private ConsumerConfig createConsumerConfig(String zookeeperAddress, String groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeperAddress);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
 
        return new ConsumerConfig(props);
    }

    public static void main(String[] arg) {
        int numProcesses = DEFAULT_NUM_STREAMS;
        if (arg.length == 4) {
            numProcesses = Integer.parseInt(arg[3]);
        }
        EventReceiver eventReceiver = new EventReceiver(arg[0], arg[1], arg[2], numProcesses);
        eventReceiver.start();   
    }
}