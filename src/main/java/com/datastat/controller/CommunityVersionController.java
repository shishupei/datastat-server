/* This project is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 PURPOSE.
 See the Mulan PSL v2 for more details.
 Create: 2022
*/
package com.datastat.controller;

import com.datastat.config.context.QueryConfContext;
import com.datastat.model.CustomPropertiesConfig;
import com.datastat.service.VersionService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author xiazhonghai
 * @date 2021/3/8 17:18
 * @description:获取各个仓库的版本号
 */
@RestController
public class CommunityVersionController {
    @Autowired
    VersionService versionService;

    @Autowired
    QueryConfContext queryConfContext;

    /***
     * 功能描述:get version informatin of community's branch
     * @param community:
     * @param repo:
     * @param branch:
     * @param pageSize:
     * @param currentPage:
     * @return: com.om.Result.Result
     * @Author: xiazhonghai
     * @Date: 2021/3/22 10:00
     */
    @GetMapping("/v1/versions")
    public String getVersionByRepo(HttpServletRequest request, @RequestParam() String community,
            @RequestParam(required = false) String repo,
            @RequestParam(required = false) String branch,
            @RequestParam(required = false, defaultValue = "0") int pageSize,
            @RequestParam(required = false, defaultValue = "0") int currentPage) throws InterruptedException,
            ExecutionException, NoSuchAlgorithmException, KeyManagementException, JsonProcessingException {
        String serviceType = community == null ? "queryConf" : community.toLowerCase() + "Conf";
        CustomPropertiesConfig queryConf = queryConfContext.getQueryConfig(serviceType);
        return versionService.getVersionByRepoBranch(queryConf, community, repo, pageSize, currentPage);
    }
}
