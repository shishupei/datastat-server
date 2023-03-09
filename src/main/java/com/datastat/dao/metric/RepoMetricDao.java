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
public class RepoMetricDao extends MetricDao {

    public String queryMetricIncrease(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        HashMap<String, Object> variables = body.getVariables();
        if (variables.containsKey("term")) {
            String term = (String) variables.get("term");
            if (term.equalsIgnoreCase("repo"))
                return queryMetricIncreaseTermContribute(queryConf, start, end, body, term);
        }
        return resultJsonStr(400, null, "query error");
    }

    @SneakyThrows
    public String queryMetricIncreaseTermContribute(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body, String term) {
        String queryjson = queryConf.getAggContributeDetailQuery();
        HashMap<String, Object> variables = body.getVariables();
        ArrayList<String> orgs = castList(variables.get("org"), String.class);
        ArrayList<String> internals = castList(variables.get("internal"), String.class);
        String interval = (String) variables.get("interval");
        ArrayList<String> metrics = body.getMetrics();
        HashMap<String, Object> resMap = new HashMap<>();

        for (String metric : metrics) {
            if (!metric.equalsIgnoreCase("pr") && !metric.equalsIgnoreCase("issue") && !metric.equalsIgnoreCase("comment")) {
                continue;
            }
            String userQuery = userQueryMap.get(metric).asText();
            String termQuery = queryConf.getTermQuery(term, companyQueryMap).get(0);
            String queryStr = String.format(queryjson, start, end, queryConf.convertList2QueryStr(internals),
                    queryConf.convertList2QueryStr(orgs), termQuery, interval, userQuery);
            ListenableFuture<Response> future = esAsyncHttpUtil.executeSearch(esUrl, queryConf.getGiteeAllIndex(), queryStr);
            String responseBody = future.get().getResponseBody(UTF_8);
            JsonNode dataNode = objectMapper.readTree(responseBody);
            Iterator<JsonNode> buckets = dataNode.get("aggregations").get("group_field").get("buckets").elements();

            HashMap<String, Object> termMap = new HashMap<>();
            while (buckets.hasNext()) {
                JsonNode bucket = buckets.next();
                String repo = bucket.get("key").asText();
                Iterator<JsonNode> termDetails = bucket.get("2").get("buckets").elements();
                ArrayList<HashMap<String, Object>> tmpList = getResponseBuckets(termDetails, "increase");;                  
                termMap.put(repo, tmpList);
            }
            resMap.put(metric, termMap);
        }
        return resultJsonStr(200, objectMapper.valueToTree(resMap), "ok");
        
    }

    public String queryMetricTotalCount(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
        HashMap<String, Object> variables = body.getVariables();
        if (variables.containsKey("term")) {
            String term = (String) variables.get("term");
            if (term.equalsIgnoreCase("repo")) {
                return queryMetricTotalCountTermDetails(queryConf, start, end, body, term);
            }   
        }
        return resultJsonStr(400, null, "query error");
    }

    public String queryMetricTotalCountTermDetails(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body, String term) {
        ArrayList<String> metrics = body.getMetrics();
        HashMap<String, Object> resMap = new HashMap<>();
        for (String metric : metrics) {
            if (metric.equalsIgnoreCase("users")) {
                // 每个repo的D0, D1, D2人数
                ArrayList<HashMap<String, Object>> userDetails = queryMetricTotalCountUser(queryConf, start, end, body, term);
                resMap.put(metric, userDetails);
            }
            if (metric.equalsIgnoreCase("contributes")) {
                // 每个repo的pr, issue, comment总数
                ArrayList<HashMap<String, Object>> contributeDetails = queryMetricTotalCountTermContribute(queryConf,
                        start, end, body, term);
                resMap.put(metric, contributeDetails);
            }
        }
        return resultJsonStr(200, objectMapper.valueToTree(resMap), "ok");
    }

    public ArrayList<HashMap<String, Object>> queryMetricTotalCountUser(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body,
            String term) {
        ArrayList<HashMap<String, Object>> resList = new ArrayList<>();
        String termQuery = queryConf.getTermQuery(term, companyQueryMap).get(0);
        String enterpriseQuery = queryConf.getTermQuery(term, companyQueryMap).get(1);
        List<String> metrics = Arrays.asList("D0", "D1", "D2");
        HashMap<String, HashMap<String, Object>> resMap = new HashMap<>();
        for (String metric: metrics) {
            HashMap<String, Integer> metricData = queryMetricTotalCountTermUser(queryConf, start, end, body, metric, enterpriseQuery, termQuery);
            for (String key : metricData.keySet()) {
                HashMap<String, Object> tmp = new HashMap<>();
                if (resMap.containsKey(key)) {
                    tmp = resMap.get(key);    
                }
                tmp.put(metric, metricData.get(key));
                tmp.put("filter", key);
                resMap.put(key, tmp);
            }
        }
        for (String key : resMap.keySet()) {
            resList.add(resMap.get(key));
        }
        return resList;
    }

    @SneakyThrows
    public HashMap<String, Integer> queryMetricTotalCountTermUser(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body,
            String metric, String enterpriseQuery, String termQuery) {
        HashMap<String, Integer> resMap = new HashMap<>();
        String queryjson = queryConf.getAggTotalUserCountQuery();
        HashMap<String, Object> variables = body.getVariables();
        ArrayList<String> orgs = castList(variables.get("org"), String.class);
        ArrayList<String> internals = castList(variables.get("internal"), String.class);

        String userQuery = userQueryMap.get(metric).asText();
        String queryStr = String.format(queryjson, start, end, queryConf.convertList2QueryStr(internals),
                queryConf.convertList2QueryStr(orgs), userQuery, enterpriseQuery, termQuery);

        ListenableFuture<Response> future = esAsyncHttpUtil.executeSearch(esUrl, queryConf.getGiteeAllIndex(), queryStr);
        String responseBody = future.get().getResponseBody(UTF_8);
        JsonNode dataNode = objectMapper.readTree(responseBody);
        Iterator<JsonNode> buckets = dataNode.get("aggregations").get("group_field").get("buckets").elements();
        while (buckets.hasNext()) {
            JsonNode bucket = buckets.next();
            String filter = bucket.get("key").asText();
            resMap.put(filter, bucket.get("res").get("value").asInt());
        }
        return resMap;
    }

    @SneakyThrows
    public ArrayList<HashMap<String, Object>> queryMetricTotalCountTermContribute(CustomPropertiesConfig queryConf,
            long start, long end, DatastatRequestBody body, String term) {
        String queryJson = queryConf.getAggTotalContributeDetailQuery();
        ArrayList<HashMap<String, Object>> resList = new ArrayList<>();        
        HashMap<String, Object> variables = body.getVariables();
        ArrayList<String> orgs = castList(variables.get("org"), String.class);
        ArrayList<String> internals = castList(variables.get("internal"), String.class);
        String termQuery = queryConf.getTermQuery(term, companyQueryMap).get(0);
        String enterpriseQuery = queryConf.getTermQuery(term, companyQueryMap).get(1);
        String queryStr = String.format(queryJson, start, end, queryConf.convertList2QueryStr(internals),
                queryConf.convertList2QueryStr(orgs), enterpriseQuery, termQuery);
        ListenableFuture<Response> future = esAsyncHttpUtil.executeSearch(esUrl, queryConf.getGiteeAllIndex(), queryStr);
        String responseBody = future.get().getResponseBody(UTF_8);
        JsonNode dataNode = objectMapper.readTree(responseBody);
        Iterator<JsonNode> buckets = dataNode.get("aggregations").get("group_field").get("buckets").elements();
        while (buckets.hasNext()) {
            JsonNode bucket = buckets.next();
            String filter = bucket.get("key").asText();
            HashMap<String, Object> item = new HashMap<>();
            item.put("filter", filter);
            item.put("pr", bucket.get("pr").get("value").asInt());
            item.put("issue", bucket.get("issue").get("value").asInt());
            item.put("comment", bucket.get("comment").get("value").asInt());
            item.put("company", bucket.get("company").get("value").asInt());
            resList.add(item);
        }
        return resList;
    }

}
