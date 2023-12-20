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

import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class ObsDao {
    @Value("${obs.ak}")
    String obsAk;

    @Value("${obs.sk}")
    String obsSk;

    @Value("${obs.endpoint}")
    String obsEndpoint;

    @Value("${obs.bucket.name}")
    String obsBucketName;

    @Value("${ip.database.path}")
    String localPath;

    @Value("${ip.location.object.key}")
    String IPObjectKey;

    @Value("${report.path}")
    String reportPath;

    @Value("${report.object.key}")
    String reportObjectKey;

    public static ObsClient obsClient;
    private static final Logger logger = LoggerFactory.getLogger(ObsDao.class);

    @PostConstruct
    public void init() {
        obsClient = new ObsClient(obsAk, obsSk, obsEndpoint);
    }

    public void putData() {
        PutObjectRequest request = new PutObjectRequest();
        request.setBucketName(obsBucketName);
        request.setObjectKey(IPObjectKey);
        request.setFile(new File(localPath));
        obsClient.putObject(request);
    }

    public InputStream getData() {
        ObsObject object = obsClient.getObject(obsBucketName, IPObjectKey);
        InputStream res = object.getObjectContent();
        return res;
    }

    public List<HashMap<String, Object>> getReportData() {
        ObsObject object = obsClient.getObject(obsBucketName, reportObjectKey);
        InputStream content = object.getObjectContent();
        ArrayList<HashMap<String, Object>> report = new ArrayList<>();
        try {
            String lineData;
            int lineNum = 0;
            String[] header = null;

            BufferedReader textFile = new BufferedReader(new InputStreamReader(content));
            while ((lineData = textFile.readLine()) != null) {
                if (lineNum == 0) {
                    header = lineData.split(",");
                } else {
                    HashMap<String, Object> dataMap = new HashMap<>();
                    String[] lineDatas = lineData.split(",");
                    for (int i = 0; i < header.length; i++) {
                        dataMap.put(header[i], lineDatas[i]);
                    }
                    report.add(dataMap);
                }
                lineNum++;
            }
        } catch (Exception e) {
            logger.error("exception", e);
        }
        return report;
    }

}
