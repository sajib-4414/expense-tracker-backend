package com.sajib_4414.expense.tracker.models.user;

//public enum Role {
//    USER,
//    ADMIN
//}

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", indexes = @Index(columnList = "name"))
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "name",nullable = false, unique = true)
    private String name;
}
