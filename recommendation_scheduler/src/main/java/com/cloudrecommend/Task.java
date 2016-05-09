package com.cloudrecommend;

public class Task implements Comparable {

    private String group;
    private long delayBetweenExec;
    private long execTime;

    public Task(String group, long delayBetweenExec) {
        this.group = group;
        this.delayBetweenExec = delayBetweenExec;
        this.execTime = System.currentTimeMillis() + delayBetweenExec;
    }

    public String getGroup() {
        return group;
    }

    public long getExecTime() {
        return execTime;
    }

    public void updateExecTime() {
        execTime += delayBetweenExec;
    }

    @Override
    public int compareTo(Object o) {
        Task oTask = (Task)o;
        if (execTime < oTask.getExecTime()) {
            return -1;
        } else if (execTime > oTask.getExecTime()) {
            return 1;
        }
        return 0;
    }
}
