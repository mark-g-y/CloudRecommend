package com.cloudrecommend.taskreceiver;

import com.cloudrecommend.Task;
import com.cloudrecommend.TaskQueue;
import com.cloudrecommend.jsonutil.JSON;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class TaskServlet extends HttpServlet {

    private TaskQueue taskQueue;

    public TaskServlet() {
        taskQueue = TaskQueue.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //<TODO> debug only - should only allow submitting tasks with POST requests
        JSONObject taskObj = JSON.create(getBody(request));

        String group = JSON.getString(taskObj, "group");
        long delayBetweenExec = JSON.getLong(taskObj, "delayBetweenExec");
        taskQueue.add(new Task(group, delayBetweenExec));

        JSONObject resp = new JSONObject();
        JSON.put(resp, "status", "success");
        response.getWriter().print(resp.toString());
    }

    private String getBody(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
