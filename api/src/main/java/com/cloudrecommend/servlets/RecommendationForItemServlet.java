package com.cloudrecommend.servlets;


import com.cloudrecommend.hbase.HBaseUtil;
import com.cloudrecommend.json.JSON;
import org.apache.hadoop.hbase.client.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RecommendationForItemServlet extends RecommendationServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String siteGroup = request.getParameter("site");
        String item = request.getParameter("item");

        Result hbaseResult = HBaseUtil.get(siteGroup + "__itoi", item);
        if (!errorCheckResult(hbaseResult, response)) {
            return;
        }

        JSONArray result = JSON.createArray(new String(hbaseResult.getValue("itoi".getBytes(), "items".getBytes())));
        JSONObject resultMessage = JSON.create();
        JSON.put(resultMessage, "status", "success");
        JSON.put(resultMessage, "result", result);

        response.getWriter().print(resultMessage.toString());
    }

}