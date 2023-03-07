package com.datastat.dao.context;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastat.dao.metric.*;

@Repository
public class MetricDaoContext {
    @Autowired
    private Map<String, MetricDao> metricDaoMap;

    @Autowired
    SigMetricDao metricDao;

    public MetricDao getQueryMetricsDao(String serviceType) {
        return metricDaoMap.getOrDefault(serviceType, metricDao);
    }
}