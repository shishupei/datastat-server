package com.datastat.config;

import com.datastat.model.CustomPropertiesConfig;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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
        String queryJson = groupField.equals("company") ? getGiteeAggCompanyQueryStr() : getGiteeAggUserQueryStr();
        repo = repo == null ? "*" : String.format("\\\"https://gitee.com/%s\\\"", repo);

        return getQueryStrByType(contributeType, queryJson, timeRange, repo);
    }

    @Override
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
        return getQueryStrWithTimeRange(queryJson, timeRange, group, label, orDefault);
    }

}
