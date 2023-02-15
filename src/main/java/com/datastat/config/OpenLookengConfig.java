package com.datastat.config;

import com.datastat.model.CustomPropertiesConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "openlookeng")
@PropertySource(value = {"file:${user.dir}/openLookeng.properties"})
@Configuration("openlookengConf")
@Data
public class OpenLookengConfig extends CustomPropertiesConfig {
}
