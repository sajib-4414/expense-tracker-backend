package com.sajib_4414.expense.tracker.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class IncomeSourceDTO {

    @NotEmpty(message = "name is required")
    private String name;
}
