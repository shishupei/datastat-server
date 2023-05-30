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

package com.datastat.dao;

import com.datastat.model.CustomPropertiesConfig;
import com.datastat.result.ReturnCode;
import com.datastat.util.EsAsyncHttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.logging.log4j.Logger;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Repository;

@Repository("openeulerDao")
public class OpenEulerQueryDao extends QueryDao {
    private static Logger logger;
    @SneakyThrows
    @Override
    public String queryUsers(CustomPropertiesConfig queryConf, String item) {
        String index = queryConf.getUsersIndex();
        String[] queryJsons = queryConf.getUsersQueryStr().split(";");

        double userCount = 0d;
        int statusCode = 500;
        String statusText = ReturnCode.RC400.getMessage();
        for (String queryJson : queryJsons) {
            //获取执行结果
            ListenableFuture<Response> future = esAsyncHttpUtil.executeSearch(esUrl, index, queryJson);
            String users = getSumBucketValue(future, item);
            JsonNode dataNode = objectMapper.readTree(users);
            statusCode = dataNode.get("code").intValue();
            userCount += dataNode.get("data").get(item).intValue();
            statusText = dataNode.get("msg").textValue();
        }
        return resultJsonStr(statusCode, item, Math.round(userCount), statusText);
    }

    @Override
    public String queryDownload(CustomPropertiesConfig queryConf, String item) {
        return resultJsonStr(200, item, 0, "ok");
    }

    @Override
    public String getRepoReadme(CustomPropertiesConfig queryConf, String name) {
        String res = "";
        try {
            String path = env.getProperty("TC.oEEP.url");
            String urlStr = path + URLEncoder.encode(name, "utf-8") + ".md";
            urlStr = urlStr.replaceAll("\\+", "%20");
            System.out.println(urlStr);
            URL url = new URL(urlStr);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if (urlConnection instanceof HttpURLConnection) {
                connection = (HttpURLConnection) urlConnection;
                connection.setConnectTimeout(0);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String current;
            while ((current = in.readLine()) != null) {
                res += current + '\n';
            }
            return resultJsonStr(200, objectMapper.valueToTree(res), "ok");
        } catch (Exception e) {
            logger.error("exception", e);
        }
        return resultJsonStr(400, null, "ok");
    }
}
