package com.datastat.model;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class IsvCount {
    @Min(value = 0, message = "Value must be greater than or equal to 0")
    private int count;

    private String token;
}
