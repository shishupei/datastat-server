package com.datastat.config;

import com.datastat.model.CustomPropertiesConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "opengauss")
@PropertySource(value = {"file:${user.dir}/openGauss.properties"})
@Configuration("opengaussConf")
@Data
public class OpenGaussConfig extends CustomPropertiesConfig {

    @Override
    public String getAggCountQueryStr(CustomPropertiesConfig queryConf, String groupField, String contributeType, String timeRange, String community, String repo, String sig) {
        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = getPastTime(timeRange);
        String queryJson = groupField.equals("company") ? getGiteeAggCompanyQueryStr() : getGiteeAggUserQueryStr();
        repo = repo == null ? "*" : String.format("\\\"https://gitee.com/%s\\\"", repo);

        return getQueryStrByType(contributeType, queryJson, lastTimeMillis, currentTimeMillis, repo);
    }

    @Override
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
                queryStr = String.format(queryJson, lastTimeMillis, currentTimeMillis, group, label, "is_pull_state_merged");
                break;
            case "issue":
                queryStr = String.format(queryJson, lastTimeMillis, currentTimeMillis, group, label, "is_gitee_issue");
                break;
            case "comment":
                queryStr = String.format(queryJson, lastTimeMillis, currentTimeMillis, group, label, "is_gitee_comment");
                break;
            default:
                queryStr = null;
        }
        return queryStr;
    }

}
