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

package com.datastat.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.datastat.config.context.QueryConfContext;
import com.datastat.dao.QueryDao;
import com.datastat.dao.RedisDao;
import com.datastat.dao.context.QueryDaoContext;
import com.datastat.model.CustomPropertiesConfig;
import com.datastat.model.vo.*;
import com.datastat.result.ReturnCode;
import com.datastat.util.PageUtils;
import com.datastat.util.RSAUtil;
import com.datastat.util.StringValidationUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Primary
@Service(value = "queryService")
public class QueryService {
    @Autowired
    Environment env;

    @Autowired
    QueryConfContext queryConfContext;

    @Autowired
    QueryDaoContext queryDaoContext;

    @Autowired
    RedisDao redisDao;

    @Autowired
    ObjectMapper objectMapper;

    private static long redisDefaultExpire;

    @PostConstruct
    public void init() {
        redisDefaultExpire = Long.parseLong(env.getProperty("redis.keyExpire", "60"));
    }

    public String queryContributors(HttpServletRequest request, String community) {
        String item = "contributors";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryContributors(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryDurationAggFromProjectHostArchPackage(HttpServletRequest request, String community) {
        String item = "avgDuration";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryDurationAggFromProjectHostArchPackage(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String querySigs(HttpServletRequest request, String community) {
        String item = "sigs";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.querySigs(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryUsers(HttpServletRequest request, String community) {
        String item = "users";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryUsers(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryNoticeUsers(HttpServletRequest request, String community) {
        String item = "noticeusers";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryNoticeUsers(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryModuleNums(HttpServletRequest request, String community) {
        String item = "modulenums";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryModuleNums(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryBusinessOsv(HttpServletRequest request, String community) {
        String item = "businessOsv";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryBusinessOsv(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCommunityMembers(HttpServletRequest request, String community) {
        String item = "communitymembers";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryCommunityMembers(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryDownload(HttpServletRequest request, String community) {
        String item = "download";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryDownload(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCount(HttpServletRequest request, String community, String item) {
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryCount(queryConf, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryAll(HttpServletRequest request, String community) throws Exception {
        String item = "all";
        String key = community.toLowerCase() + item;
        String result = null;//(String) redisDao.get(key);

        JsonNode newData;
        JsonNode oldData = null;
        boolean isFlush = false;

        if (result != null) {
            JsonNode all = objectMapper.readTree(result);
            String updateAt = all.get("update_at").asText();
            oldData = all.get("data");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            try {
                Date updateDate = sdf.parse(updateAt);
                Date now = new Date();
                long diffs = (now.getTime() - updateDate.getTime());
                if (diffs > Long.parseLong(env.getProperty("redis.flush.interval", "7200000"))) {
                    isFlush = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (isFlush || result == null) {
            boolean flag = false;
            try {
                QueryDao queryDao = getQueryDao(request);
                CustomPropertiesConfig queryConf = getQueryConf(request);
                String resultNew = queryDao.queryAll(queryConf, community);

                JsonNode allNew = objectMapper.readTree(resultNew);
                newData = allNew.get("data");
                if (oldData != null) {
                    flag = false; //TODO errorAlertService.errorAlert(community, oldData, newData);
                }
                if (!flag) {
//                    redisDao.set(key, resultNew, -1L);
                    result = resultNew;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public String queryBlueZoneContributes(HttpServletRequest request, BlueZoneContributeVo body) {
        String item = "contributes";
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);

        String token = queryConf.getBlueZoneApiToken();
        if (StringUtils.isBlank(body.getToken()) || !body.getToken().equals(token)) {
            return queryDao.resultJsonStr(401, item, "token error", "token error");
        }
        return queryDao.queryBlueZoneContributes(queryConf, body, item);
    }

    public String putBlueZoneUser(HttpServletRequest request, BlueZoneUserVo userVo) {
        String item = "user";
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);

        String token = queryConf.getBlueZoneApiToken();
        if (StringUtils.isBlank(userVo.getToken()) || !userVo.getToken().equals(token)) {
            return queryDao.resultJsonStr(401, item, "token error", "token error");
        }
        return queryDao.putBlueZoneUser(queryConf, userVo, item);
    }

    public String queryOrgStarAndFork(HttpServletRequest request, String community) {
        String item = "starFork";
        String key = community + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryOrgStarAndFork(queryConf, community, item);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCveDetails(HttpServletRequest request, String community, String lastCursor, String pageSize) {
        String item = "cveDetails";
        String key = community + item;
        String result = null;
        if (pageSize == null) result = (String) redisDao.get(key);

        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryCveDetails(queryConf, lastCursor, pageSize, item);
//            if (pageSize == null) redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryNewYear(HttpServletRequest request, String community, String user, String year) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        return queryDao.queryNewYear(community, user, year);
    }

    public String queryNewYearMonthCount(HttpServletRequest request, String user) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        return queryDao.queryNewYearMonthCount(queryConf, user);
    }

    public String queryBugQuestionnaire(HttpServletRequest request, String community, String lastCursor, String pageSize) {
        String item = "bugQuestionnaire";
        String key = community + item;
        String result = null;
        if (pageSize == null) result = (String) redisDao.get(key);

        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryBugQuestionnaire(queryConf, lastCursor, pageSize, item);
//            if (pageSize == null) redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryObsDetails(HttpServletRequest request, String branch, String limit) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        return queryDao.queryObsDetails(queryConf, branch);
    }

    public String queryIsoBuildTimes(HttpServletRequest request, IsoBuildTimesVo body) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        return queryDao.queryIsoBuildTimes(queryConf, body);
    }

    public String querySigDetails(HttpServletRequest request, SigDetailsVo body) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        return queryDao.querySigDetails(queryConf, body);
    }

    public String queryCompanyContributors(HttpServletRequest request, String community, String contributeType, String timeRange, String repo) {
        String item = "companyContribute";
        String key = community.toLowerCase() + item + contributeType.toLowerCase() + timeRange.toLowerCase() + repo;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryCompanyContributors(queryConf, community, contributeType, timeRange, repo, null);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryUserContributors(HttpServletRequest request, String community, String contributeType, String timeRange, String repo) {
        String item = "userContribute";
        String key = community.toLowerCase() + item + contributeType.toLowerCase() + timeRange.toLowerCase() + repo;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryUserContributors(queryConf, community, contributeType, timeRange, repo, null);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryIssueScore(HttpServletRequest request, String startDate, String endDate) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        return queryDao.queryIssueScore(queryConf, startDate, endDate);
    }

    public String queryBuildCheckInfo(HttpServletRequest request, BuildCheckInfoQueryVo queryBody, String lastCursor, String pageSize) {
        String item = "buildCheckInfo";
        String validateResult = validateBuildCheckInfo(queryBody, item);
        if (!StringUtils.isBlank(validateResult)) {
            return validateResult;
        }
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(queryBody.getCommunityName());
        return queryDao.queryBuildCheckInfo(queryConf, queryBody, item, lastCursor, pageSize);
    }

    public String putUserActionsInfo(HttpServletRequest request, String community, String data) {
        QueryDao queryDao = getQueryDao(request);
        return queryDao.putUserActionsInfo(community, data);
    }

    public String querySigName(HttpServletRequest request, String community, String lang) {
        String item = "sigsname";
        String key = community + item + lang;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.querySigName(queryConf, community, lang);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String querySigInfo(HttpServletRequest request, String community, String sig, String repo, String user, String search, String page, String pageSize) throws Exception {
        if (search != null && search.equals("fuzzy")) {
            return queryFuzzySigInfo(request, community, sig, repo, user, search, page, pageSize);
        }
        return querySigInfo(request, community, sig);
    }

    public String querySigRepo(HttpServletRequest request, String community, String sig, String page, String pageSize) throws Exception {
        String item = "repo";
        String key = community.toLowerCase() + sig + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.querySigRepo(queryConf, sig);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        if (pageSize == null || page == null) return result;

        JsonNode all = objectMapper.readTree(result);
        if (all.get("data") != null) {
            JsonNode res = all.get("data");
            ArrayList<String> resList = objectMapper.convertValue(res, new TypeReference<>() {
            });

            int currentPage = Integer.parseInt(page);
            int pagesize = Integer.parseInt(pageSize);
            Map data = PageUtils.getDataByPage(currentPage, pagesize, resList);
            ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();
            dataList.add((HashMap<String, Object>) data);
            HashMap<String, Object> resMap = new HashMap<>();
            resMap.put("code", 200);
            resMap.put("data", dataList);
            resMap.put("msg", "success");
            result = objectMapper.valueToTree(dataList).toString();
        }
        return result;
    }

    public String querySigCompanyContributors(HttpServletRequest request, String community, String contributeType, String timeRange, String sig) {
        String item = "companyContribute";
        String key = community.toLowerCase() + item + contributeType.toLowerCase() + timeRange.toLowerCase() + sig;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryCompanyContributors(queryConf, community, contributeType, timeRange, null, sig);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCompanyName(HttpServletRequest request, String community) {
        String key = community.toLowerCase() + "companyname";
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryCompanyName(queryConf, community);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCompanyUserContribute(HttpServletRequest request, String community, String company, String contributeType,
                                             String timeRange, String token) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        // 是否有企业访问权限
        boolean per = checkPermission(request, queryDao, queryConf, token, company);
        if (!per) return resultJsonStr(400, "", "No Permission");

        String key = community.toLowerCase() + company + "usertypecontribute_" + contributeType.toLowerCase();
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            result = queryDao.queryGroupUserContributors(queryDao, queryConf, "company", company, contributeType, timeRange);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCompanySigContribute(HttpServletRequest request, String community, String company, String contributeType,
                                            String timeRange, String token) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        // 是否有企业访问权限
        boolean per = checkPermission(request, queryDao, queryConf, token, company);
        if (!per) return resultJsonStr(400, "", "No Permission");

        String key = community.toLowerCase() + company + "sigtypecontribute_" + contributeType.toLowerCase() + timeRange.toLowerCase();
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            result = queryDao.queryGroupSigContribute(queryDao, queryConf, "company", company, contributeType, timeRange);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCompanySigDetails(HttpServletRequest request, String community, String company, String timeRange, String token) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        // 是否有企业访问权限
        boolean per = checkPermission(request, queryDao, queryConf, token, company);
        if (!per) return resultJsonStr(400, "", "No Permission");

        String key = community.toLowerCase() + company + "sig" + timeRange.toLowerCase();
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            result = queryDao.queryCompanySigDetails(queryConf, company, timeRange);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String querySigUserTypeCount(HttpServletRequest request, String community, String sig, String contributeType, String timeRange) {
        String key = community.toLowerCase() + sig + "usertypecontribute_" + contributeType.toLowerCase() + timeRange.toLowerCase();
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryGroupUserContributors(queryDao, queryConf, "sig", sig, contributeType, timeRange);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCompanyUsers(HttpServletRequest request, String community, String company, String timeRange, String token) {
        QueryDao queryDao = getQueryDao(request);
        CustomPropertiesConfig queryConf = getQueryConf(request);
        // 是否有企业访问权限
        boolean per = checkPermission(request, queryDao, queryConf, token, company);
        if (!per) return resultJsonStr(400, "", "No Permission");

        String key = community.toLowerCase() + company + "companyusers" + timeRange.toLowerCase();
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            result = queryDao.queryCompanyUsers(queryConf, company, timeRange);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCommunityRepos(HttpServletRequest request, String community) {
        String key = community.toLowerCase() + "repos";
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryCommunityRepos(queryConf);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String querySigScore(HttpServletRequest request, String community, String sig, String timeRange) {
        String key = community.toLowerCase() + sig + "sigscore" + timeRange.toLowerCase();
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.querySigScore(queryConf, sig, timeRange, "");
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String querySigScoreAll(HttpServletRequest request, String community) {
        String keyStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = community.toLowerCase() + "sigscoreall" + keyStr;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.querySigScoreAll(queryConf);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String querySigRadarScore(HttpServletRequest request, String community, String sig, String timeRange) {
        String key = community.toLowerCase() + sig + "sigradarscore" + timeRange.toLowerCase();
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.querySigScore(queryConf, sig, timeRange, "radar");
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryCompanySigs(HttpServletRequest request, String community, String timeRange) {
        String key = community.toLowerCase() + "companysigs" + timeRange.toLowerCase();
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryCompanySigs(queryConf, timeRange);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String querySigsOfTCOwners(HttpServletRequest request, String community) {
        String key = community.toLowerCase() + "sigs_of_tc_owners";
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.querySigsOfTCOwners(queryConf);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryUserSigContribute(HttpServletRequest request, String community, String user, String contributeType, String timeRange) {
        String key = community.toLowerCase() + user + "sigtypecontribute_" + contributeType.toLowerCase() + timeRange.toLowerCase();
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryGroupSigContribute(queryDao, queryConf, "user", user, contributeType, timeRange);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryUserOwnerType(HttpServletRequest request, String community, String user) {
        String key = community.toLowerCase() + "all" + "ownertype";
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryAllUserOwnerType(queryConf, user);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    public String queryUserContributeDetails(HttpServletRequest request, String community, String user, String sig, String contributeType,
                                             String timeRange, String page, String pageSize, String comment_type, String filter) throws Exception {
        String key = community.toLowerCase() + sig + contributeType.toLowerCase() + timeRange.toLowerCase() + comment_type;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.queryUserContributeDetails(queryDao, queryConf, community, user, sig, contributeType, timeRange, comment_type, filter);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        if (page != null && pageSize != null) {
            JsonNode all = objectMapper.readTree(result);
            if (all.get("data").get(user) == null) return result;

            Iterator<JsonNode> buckets = all.get("data").get(user).iterator();
            ArrayList<JsonNode> userCount = new ArrayList<>();
            ArrayList<JsonNode> filterRes = new ArrayList<>();
            while (buckets.hasNext()) {
                JsonNode bucket = buckets.next();
                if (filter == null) userCount.add(bucket);
                if (filter != null && bucket.get("info").toString().contains(filter)) filterRes.add(bucket);
            }
            ArrayList<JsonNode> resList = filter == null ? userCount : filterRes;
            Map map = PageUtils.getDataByPage(Integer.parseInt(page), Integer.parseInt(pageSize), resList);
            HashMap<String, Object> resMap = new HashMap<>();
            resMap.put("code", 200);
            resMap.put("data", map);
            resMap.put("msg", "success");
            return objectMapper.valueToTree(resMap).toString();
        }

        return result;
    }

    public String putGiteeHookUser(HttpServletRequest request, String requestBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode all = objectMapper.readTree(requestBody);
            String hookName = all.get("hook_name").asText();
            JsonNode action = switch (hookName) {
                case "merge_request_hooks" -> all.get("pull_request");
                case "issue_hooks" -> all.get("issue");
                case "note_hooks" -> all.get("comment");
                default -> null;
            };
            if (action == null) return resultJsonStr(400, "user_count", 0, "parse body fail");
            ArrayList<Map<String, Object>> users = new ArrayList<>();

            JsonNode user = action.get("user");
            String createdAt = action.get("created_at").asText();
            Map<String, Object> userRes = giteeWebhookUser(user, createdAt);
            if (userRes != null) users.add(userRes);

            JsonNode assignee = action.get("assignee");
            Map<String, Object> assigneeRes = giteeWebhookUser(assignee, createdAt);
            if (assigneeRes != null) users.add(assigneeRes);

            TreeSet<Map<String, Object>> userSet = new TreeSet<>(Comparator.comparing(o -> o.get("email").toString()));
            userSet.addAll(users);

            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            queryDao.putGiteeHookUser(queryConf, userSet);
            return resultJsonStr(200, "user_count", userSet.size(), "success");
        } catch (Exception e) {
            e.printStackTrace();
            return resultJsonStr(400, "user_count", 0, "parse body fail");
        }
    }


    public QueryDao getQueryDao(HttpServletRequest request) {
        String community = request.getParameter("community");
        String serviceType = community == null ? "queryDao" : community.toLowerCase() + "Dao";
        return queryDaoContext.getQueryDao(serviceType);
    }

    public CustomPropertiesConfig getQueryConf(HttpServletRequest request) {
        String community = request.getParameter("community");
        return getQueryConf(community);
    }

    public CustomPropertiesConfig getQueryConf(String community) {
        String serviceType = community == null ? "queryConf" : community.toLowerCase() + "Conf";
        return queryConfContext.getQueryConfig(serviceType);
    }

    private String resultJsonStr(int code, String item, Object data, String msg) {
        return "{\"code\":" + code + ",\"data\":{\"" + item + "\":" + data + "},\"msg\":\"" + msg + "\"}";
    }

    private String resultJsonStr(int code, Object data, String msg) {
        return "{\"code\":" + code + ",\"data\":" + data + ",\"msg\":\"" + msg + "\"}";
    }

    private String validateBuildCheckInfo(BuildCheckInfoQueryVo buildCheckInfoQueryVo, String item) {
        List<String> checkTotalValidField = Arrays.asList("success", "failed");
        double MIN_BUILD_DURATION = 0;
        double min_duration_time;
        String errorMsg = "";

        String check_total = buildCheckInfoQueryVo.getCheckTotal();
        if (!StringUtils.isBlank(check_total) && !checkTotalValidField.contains(check_total.toLowerCase())) {
            errorMsg = "check_total is invalid, Only allows: SUCCESS and FAILED";
            return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
        }

        Map<String, String> build_duration = buildCheckInfoQueryVo.getBuildDuration();
        if (StringUtils.isNotBlank(build_duration.get("min_duration_time"))) {
            min_duration_time = Double.parseDouble(build_duration.get("min_duration_time"));
            if (min_duration_time < MIN_BUILD_DURATION) {
                errorMsg = "build_time is invalid, Only allows: bigger than " + MIN_BUILD_DURATION + "";
                return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
            }
        }

        Map<String, String> pr_create_time = buildCheckInfoQueryVo.getPrCreateTime();
        Map<String, String> result_build_time = buildCheckInfoQueryVo.getResultBuildTime();
        Map<String, String> result_update_time = buildCheckInfoQueryVo.getResultUpdateTime();
        Map<String, String> mistake_update_time = buildCheckInfoQueryVo.getMistakeUpdateTime();

        String pr_create_start_time = pr_create_time.get("start_time");
        String pr_create_end_time = pr_create_time.get("end_time");
        String result_build_start_time = result_build_time.get("start_time");
        String result_build_end_time = result_build_time.get("end_time");
        String result_update_start_time = result_update_time.get("start_time");
        String result_update_end_time = result_update_time.get("end_time");
        String mistake_update_start_time = mistake_update_time.get("start_time");
        String mistake_update_end_time = mistake_update_time.get("end_time");

        if (!StringValidationUtil.isDateTimeStrValid(pr_create_start_time)) {
            errorMsg = "pr_create_start_time format is invalid";
            return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
        }
        if (!StringValidationUtil.isDateTimeStrValid(pr_create_end_time)) {
            errorMsg = "pr_create_end_time format is invalid";
            return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
        }
        if (!StringValidationUtil.isDateTimeStrValid(result_build_start_time)) {
            errorMsg = "result_build_start_time format is invalid";
            return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
        }
        if (!StringValidationUtil.isDateTimeStrValid(result_build_end_time)) {
            errorMsg = "result_build_end_time format is invalid";
            return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
        }
        if (!StringValidationUtil.isDateTimeStrValid(result_update_start_time)) {
            errorMsg = "result_update_start_time format is invalid";
            return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
        }
        if (!StringValidationUtil.isDateTimeStrValid(result_update_end_time)) {
            errorMsg = "result_update_end_time format is invalid";
            return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
        }
        if (!StringValidationUtil.isDateTimeStrValid(mistake_update_start_time)) {
            errorMsg = "mistake_update_start_time format is invalid";
            return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
        }
        if (!StringValidationUtil.isDateTimeStrValid(mistake_update_end_time)) {
            errorMsg = "mistake_update_end_time format is invalid";
            return resultJsonStr(400, item, ReturnCode.RC400.getMessage(), errorMsg);
        }

        return null;
    }

    private String querySigInfo(HttpServletRequest request, String community, String sig) {
        String item = "siginfo";
        String key = community + sig + item;
        String result = null; //(String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.querySigInfo(queryConf, sig);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        return result;
    }

    private String queryFuzzySigInfo(HttpServletRequest request, String community, String sig, String repo, String user, String search, String page, String pageSize) throws Exception {
        String key = community + "allsiginfo";
        String result = null;
        result = (String) redisDao.get(key);
        if (result == null) {
            QueryDao queryDao = getQueryDao(request);
            CustomPropertiesConfig queryConf = getQueryConf(request);
            result = queryDao.querySigInfo(queryConf, sig);
//            redisDao.set(key, result, redisDefaultExpire);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode all = objectMapper.readTree(result);
        if (all.get("code").asInt() != 200) {
            return resultJsonStr(400, ReturnCode.RC400.getMessage(), ReturnCode.RC400.getMessage());
        }
        JsonNode res = all.get("data");
        ArrayList<HashMap<String, Object>> resList = objectMapper.convertValue(res, new TypeReference<>() {
        });
        ArrayList<HashMap<String, Object>> tempList = new ArrayList<>();
        for (HashMap<String, Object> list : resList) {
            String sig_name = list.get("sig_name").toString();
            ArrayList<String> repos = (ArrayList<String>) list.get("repos");
            Boolean bool = sig != null && !sig_name.toLowerCase().contains(sig.toLowerCase()) ? false : true;
            ArrayList<String> maintainers = (ArrayList<String>) list.get("maintainers");
            if (bool && matchList(repos, repo) && matchList(maintainers, user)) {
                tempList.add(list);
            }
        }

        Collections.sort(tempList, Comparator.comparing(t -> t.get("sig_name").toString().toLowerCase()));

        if (pageSize != null && page != null) {
            int currentPage = Integer.parseInt(page);
            int pagesize = Integer.parseInt(pageSize);
            Map data = PageUtils.getDataByPage(currentPage, pagesize, tempList);
            ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();
            dataList.add((HashMap<String, Object>) data);
            tempList = dataList;
        }
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("data", tempList);
        resMap.put("msg", "success");
        result = objectMapper.valueToTree(resMap).toString();
        return result;
    }

    private Boolean matchList(ArrayList<String> arrayList, String str) {
        if (str == null) {
            return true;
        }
        for (String list : arrayList) {
            if (list.toLowerCase().contains(str.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPermission(HttpServletRequest request, QueryDao queryDao, CustomPropertiesConfig queryConf, String token, String company) {
        try {
            RSAPrivateKey privateKey = RSAUtil.getPrivateKey(env.getProperty("rsa.authing.privateKey"));
            DecodedJWT decode = JWT.decode(RSAUtil.privateDecrypt(token, privateKey));
            String permissionList = decode.getClaim("permissionList").asString();
            String[] pers = new String(Base64.getDecoder().decode(permissionList.getBytes())).split(",");
            for (String per : pers) {
                String[] perList = per.split(":");
                if (perList.length > 1 && perList[1].equalsIgnoreCase(queryConf.getCompanyAction())) return true;
            }

            String login = queryDao.getOneIdUserGiteeLoginName(request);
            if (StringUtils.isNotBlank(login)) {
                String org = queryDao.queryUserCompany(queryConf, login);
                ArrayList<String> companyNameList = queryDao.getCompanyNameList(company);
                for (String name : companyNameList) {
                    if (org.equals(name)) return false;//true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map<String, Object> giteeWebhookUser(JsonNode user, String createdAt) {
        if (user == null || user.isNull() || user.isEmpty()) return null;

        String email = user.get("email").asText();
        if (StringUtils.isBlank(email)) return null;

        long id = user.get("id").asLong();
        String login = user.get("login").asText();

        return Map.of("id", id, "gitee_id", login, "email", email, "created_at", createdAt);
    }
}
