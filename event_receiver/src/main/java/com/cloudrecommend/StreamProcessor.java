
package com.cloudrecommend;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

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
            System.out.println(processId + ": " + new String(it.next().message()));
        }
    }

}