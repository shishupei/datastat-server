package com.datastat.dao.metric;

import java.util.*;
import org.springframework.stereotype.Repository;

import com.datastat.model.CustomPropertiesConfig;
import com.datastat.model.DatastatRequestBody;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

import org.asynchttpclient.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Repository
public class UserMetricDao extends MetricDao {

    public String queryMetricIncrease(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        HashMap<String, Object> variables = body.getVariables();
        String interval = (String) variables.get("interval");
        if (variables.containsKey("term")) {
            return queryMetricIncreaseContribute(queryConf, start, end, body, interval);
        }
        ArrayList<String> metrics = body.getMetrics();
        HashMap<String, Object> result = new HashMap<>();
        for (String metric : metrics) {
            ArrayList<HashMap<String, Object>> list = getMetricUserIncrease(queryConf, start, end, interval, variables, metric);
            result.put(metric + "_" + interval, list);
        }
        return resultJsonStr(200, objectMapper.valueToTree(result), "ok");
        
    }

    @SneakyThrows
    public String queryMetricIncreaseContribute(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body, String interval) {     
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Object> variables = body.getVariables();
        ArrayList<String> internals = castList(variables.get("internal"), String.class);
        ArrayList<String> orgs = castList(variables.get("org"), String.class);

        String term = (String) variables.get("term"); // term: D1, D2
        String termQuery;
        if (term.equalsIgnoreCase("D1") || term.equalsIgnoreCase("D2")) {
            termQuery = userQueryMap.get(term).asText();
        } else {
            return resultJsonStr(400, null, "query error");
        }
        ArrayList<String> metrics = body.getMetrics();
        String queryJson = queryConf.getUserContributeDetailQuery();
        for (String metric: metrics) { // pr, issue, comment
            String metricQuery = userQueryMap.get(metric).asText();            
            if (term.equals("D2") && !metric.equalsIgnoreCase("pr")) continue;
            String queryStr = String.format(queryJson, start, end, queryConf.convertList2QueryStr(internals),
                    queryConf.convertList2QueryStr(orgs), termQuery, interval, metricQuery);
            ArrayList<HashMap<String, Object>> res = getResponseResult(queryConf.getGiteeAllIndex(), queryStr);
            if (body.getOperation().equalsIgnoreCase("totalcount")) {
                result.put(term + "_" + metric, res.get(0).get("total"));
            } else {
                result.put(term + "_" + metric, res);
            }
        }
        return resultJsonStr(200, objectMapper.valueToTree(result), "ok");
    }

    public ArrayList<HashMap<String, Object>> getMetricUserIncrease(CustomPropertiesConfig queryConf, long start,
            long end, String interval, HashMap<String, Object> variables, String metric) {
        String index = queryConf.getUserCountIndex();
        String queryjson = queryConf.getUserCountQuery();
        ArrayList<String> orgs = castList(variables.get("org"), String.class);
        ArrayList<String> internals = castList(variables.get("internal"), String.class);
        String queryStr = String.format(queryjson, start, end, queryConf.convertList2QueryStr(internals),
                queryConf.convertList2QueryStr(orgs), interval, metric);
        return getResponseResult(index, queryStr);
    }

    @SneakyThrows
    public String queryMetricActive(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        String queryjson = queryConf.getUserActiveQuery();       
        HashMap<String, Object> variables = body.getVariables();
        ArrayList<String> orgs = castList(variables.get("org"), String.class);
        ArrayList<String> internals = castList(variables.get("internal"), String.class);
        String interval = (String) variables.get("interval");

        HashMap<String, Object> resMap = new HashMap<>();
        ArrayList<String> metrics = body.getMetrics();
        for (String metric : metrics) {
            String userQuery = userQueryMap.get(metric).asText();
            String queryStr = String.format(queryjson, start, end, queryConf.convertList2QueryStr(internals),
            queryConf.convertList2QueryStr(orgs), userQuery, interval);
            ListenableFuture<Response> future = esAsyncHttpUtil.executeSearch(esUrl, queryConf.getGiteeAllIndex(), queryStr);
            String responseBody = future.get().getResponseBody(UTF_8);
            JsonNode dataNode = objectMapper.readTree(responseBody);
            Iterator<JsonNode> buckets = dataNode.get("aggregations").get("group_field").get("buckets").elements();
            ArrayList<HashMap<String, Object>> resList = getResponseBuckets(buckets, "active");
            resMap.put(metric + "_" + interval, resList);
        }
        return resultJsonStr(200, objectMapper.valueToTree(resMap), "ok");       
    }

    public String queryMetricTotalCount(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        HashMap<String, Object> variables = body.getVariables();
        if (variables.containsKey("term")) {
            // D0, D1, D2的pr, issue, comment总数
            return queryMetricIncreaseContribute(queryConf, start, end, body, "10000d");
        }
        // D0, D1, D2总数
        return queryMetricUserTotalCount(queryConf, start, end, body);
    }

    public String queryMetricUserTotalCount(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        HashMap<String, Object> resMap = new HashMap<>();
        ArrayList<String> metrics = body.getMetrics();
        for (String metric : metrics) {
            String index = queryConf.getGiteeAllIndex();
            String queryJson = queryConf.getUserActiveQuery();
            String userQuery = userQueryMap.get(metric).asText();
            HashMap<String, Object> variables = body.getVariables();
            ArrayList<String> internals = castList(variables.get("internal"), String.class);
            ArrayList<String> orgs = castList(variables.get("org"), String.class);
            String queryStr = String.format(queryJson, start, end, queryConf.convertList2QueryStr(internals),
                    queryConf.convertList2QueryStr(orgs), userQuery, "10000d");
            int ans = parseTotalCount(queryConf, queryStr, index);
            resMap.put(metric, ans);
        }
        return resultJsonStr(200, objectMapper.valueToTree(resMap), "ok");

    }

    @SneakyThrows
    public String queryMetricRatio(CustomPropertiesConfig queryConf, long from, long end, DatastatRequestBody body) {
        String index= queryConf.getUserCountIndex();
        String queryJson = queryConf.getUserCountQuery();
        HashMap<String, Object> variables = body.getVariables();       
        ArrayList<String> internals = castList(variables.get("internal"), String.class);
        ArrayList<String> orgs = castList(variables.get("org"), String.class);

        int period = 0;
        String term = (String) variables.get("term");
        if (term.equalsIgnoreCase("week")) period = Calendar.WEEK_OF_MONTH;
        if (term.equalsIgnoreCase("month")) period = Calendar.MONTH;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(end);
        c.add(period, -1);
        long start = c.getTimeInMillis();

        ArrayList<String> metrics = body.getMetrics();
        HashMap<String, Object> resMap = new HashMap<>();
        for (String metric : metrics) {
            String queryStr = String.format(queryJson, start, end, queryConf.convertList2QueryStr(internals),
                    queryConf.convertList2QueryStr(orgs), "10000d", metric);
            double curCount = parseTotalCount(queryConf, queryStr, index);

            c.add(period, -1);
            long preStart = c.getTimeInMillis();
            queryStr = String.format(queryJson, preStart, start, queryConf.convertList2QueryStr(internals),
                    queryConf.convertList2QueryStr(orgs), "10000d", metric);
            double preCount = parseTotalCount(queryConf, queryStr, index);
            double ratio = preCount == 0 ? curCount : (curCount - preCount) / preCount;
            resMap.put(metric, ratio);
        }
        return resultJsonStr(200, objectMapper.valueToTree(resMap), "ok");
    }

}
