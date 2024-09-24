package com.sajib_4414.expense.tracker.user;

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
@Table(name = "e_roles")
public class Role {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    private User user;

    @Column(name = "name")
    private String name;
}
