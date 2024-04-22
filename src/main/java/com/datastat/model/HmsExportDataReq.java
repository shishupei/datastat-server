package com.datastat.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HmsExportDataReq implements Serializable{
    private static final long serialVersionUID = 1L;
    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("status_time")
    private String statusTime;
    
}
