package com.sajib_4414.expense.tracker.payload;

import com.sajib_4414.expense.tracker.models.income.IncomeSource;
import com.sajib_4414.expense.tracker.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDTO {


    @NotNull(message = "amount is required")
    @Min(value = 1,message = "Please provide a valid amount")
    private Integer amount;

    @Column(name = "notes")
    private String notes;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "Date is required")
    private Date dateTime;

    @NotNull(message = "incomeSource is required")
    @Min(value = 1,message = "Please provide a valid incomeSource")
    private int income_source_id;
}
