package com.sajib_4414.expense.tracker.models.budget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sajib_4414.expense.tracker.models.BaseEntity;
import com.sajib_4414.expense.tracker.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "budget", indexes = @Index(columnList = "user_id, start_date, end_date"))
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Budget extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "estimated_income")
    private int estimatedIncome;

    @Column(name = "max_spend")
    private int maxSpend;

    @Column(name = "warning_spend")
    private int warningSpend;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BudgetItem> budgetItemList;
}
