/* This project is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 PURPOSE.
 See the Mulan PSL v2 for more details.
 Create: 2023
*/

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
public class DownloadMetricDao extends MetricDao {

    public String queryMetricIncrease(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        HashMap<String, Object> variables = body.getVariables();
        String interval = (String) variables.get("interval");       
        ArrayList<String> metrics = body.getMetrics();
        HashMap<String, Object> resMap = new HashMap<>();
        for (String metric : metrics) {
            if (metric.equals("download_count")) {
                ArrayList<HashMap<String, Object>> metricList = getMetricDownloadCountIncrease(queryConf, start, end, variables);
                resMap.put(metric + "_" + interval, metricList);
            } else if (metric.equals("download_ip")) {
                ArrayList<HashMap<String, Object>> metricList = getMetricDownloadIpIncrease(queryConf, start, end, interval);
                resMap.put(metric + "_" + interval, metricList);
            }
        }
        return resultJsonStr(200, objectMapper.valueToTree(resMap), "ok");
    }

    @SneakyThrows
    public ArrayList<HashMap<String, Object>> getMetricDownloadCountIncrease(CustomPropertiesConfig queryConf,
            long start, long end, HashMap<String, Object> variables) {
        ArrayList<HashMap<String, Object>> resList = new ArrayList<>();
        return resList;
    }

    public ArrayList<HashMap<String, Object>> getMetricDownloadIpIncrease(CustomPropertiesConfig queryConf, long start,
            long end, String interval) {
        String index = queryConf.getDownloadIpIndex();
        String queryjson = queryConf.getDownloadIpIncreaseQuery();
        String queryStr = String.format(queryjson, start, end, interval);
        return getResponseResult(index, queryStr);
    }

    public String queryMetricTotalCount(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        HashMap<String, Object> resMap = new HashMap<>();
        ArrayList<String> metrics = body.getMetrics();
        for (String metric : metrics) {
            if (metric.equals("download_ip")) {
                int ans = queryMetricDownloadIpCount(queryConf, start, end, body);
                resMap.put(metric, ans);
            } else if (metric.equals("download_count")) {
                int ans = queryMetricDownloadCount(queryConf, start, end, body);
                resMap.put(metric, ans);
            }
        }
        return resultJsonStr(200, objectMapper.valueToTree(resMap), "ok");
    }

    @SneakyThrows
    public int queryMetricDownloadIpCount(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        String queryJson = queryConf.getDownloadIpCountQuery();
        int ans = 0;

        String queryStr = String.format(queryJson, start, end);
        ListenableFuture<Response> future = esAsyncHttpUtil.executeSearch(esUrl, queryConf.getDownloadIpIndex(), queryStr);
        String responseBody = future.get().getResponseBody(UTF_8);
        JsonNode dataNode = objectMapper.readTree(responseBody);
        Iterator<JsonNode> buckets = dataNode.get("aggregations").get("group_field").get("buckets").elements();
        while (buckets.hasNext()) {
            JsonNode bucket = buckets.next();
            ans = bucket.get("res").get("value").asInt();
        }       
        return ans;
    }

    @SneakyThrows
    public int queryMetricDownloadCount(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        int ans = 0;
        return ans;
    }

    @SneakyThrows
    public String queryMetricRatio(CustomPropertiesConfig queryConf, long from, long end, DatastatRequestBody body) {
        HashMap<String, Object> variables = body.getVariables();
        int period = 0;
        String term = (String) variables.get("term");
        if (term.equalsIgnoreCase("week")) period = Calendar.WEEK_OF_MONTH;
        if (term.equalsIgnoreCase("month")) period = Calendar.MONTH;
        
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(end);
        c.add(period, -1);
        long start = c.getTimeInMillis();

        c.add(period, -1);
        long preStart = c.getTimeInMillis();

        ArrayList<String> metrics = body.getMetrics();
        HashMap<String, Object> resMap = new HashMap<>();
        for (String metric : metrics) {
            if (metric.equalsIgnoreCase("download_count")) {
                double curCount = queryMetricDownloadCount(queryConf, start, end, body);
                double preCount = queryMetricDownloadCount(queryConf, preStart, start, body);
                double ratio = preCount == 0 ? curCount : (curCount - preCount) / preCount;
                resMap.put(metric, ratio);
            }
            if (metric.equalsIgnoreCase("download_ip")) {
                double curCount = queryMetricDownloadIpCount(queryConf, start, end, body);
                double preCount = queryMetricDownloadIpCount(queryConf, preStart, start, body);
                double ratio = preCount == 0 ? curCount : (curCount - preCount) / preCount;
                resMap.put(metric, ratio);
            }
            
        }
        return resultJsonStr(200, objectMapper.valueToTree(resMap), "ok");
    }
}
