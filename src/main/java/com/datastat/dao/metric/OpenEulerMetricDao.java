package com.datastat.dao.metric;

import java.util.*;

import org.springframework.stereotype.Repository;

import com.datastat.model.CustomPropertiesConfig;
import com.datastat.model.DatastatRequestBody;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

import org.asynchttpclient.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Repository(value = "openeulerMetricDao")
public class OpenEulerMetricDao {

    @Repository(value = "openeulersigMetricDao")
    public class OpenEulerSigMetricDao extends SigMetricDao {
    }

    @Repository(value = "openeuleruserMetricDao")
    public class OpenEulerUserMetricDao extends UserMetricDao {
    }

    @Repository(value = "openeulerrepoMetricDao")
    public class OpenEulerRepoMetricDao extends RepoMetricDao {
    }

    @Repository(value = "openeulercompanyMetricDao")
    public class OpenEulerCompanyMetricDao extends CompanyMetricDao {
    }

    @Repository(value = "openeulerdownloadMetricDao")
    public class OpenEulerDownloadMetricDao extends DownloadMetricDao {
        @SneakyThrows
        public ArrayList<HashMap<String, Object>> getMetricDownloadCountIncrease(CustomPropertiesConfig queryConf,
                long start, long end, HashMap<String, Object> variables) {
            String[] queryJsons = queryConf.getDownloadCountQueryStr().split(";");
            ArrayList<String> oversea = castList(variables.get("oversea"), String.class);
            String interval = (String) variables.get("interval");
            HashMap<Long, Double> increaseMap = new HashMap<>();
            for (int i = 0; i < queryJsons.length; i++) {
                String queryStr = String.format(queryJsons[i], start, end, queryConf.convertList2QueryStr(oversea), interval);
                ListenableFuture<Response> future = esAsyncHttpUtil.executeSearch(esUrl, queryConf.getUsersIndex(), queryStr);
                String responseBody = future.get().getResponseBody(UTF_8);
                JsonNode dataNode = objectMapper.readTree(responseBody);
                Iterator<JsonNode> buckets = dataNode.get("aggregations").get("group_field").get("buckets").elements();
                while (buckets.hasNext()) {
                    JsonNode bucket = buckets.next();
                    Long period = bucket.get("key").asLong();
                    Double cur = bucket.get("res").get("value").asDouble();
                    if (increaseMap.containsKey(period)) {
                        increaseMap.put(period, increaseMap.get(period) + cur);
                    } else {
                        increaseMap.put(period, cur);
                    }
                }
            }
            return parseResMap(increaseMap);
        }

        @SneakyThrows
        public int queryMetricDownloadCount(CustomPropertiesConfig queryConf, long start, long end, DatastatRequestBody body) {
            int ans = 0;
            String[] queryJsons = queryConf.getDownloadCountQueryStr().split(";");
            HashMap<String, Object> variables = body.getVariables();
            ArrayList<String> oversea = castList(variables.get("oversea"), String.class);

            for (int i = 0; i < queryJsons.length; i++) {
                String queryStr = String.format(queryJsons[i], start, end, queryConf.convertList2QueryStr(oversea), "10000d");
                ListenableFuture<Response> future = esAsyncHttpUtil.executeSearch(esUrl, queryConf.getUsersIndex(), queryStr);
                String responseBody = future.get().getResponseBody(UTF_8);
                JsonNode dataNode = objectMapper.readTree(responseBody);
                Iterator<JsonNode> buckets = dataNode.get("aggregations").get("group_field").get("buckets").elements();
                while (buckets.hasNext()) {
                    JsonNode bucket = buckets.next();
                    ans += bucket.get("res").get("value").asInt();
                }
            } 
            return ans;
        }
    }
    
}
