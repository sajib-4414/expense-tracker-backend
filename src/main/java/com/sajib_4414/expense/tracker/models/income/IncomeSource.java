package com.sajib_4414.expense.tracker.models.income;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sajib_4414.expense.tracker.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "income_source", indexes = @Index(name = "created_by_index",columnList = "created_by"))
public class IncomeSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "created_by")
    @ManyToOne
    @JsonIgnore
    @JsonBackReference //reduntant, use in case we dont ignore
    private User createdBy;

    @Column(name = "name", nullable = false)
    private String name;
}
