package com.sajib_4414.expense.tracker.payload;

import lombok.Data;

@Data
public class IncomeByIncomeSourceDTO {
    private String income_source_name;
    private Long income_total;
    private int income_month;
    private int income_source_id;

    public IncomeByIncomeSourceDTO(Long income_total, String income_source_name,int income_source_id , int income_month ) {
        this.income_total = income_total;
        this.income_month = income_month;
        this.income_source_id = income_source_id;
        this.income_source_name = income_source_name;
    }
}
