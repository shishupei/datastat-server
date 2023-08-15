package com.datastat.model.yaml;

import java.util.List;

import lombok.Data;

@Data
public class InnovationItemInfo {
    private String project_name;
    private List<String> repos;
}
