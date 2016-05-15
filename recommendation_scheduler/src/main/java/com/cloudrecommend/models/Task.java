package com.cloudrecommend.models;

public class Task implements Comparable {

    private String group;
    private long delayBetweenExec;
    private long execTime;

    public Task(String group, long delayBetweenExec) {
        this(group, delayBetweenExec, System.currentTimeMillis() - delayBetweenExec);
    }

    public Task(String group, long delayBetweenExec, long lastRun) {
        this.group = group;
        this.delayBetweenExec = delayBetweenExec;
        this.execTime = lastRun + delayBetweenExec;
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
