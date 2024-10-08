package com.sajib_4414.expense.tracker.models.income;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sajib_4414.expense.tracker.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "income_sources", indexes = @Index(name = "created_by_index",columnList = "user"))
public class IncomeSource {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "user")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

    @Column(name = "name", nullable = false)
    @NotEmpty(message = "name cannot be null")
    private String name;
}
