package com.sajib_4414.expense.tracker.models.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    //this is a method we declared, and jpa will automatically create the method as followed the convention
    //we dont need implementation
    Optional<User> findByEmail(String email);
}
