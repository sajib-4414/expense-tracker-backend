package com.sajib_4414.expense.tracker.models.expense;

import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.category.CategoryRepository;
import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.models.user.UserRepository;
import com.sajib_4414.expense.tracker.payload.ExpenseDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ExpenseRepository {
    private EntityManager entityManager;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    public List<Expense> getAllExpenseByUser (String username){
        return entityManager
                .createQuery("SELECT e from Expense e JOIN e.owner u" +
                " WHERE u.username = :username", Expense.class)
                .setParameter("username",username)
                .getResultList();
    }

    @Transactional
    public Expense createExpenseForUser(String username, ExpenseDTO payload) {
        User owner = userRepository.findByUsername(username).orElseThrow(()-> new ItemNotFoundException("User not found"));
        Category category = categoryRepository.findById(payload.getCategory_id()).orElseThrow(()-> new ItemNotFoundException("Category not found"));
        Expense expense = Expense
                .builder()
                .owner(owner)
                .cost(payload.getCost())
                .category(category)
                .build();
        entityManager.persist(expense);
        return expense;
    }


    public Expense updateCost(Expense expense, ExpenseDTO payload){
        expense.setCost(payload.getCost());
        entityManager.merge(expense);
        return expense;
    }

    public Optional<Expense> findById(int id){
        Expense expense = entityManager.find(Expense.class,id);
        return Optional.ofNullable(expense);
    }

    public Expense updateCategory(Expense expense, Category newCategory) {
        expense.setCategory(newCategory);
        entityManager.merge(expense);
        return expense;
    }

    public void remove(Expense validatedExpense) {
        System.out.println("I am inside remove, expense="+validatedExpense);
        try{
            entityManager.createQuery("DELETE FROM Expense e WHERE e.id = :id")
                    .setParameter("id",validatedExpense.getId())
                    .executeUpdate();
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("caught this exception,"+e);
        }
        catch (Exception e){
            System.out.println("caught this generic,"+e);
        }

    }
}
