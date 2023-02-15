package com.datastat.config.context;

import com.datastat.config.QueryConfig;
import com.datastat.model.CustomPropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class QueryConfContext {
    @Autowired
    private Map<String, CustomPropertiesConfig> queryConfMap;

    @Autowired
    QueryConfig queryConfig;

    public CustomPropertiesConfig getQueryConfig(String type) {
        return queryConfMap.getOrDefault(type, queryConfig);
    }
}
