
package com.cloudrecommend;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class EventReceiver {

    private static ConsumerConnector consumer;

    private static ConsumerConfig createConsumerConfig(String zookeeperAddress, String groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeperAddress);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
 
        return new ConsumerConfig(props);
    }

    private static void consume(KafkaStream stream) {
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            System.out.println(new String(it.next().message()));
        }
        System.out.println("Shutting down");
    }

    public static void main(String[] arg) {
        String groupId = arg[0];
        String topic = arg[1];
        System.out.println("args: " + groupId + ", " + topic);
        consumer = Consumer.createJavaConsumerConnector(createConsumerConfig("localhost:2181", groupId));

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        for (KafkaStream stream : streams) {
            consume(stream);
        }
    }
}