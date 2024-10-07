package com.sajib_4414.expense.tracker.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetItemDTO {
    @NotNull(message = "category_id is required")
    private Integer category_id;

    @NotNull(message = "budget_id is required")
    private Integer budget_id;

    @NotNull(message = "maxSpend is required")
    @Min(value = 1, message = "Please provide a valid maxSpend")
    private Integer maxSpend;

    @NotNull(message = "warningSpend is required")
    @Min(value = 1, message = "Please provide a valid warningSpend")
    private Integer warningSpend;
}