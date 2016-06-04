package com.cloudrecommend;

import java.util.concurrent.LinkedBlockingQueue;

public class AvailableWorkersQueue extends LinkedBlockingQueue<String> {

    private static AvailableWorkersQueue queue;

    private AvailableWorkersQueue() {
    }

    public static AvailableWorkersQueue getInstance() {
        if (queue == null) {
            queue = new AvailableWorkersQueue();
        }
        return queue;
    }
}
