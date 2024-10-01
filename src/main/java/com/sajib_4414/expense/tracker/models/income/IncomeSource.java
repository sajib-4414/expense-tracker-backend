package com.sajib_4414.expense.tracker.models.income;

import com.sajib_4414.expense.tracker.models.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "income_source", indexes = @Index(name = "created_by_index",columnList = "created_by"))
public class IncomeSource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn(name = "created_by")
    @ManyToOne
    private User createdBy;

    @Column(name = "name", nullable = false)
    private String name;
}
