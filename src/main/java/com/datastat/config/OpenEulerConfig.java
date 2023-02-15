package com.datastat.config;

import com.datastat.model.CustomPropertiesConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "openeuler")
@PropertySource(value = {"file:${user.dir}/openEuler.properties"})
@Configuration("openeulerConf")
@Data
public class OpenEulerConfig extends CustomPropertiesConfig {
    @Override
    public String getAggCountQueryStr(CustomPropertiesConfig queryConf, String groupField, String contributeType, String timeRange, String community, String repo, String sig) {
        long currentTimeMillis = System.currentTimeMillis();
        long lastTimeMillis = getPastTime(timeRange);
        String queryJson = groupField.equals("company") ? getGiteeAggCompanyQueryStr() : getGiteeAggUserQueryStr();
        sig = sig == null ? "*" : sig;

        return groupField.equals("company")
                ? getQueryStrByType(contributeType, queryJson, lastTimeMillis, currentTimeMillis, sig)
                : getQueryStrByType(contributeType, queryJson, lastTimeMillis, currentTimeMillis, null);
    }
}
