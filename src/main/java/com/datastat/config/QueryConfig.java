package com.datastat.config;

import com.datastat.model.CustomPropertiesConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "custom")
@PropertySource(value = {"file:${user.dir}/custom.properties"})
@Configuration("queryConf")
@Data
public class QueryConfig extends CustomPropertiesConfig {
}
