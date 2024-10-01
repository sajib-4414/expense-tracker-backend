package com.sajib_4414.expense.tracker.models.category;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sajib_4414.expense.tracker.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "e_categories", indexes = @Index(columnList = "name"))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "name",nullable = false, unique = true)
    private String name;
}
