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
import com.datastat.util.YamlUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository("mindsporeDao")
public class MindSporeQueryDao extends QueryDao {
    @SneakyThrows
    @Override
    public String querySigName(CustomPropertiesConfig queryConf, String community, String lang) {
        lang = lang == null ? "zh" : lang;
        HashMap<String, Object> res = getSigFromYaml(queryConf, lang);
        HashMap<String, Object> resData = new HashMap<>();
        resData.put("name", res.get("name"));
        resData.put("description", res.get("description"));
        resData.put("SIG_list", res.get("SIG list"));
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("data", resData);
        resMap.put("msg", "success");
        return objectMapper.valueToTree(resMap).toString();
    }

    private HashMap<String, Object> getSigFromYaml(CustomPropertiesConfig queryConf, String lang) {
        HashMap<String, Object> res = new HashMap<>();
        String mindSporeSigYaml;
        switch (lang){
            case "zh":
                mindSporeSigYaml = queryConf.getSigYamlZh();
                break;
            case "en":
                mindSporeSigYaml = queryConf.getSigYamlEn();
                break;
            default :
                return res;
        }

        String localYamlPath = env.getProperty("company.name.local.yaml");
        YamlUtil yamlUtil = new YamlUtil();
        String localFile = yamlUtil.wget(mindSporeSigYaml, localYamlPath);
        res = yamlUtil.readYaml(localFile);
        return res;
    }
}
