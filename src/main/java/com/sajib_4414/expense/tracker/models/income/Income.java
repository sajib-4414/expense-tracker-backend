package com.sajib_4414.expense.tracker.models.income;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sajib_4414.expense.tracker.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "income", indexes = @Index(columnList = "user_id"))
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount")
    private int amount;

    @Column(name = "notes")
    private String notes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time")
    private Date dateTime;

    @ManyToOne
    @JoinColumn(name = "income_source_id")
    private IncomeSource incomeSource;
}
