package com.sajib_4414.expense.tracker.models.expense;

import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.models.user.UserRepository;
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
    private UserRepository userRepository;

    public List<Expense> getAllExpenseByUser (String username){
        return entityManager
                .createQuery("SELECT e from Expense e JOIN e.owner u" +
                " WHERE u.username = :username", Expense.class)
                .setParameter("username",username)
                .getResultList();
    }

    @Transactional
    public Expense createExpenseForUser(String username, Expense payload) {
        User user = userRepository.findByUsername(username).orElseThrow();
        payload.setOwner(user);
        System.out.println("payload category="+payload.getCategory());
        entityManager.persist(payload);
        return payload;
    }
}
