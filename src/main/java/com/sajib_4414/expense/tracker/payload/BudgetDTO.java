package com.sajib_4414.expense.tracker.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "budget_period  is required")
    private Date budget_period;


    @NotNull(message = "estimatedIncome date is required")
    @Min(value = 1,message = "min 1$ income is required")
    private Integer estimatedIncome;


    @NotNull(message = "maxSpend  is required")
    @Min(value = 1,message = "min 1$ maxSpend is required")
    private Integer maxSpend;


    @NotNull(message = "warningSpend  is required")
    @Min(value = 1,message = "min 1$ warningSpend is required")
    private Integer warningSpend;
    // Getters and Setters
}

