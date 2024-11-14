package com.sajib_4414.expense.tracker.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetListItemDTO {

    private LocalDate budget_period;
    private Integer maximum_expense;
    private Double total_income;
    private Double total_expense;

}
