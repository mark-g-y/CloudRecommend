package com.cloudrecommend.servlets;


import com.cloudrecommend.json.JSON;
import org.apache.hadoop.hbase.client.Result;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RecommendationServlet extends HttpServlet {

    protected boolean errorCheckResult(Result result, HttpServletResponse response) throws IOException {
        if (result == null) {
            JSONObject resultMessage = JSON.create();
            JSON.put(resultMessage, "status", "error");
            JSON.put(resultMessage, "result", "key_not_found");
            response.getWriter().print(resultMessage.toString());
            return false;
        }
        return true;
    }

}
