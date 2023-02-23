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

package com.datastat.model;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Data
public class CustomPropertiesConfig {
    // -- other --
    private String tokenUserName;
    private String tokenUserPassword;
    private String tokenBasePassword;
    private String tokenExpireSeconds;
    private String accessToken;
    private String multiCommunity;
    private String sigOwnerType;
    private String allSigOwnerType;
    private String companyUsers;
    private String sigFeatureUrl;
    private String userOwnsSigStr;
    private String tcOwnerUrl;
    private String UserReportCsvData;
    private String businessOsv;
    private String blueZoneApiToken;
    private String productLineCodeAllUpdate;
    private String companyAction;
    private String sigAction;

    // -- index --
    private String extOsIndex;
    private String sigIndex;
    private String usersIndex;
    //    private String contributorsIndex;
    private String noticeUsersIndex;
    //    private String communityMembersIndex;
    private String giteeAllIndex;
    private String claCorporationIndex;
    private String cveDetailsIndex;
    private String durationAggIndex;
    private String bugQuestionnaireIndex;
    private String obsDetailsIndex;
    private String isoBuildIndex;
    private String sigDetailsIndex;
    private String issueScoreIndex;
    private String buildCheckResultIndex;
    private String buildCheckMistakeIndex;
    private String meetingsIndex;
    private String sigScoreIndex;
    private String sigRadarScoreIndex;
    private String trackerIndex;
    private String accountOrgIndex;
    private String downloadIndex;
    private String starForkIndex;
    private String blueZoneUserIndex;
    private String blueZoneUserContributesIndex;
    private String giteeEmailIndex;

    // -- query str --
    private String extOsQueryStr;
    private String sigQueryStr;
    private String usersQueryStr;
    private String contributorsQueryStr;
    private String giteeContributesQueryStr;
    private String noticeUsersQueryStr;
    private String communityMembersQueryStr;
    private String giteeAggCompanyQueryStr;
    private String giteeAggUserQueryStr;
    private String durationAggQueryStr;
    private String obsDetailsQueryStr;
    private String isoBuildQueryStr;
    private String sigDetailsQueryStr;
    private String issueScoreQueryStr;
    private String sigNameQueryStr;
    private String sigInfoQueryStr;
    private String sigRepoQueryStr;
    private String sigGiteeQueryStr;
    private String sigContributeQueryStr;
    private String sigMeetingsQueryStr;
    private String sigMaintainersQueryStr;
    private String companyNameQueryStr;
    private String companyUserQueryStr;
    private String companySigQueryStr;
    private String companyContributeQueryStr;
    private String companyMeetingsQueryStr;
    private String companyMaintainersQueryStr;
    private String companyAggUserQueryStr;
    private String sigAggUserQueryStr;
    private String companyAggSigQueryStr;
    private String sigScoreQueryStr;
    private String allSigScoreQueryStr;
    private String allCompanySigQueryStr;
    private String groupAggSigQueryStr;
    private String userOwnerTypeQueryStr;
    private String allUserOwnerTypeQueryStr;
    private String userListQueryStr;
    private String SigRepoCommittersQueryStr;
    private String giteeAllIssueByMilQueryStr;
    private String giteeAllQueryStr;
    private String bugQuestionnaireQueryStr;
    private String accountOrgQueryStr;
    private String downloadQueryStr;
    private String giteeStarCountQueryStr;
    private String giteeIssueCountQueryStr;
    private String giteePrCountQueryStr;
    private String starForkQueryStr;
    private String monthCountQueryStr;
    private String sigYamlEn;
    private String sigYamlZh;
    private String sigLabelQueryStr;
    private String communityRepoQueryStr;
    private String sigCountQuery;

    protected static final Map<String, String> contributeTypeMap = new HashMap<>();

    @PostConstruct
    public void init() {
        contributeTypeMap.put("pr", "is_pull_state_merged");
        contributeTypeMap.put("issue", "is_gitee_issue");
        contributeTypeMap.put("comment", "is_gitee_comment");
    }

    public String getCountQueryStr(String item) {
        String queryStr = "";
        switch (item) {
            case "stars":
                queryStr = getGiteeStarCountQueryStr();
                break;
            case "issues":
                queryStr = getGiteeIssueCountQueryStr();
                break;
            case "prs":
                queryStr = getGiteePrCountQueryStr();
                break;
            default:
                return "";
        }
        return queryStr;
    }

    public String getAggCountQueryStr(CustomPropertiesConfig queryConf, String groupField, String contributeType, String timeRange, String community, String repo, String sig) {
        String queryJson = groupField.equals("company") ? getGiteeAggCompanyQueryStr() : getGiteeAggUserQueryStr();
        return getQueryStrByType(contributeType, queryJson, timeRange, null);
    }

    public String getQueryStrByType(String contributeType, String queryJson, String timeRange, String item) {
        String orDefault = contributeTypeMap.getOrDefault(contributeType, "");
        if (StringUtils.isBlank(orDefault)) return "";
        return getQueryStrWithTimeRange(queryJson, timeRange, item, orDefault);
    }

    public String getAggGroupCountQueryStr(String groupField, String group, String contributeType, String timeRange, String label) {
        String queryJson;
        switch (groupField) {
            case "sig":
                queryJson = getSigAggUserQueryStr();
                break;
            case "company":
                queryJson = getCompanyAggUserQueryStr();
                break;
            default:
                return null;
        }
        String orDefault = contributeTypeMap.getOrDefault(contributeType, "");
        if (StringUtils.isBlank(orDefault)) return null;
        return getQueryStrWithTimeRange(queryJson, timeRange, group, orDefault);
    }

    public String getAggGroupSigCountQueryStr(String queryJson, String contributeType, String timeRange, String group, String field) {
        String orDefault = contributeTypeMap.getOrDefault(contributeType, "");
        if (StringUtils.isBlank(orDefault)) return null;
        return getQueryStrWithTimeRange(queryJson, timeRange, field, group, orDefault);
    }

    public String[] getAggCompanyGiteeQueryStr(String queryJson, String timeRange, String company) {
        if (queryJson == null) return null;

        String[] queryJsons = queryJson.split(";");
        String[] queryStr = new String[queryJsons.length];
        for (int i = 0; i < queryJsons.length; i++) {
            queryStr[i] = getQueryStrWithTimeRange(queryJsons[i], timeRange, company);
        }
        return queryStr;
    }

    public String getQueryStrWithTimeRange(String queryJson, String timeRange, Object... args) {
        if (queryJson == null) return null;

        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = getPastTime(timeRange);
        return queryStrFormat(queryJson, lastTimeMillis, currentTimeMillis, args);
    }

    public ArrayList<Object> getAggUserCountQueryParams(String contributeType, String timeRange) {
        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = getPastTime(timeRange);
        ArrayList<Object> list = new ArrayList<>();
        list.add(contributeType);
        list.add(lastTimeMillis);
        list.add(currentTimeMillis);
        switch (contributeType.toLowerCase()) {
            case "pr":
                list.add("is_pull_state_merged");
                list.add("pull_title");
                list.add("pull_url");
                list.add("pull_id_in_repo");
                break;
            case "issue":
                list.add("is_gitee_issue");
                list.add("issue_title");
                list.add("issue_url");
                list.add("issue_id_in_repo");
                break;
            case "comment":
                list.add("is_gitee_comment");
                list.add("body");
                list.add("sub_type");
                list.add("id");
                break;
            default:
                return null;
        }
        return list;
    }

    public String getUserContributeDetailsQuery(CustomPropertiesConfig queryConf, String sig, String label) {
        return null;
    }


    protected static String queryStrFormat(String queryJson, Object... args) {
        return String.format(queryJson, args);
    }

    protected long getPastTime(String timeRange) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        switch (timeRange.toLowerCase()) {
            case "lastonemonth":
                c.add(Calendar.MONTH, -1);
                break;
            case "lasthalfyear":
                c.add(Calendar.MONTH, -6);
                break;
            case "lastoneyear":
                c.add(Calendar.YEAR, -1);
                break;
            default:
                c.setTimeInMillis(0);
        }
        return c.getTimeInMillis();
    }
}
