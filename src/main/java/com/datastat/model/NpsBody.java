package com.datastat.model;

import lombok.Data;

@Data
public class NpsBody {
    private String feedbackPageUrl;
    private int feedbackValue;
    private String feedbackText;
}
