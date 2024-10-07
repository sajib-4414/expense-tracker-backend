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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "start date is required")
    private Date startDate;


    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "end date is required")
    private Date endDate;


    @NotNull(message = "estimatedIncome date is required")
    @Min(value = 1,message = "min 1$ income is required")
    private int estimatedIncome;


    @NotNull(message = "maxSpend  is required")
    @Min(value = 1,message = "min 1$ maxSpend is required")
    private int maxSpend;


    @NotNull(message = "warningSpend  is required")
    @Min(value = 1,message = "min 1$ warningSpend is required")
    private int warningSpend;
    // Getters and Setters
}
