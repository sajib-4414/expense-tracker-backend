package com.sajib_4414.expense.tracker.models.expense;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Repository
@AllArgsConstructor
public class ExpenseRepository {
    private EntityManager entityManager;

    @Transactional
    public void save(Expense expense){
        entityManager.persist(expense);
    }

    public List<Expense> getAllExpenseByUser (String username){
        return entityManager
                .createQuery("SELECT e from Expense e JOIN e.owner u" +
                " WHERE u.username = :username"+username, Expense.class)
                .setParameter("username",username)
                .getResultList();
    }
}
