package com.sajib_4414.expense.tracker.models.expense;

import com.sajib_4414.expense.tracker.config.console;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.category.CategoryRepository;
import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.models.user.UserRepository;
import com.sajib_4414.expense.tracker.payload.CategoryExpense;
import com.sajib_4414.expense.tracker.payload.ExpenseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.NonUniqueResultException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ExpenseRepository {
    private EntityManager entityManager;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

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
                .notes(payload.getNotes())
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
            //alternate raw query
//            entityManager.createQuery("DELETE FROM Expense e WHERE e.id = :id")
//                    .setParameter("id",validatedExpense.getId())
//                    .executeUpdate();
            entityManager.remove(validatedExpense);
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("caught this exception,"+e);
        }
        catch (Exception e){
            System.out.println("caught this generic,"+e);
        }

    }

    public Expense updateExpense(Expense dbRetrievedExpense, ExpenseDTO expenseDTO) {
        //this auto maps the compatiable fields only like cost, notes. for which DTO and model fields name are the same
        modelMapper.map(expenseDTO, dbRetrievedExpense);

        //for category, atuomatica mapping is not going to work, as client passes only id
        Category newCategory = categoryRepository.findById(expenseDTO.getCategory_id()).orElseThrow(()->new ItemNotFoundException("category now found"));
        dbRetrievedExpense.setCategory(newCategory);
        entityManager.merge(dbRetrievedExpense);
        //automatically updates
        return dbRetrievedExpense;
    }

    public Double getTotalExpenseOfMonth(int month, int user_id){
        Query query= entityManager.createNativeQuery("SELECT sum(COALESCE(e_expense.cost, 0)) " +
                        "FROM e_expense where e_expense.owner_id= :user_id and " +
                        "EXTRACT(MONTH FROM e_expense.date_time) = :month")
                .setParameter("month",month)
                .setParameter("user_id",user_id);

        Object result = query.getSingleResult();
        return result != null ? ((Number) result).doubleValue() : 0.0;
    }
    public List<CategoryExpense> getTop5CategoryExpenseOfMonth(int month, int user_id){
        TypedQuery<CategoryExpense> query = entityManager.createQuery(
                "select new com.sajib_4414.expense.tracker.payload.CategoryExpense(ec.id, ec.name, sum(ex.cost)) " +
                        "from Expense ex LEFT JOIN ex.category ec " +
                        "where ex.owner.id = :user_id " +
                        "and EXTRACT(month from ex.dateTime) = :month " +
                        "group by ec.id order by sum(ex.cost) desc",
                CategoryExpense.class
        );
        query.setParameter("user_id", user_id);
        query.setParameter("month", month);
        query.setMaxResults(5);

        return query.getResultList();
    }

//    public Boolean isCategoryOwner(String username, int categoryId) {
//
//        try{
//            Category category = entityManager.createQuery("select c from Category c where c.id= :categoryId " +
//                            "and c.createdBy.id=(select u.id from User u where u.username= :username)", Category.class)
//                    .setParameter("username",username )
//                    .setParameter("categoryId",categoryId)
//                    .getSingleResult();
//            return category!=null;
//        } catch (NoResultException e) {
//            return false; // No category found
//        } catch (NonUniqueResultException e) {
//            // Handle the case where multiple results are found
//            return false; // or log an error
//        }

//    }
}
