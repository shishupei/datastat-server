package com.datastat.config;

import com.datastat.model.CustomPropertiesConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "mindspore")
@PropertySource(value = {"file:${user.dir}/mindSpore.properties"})
@Configuration("mindsporeConf")
@Data
public class MindSporeConfig extends CustomPropertiesConfig {
}
