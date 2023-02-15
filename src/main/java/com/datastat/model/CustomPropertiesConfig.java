package com.datastat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

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
    private String meetingEsUrl;
    private String meetingUserPass;

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

    public String getCountQueryStr(String item) {
        String queryStr = "";
        switch (item) {
            case "stars":
                queryStr = giteeStarCountQueryStr;
                break;
            case "issues":
                queryStr = giteeIssueCountQueryStr;
                break;
            case "prs":
                queryStr = giteePrCountQueryStr;
                break;
            default:
                return "";
        }
        return queryStr;
    }

    public String getAggCountQueryStr(CustomPropertiesConfig queryConf, String groupField, String contributeType, String timeRange, String community, String repo, String sig) {
        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = getPastTime(timeRange);
        String queryJson = groupField.equals("company") ? getGiteeAggCompanyQueryStr() : getGiteeAggUserQueryStr();

        return getQueryStrByType(contributeType, queryJson, lastTimeMillis, currentTimeMillis, null);
    }

    protected String getQueryStrByType(String contributeType, String queryJson, long lastTimeMillis, long currentTimeMillis, String item) {
        String queryStr;
        switch (contributeType.toLowerCase()) {
            case "pr":
                queryStr = getQueryStrByType(queryJson, lastTimeMillis, currentTimeMillis, item, "is_pull_state_merged");
                break;
            case "issue":
                queryStr = getQueryStrByType(queryJson, lastTimeMillis, currentTimeMillis, item, "is_gitee_issue");
                break;
            case "comment":
                queryStr = getQueryStrByType(queryJson, lastTimeMillis, currentTimeMillis, item, "is_gitee_comment");
                break;
            default:
                return "";
        }
        return queryStr;
    }

    protected String getQueryStrByType(String queryJson, long lastTimeMillis, long currentTimeMillis, String item, String filterField) {
        return item == null
                ? String.format(queryJson, lastTimeMillis, currentTimeMillis, filterField)
                : String.format(queryJson, lastTimeMillis, currentTimeMillis, item, filterField);
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

    public String getAggGroupCountQueryStr(String groupField, String group, String contributeType, String timeRange, String label) {
        String queryStr;
        String queryJson;
        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = getPastTime(timeRange);
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
        if (queryJson == null) return null;

        switch (contributeType.toLowerCase()) {
            case "pr":
                queryStr = String.format(queryJson, lastTimeMillis, currentTimeMillis, group, "is_pull_state_merged");
                break;
            case "issue":
                queryStr = String.format(queryJson, lastTimeMillis, currentTimeMillis, group, "is_gitee_issue");
                break;
            case "comment":
                queryStr = String.format(queryJson, lastTimeMillis, currentTimeMillis, group, "is_gitee_comment");
                break;
            default:
                queryStr = null;
        }
        return queryStr;
    }

    public String getAggGroupSigCountQueryStr(String queryJson, String contributeType, String timeRange, String group, String field) {
        String queryStr;
        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = getPastTime(timeRange);

        switch (contributeType.toLowerCase()) {
            case "pr":
                queryStr = String.format(queryJson, lastTimeMillis, currentTimeMillis, field, group, "is_pull_state_merged");
                break;
            case "issue":
                queryStr = String.format(queryJson, lastTimeMillis, currentTimeMillis, field, group, "is_gitee_issue");
                break;
            case "comment":
                queryStr = String.format(queryJson, lastTimeMillis, currentTimeMillis, field, group, "is_gitee_comment");
                break;
            default:
                return null;
        }
        return queryStr;
    }

    public String[] getAggCompanyGiteeQueryStr(String queryJson, String timeRange, String company) {
        if (queryJson == null) {
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = getPastTime(timeRange);
        String[] queryJsons = queryJson.split(";");
        String[] queryStr = new String[queryJsons.length];
        for (int i = 0; i < queryJsons.length; i++) {
            queryStr[i] = String.format(queryJsons[i], lastTimeMillis, currentTimeMillis, company);
        }
        return queryStr;
    }

    public String getSigScoreQuery(String queryJson, String timeRange, String sig) {
        if (queryJson == null) return null;

        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = getPastTime(timeRange);
        return String.format(queryJson, lastTimeMillis, currentTimeMillis, sig);
    }

}
