package com.datastat.model.dto;
import com.datastat.aop.moderation.ModerationValid;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NpsIssueBody {
    @Size(max = 200,  message = "the length can not exceed 200")
    private String pkgId;

    @Size(max = 50,  message = "the length can not exceed 200")
    private String name;

    @Size(max = 50,  message = "the length can not exceed 200")
    private String version;

    @Size(max = 10,  message = "the length can not exceed 10")
    private String type;

    @Size(max = 100,  message = "the length can not exceed 100")
    private String srcRepo;

    @Size(max = 20,  message = "the length can not exceed 20")
    private String maintainer;

    @Size(max = 50,  message = "the length can not exceed 50")
    private String maintainerEmail;

    @Size(max = 500,  message = "the length can not exceed 500")
    @Pattern(regexp = "^((http|https)://)[^<>{}\\[\\])]*$", message = "Url format error")
    private String feedbackPageUrl;

    @Min(value = 0, message = "Value must be greater than or equal to 0")
    @Max(value = 5, message = "Value must be less than or equal to 10")
    private int feedbackValue;
    
    @Size(max = 500,  message = "the length can not exceed 500")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    @ModerationValid
    private String feedbackText;
}
