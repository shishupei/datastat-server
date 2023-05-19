package com.datastat.model;

import java.util.HashMap;

import lombok.Data;

@Data
public class QaBotRequestBody {
    private String question;
    private String top;
    private HashMap<String, Object> extend;
    private String session_id;
    private String feedback;
    private String comment;
    private String request_id;
    private String degree;
    private String feedback_tag;
}
