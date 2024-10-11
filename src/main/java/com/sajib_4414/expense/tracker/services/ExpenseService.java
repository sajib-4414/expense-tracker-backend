package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.config.Roles;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.PermissionError;
import com.sajib_4414.expense.tracker.models.budget.Budget;
import com.sajib_4414.expense.tracker.models.budget.BudgetRepository;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.category.CategoryRepository;
import com.sajib_4414.expense.tracker.models.expense.Expense;
import com.sajib_4414.expense.tracker.models.expense.ExpenseRepository;
import com.sajib_4414.expense.tracker.models.user.Role;
import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.payload.CategoryExpense;
import com.sajib_4414.expense.tracker.payload.ExpenseDTO;
import com.sajib_4414.expense.tracker.payload.PagedResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.sajib_4414.expense.tracker.config.Helper.getCurrentUser;

@Service
@AllArgsConstructor
public class ExpenseService {
    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;
    private BudgetRepository budgetRepository;


    @Transactional
    public PagedResponse<Expense> getCurrentUserExpense(int page, int size){
        PagedResponse<Expense> pagedResponse =  expenseRepository.getAllExpenseByUser(getCurrentUser().getUsername(), page, size);
        return pagedResponse;
    }

    public Expense createExpense(ExpenseDTO payload) {

        //check if the user is admin, then allow creating expense in any categories, although admin wont do that ever.
        //if not admin, check if thats the category he has access to.
        User currentUser = getCurrentUser();
        Collection<? extends GrantedAuthority> authorities = getCurrentUser().getAuthorities();
        List<GrantedAuthority> userAuthorities = new ArrayList<>(authorities);

        Optional<GrantedAuthority> adminAuthority = userAuthorities.stream().filter( authority -> authority.getAuthority().equals(Roles.ADMIN)).findAny();
        if(adminAuthority.isPresent()){
            //he is an admin, allow creating with any category
            return expenseRepository.createExpenseForUser(currentUser.getUsername(), payload);
        }
        else{
            //check if he own the category
            //check if we can get any category with his username
            Boolean isOwner = categoryRepository.isUserCategoryOwner( currentUser.getUsername(), payload.getCategory_id());
            if(isOwner)
                return expenseRepository.createExpenseForUser(currentUser.getUsername(), payload);
            else
                throw new PermissionError("You dont have permission to create this expense with this category");
        }

    }

    @Transactional
    public Expense updateCost(ExpenseDTO payload, int expenseId) {
        Expense validatedExpense = validate(expenseId);
        if(validatedExpense.getCost()== payload.getCost())
            return validatedExpense;
        return expenseRepository.updateCost(validatedExpense, payload);
    }

    @Transactional
    public Expense updateCategory(ExpenseDTO payload, int expenseId) {
        Expense validatedExpense = validate(expenseId);
        if(validatedExpense.getCategory().getId() == payload.getCategory_id())
            return validatedExpense;
        Category newCategory = categoryRepository.findById(payload.getCategory_id()).orElseThrow(()->new ItemNotFoundException("category now found"));
        return expenseRepository.updateCategory(validatedExpense, newCategory);
    }

    public Expense validate(int expenseId){
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);
        if(!optionalExpense.isPresent()){
            throw new ItemNotFoundException("Expense not found");
        }

        if(!optionalExpense.get().getUser().getUsername().equals(getCurrentUser().getUsername())){
            throw new PermissionError();
        }
        return optionalExpense.get();
    }

    @Transactional
    public void delete(@Valid int expenseId) {
        Expense validatedExpense = validate(expenseId);
        expenseRepository.remove(validatedExpense);
    }

    @Transactional
    public Expense update(@Valid ExpenseDTO payload, @Valid int expenseId) {
        Expense validatedExpense = validate(expenseId);
        return expenseRepository.updateExpense(validatedExpense, payload);
    }


    public List<CategoryExpense> getExpenseSummaryView() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        Double expenseThisMonth = expenseRepository.getTotalExpenseOfMonth(currentMonth, getCurrentUser().getId());
        Budget budgetOfMonth = budgetRepository.getBudgetWithItems()

        return expenseRepository.getExpenseListOfMonthByCategory(currentMonth, getCurrentUser().getId());
    }
}
