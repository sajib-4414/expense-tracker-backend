package com.sajib_4414.expense.tracker.payload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IncomeSummaryDTO {
    private Double totalIncomeThisMonth;
    private Double totalIncomeLastMonth;
    private Double budgetedIncomeThisMonth;
    private List<IncomeByIncomeSource> incomeListBySource;
}
