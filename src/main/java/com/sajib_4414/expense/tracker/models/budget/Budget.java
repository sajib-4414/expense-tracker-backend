package com.sajib_4414.expense.tracker.models.budget;


import com.fasterxml.jackson.annotation.JsonIgnore;

import com.sajib_4414.expense.tracker.models.BaseEntity;
import com.sajib_4414.expense.tracker.models.user.User;
import jakarta.persistence.*;

import lombok.*;


import java.time.LocalDate;

import java.util.List;



@Entity
@Table(name = "budgets", indexes = @Index(columnList = "user_id, budget_month_year"))
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

    @Temporal(TemporalType.DATE)
    @Column(name = "budget_month_year")

    private LocalDate budgetPeriod;


    @Column(name = "estimated_income")

    private int estimatedIncome;

    @Column(name = "max_spend")

    private int maxSpend;

    @Column(name = "warning_spend")

    private int warningSpend;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BudgetItem> budgetItemList;

//    @JsonIgnore
//    private  BudgetRepository budgetRepository;
//
//    @PrePersist
//    public void prePersist() {
//        // logic before persisting
//        // check if for this user, for this month, a budget is already defined.
//        //if yes, reject
//        boolean ifBudgetExistForMonth = budgetRepository.ifBudgetExistForUserAndMonth(getCurrentUser().getId(),startDate);
//        if(ifBudgetExistForMonth)
//            throw new BadDataException("Budget already exists for the month");
//    }


//    @PreUpdate
//    public void preUpdate() {
//        // logic before updating
//        //check if the user is owner of this budget
//        //if not reject it
//
//    }


}
