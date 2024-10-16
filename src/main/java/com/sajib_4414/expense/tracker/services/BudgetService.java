package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.config.console;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.models.budget.*;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.category.CategoryRepository;
import com.sajib_4414.expense.tracker.models.expense.ExpenseRepository;
import com.sajib_4414.expense.tracker.payload.BudgetDTO;
import com.sajib_4414.expense.tracker.payload.BudgetItemDTO;
import com.sajib_4414.expense.tracker.payload.BudgetSummaryBoardDTO;
import com.sajib_4414.expense.tracker.payload.CategoryWiseBudgetSummary;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.sajib_4414.expense.tracker.config.Helper.getCurrentUser;

@Service
@AllArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetQRepository budgetQRepository;
    private final BudgetItemRepository budgetItemRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private ModelMapper modelMapper;

    @Transactional
    public Budget createBudget(BudgetDTO payload){
        Budget newBudget = new Budget();
        modelMapper.map(payload,newBudget );
        newBudget.setUser(getCurrentUser());
        budgetRepository.save(newBudget);
        newBudget.setBudgetItemList(new ArrayList<>());
        return newBudget;
    }

    public List<Budget> getMyBudgets(){
        return budgetRepository.findByUser(getCurrentUser());
    }

    @Transactional
    public Budget updateBudget(BudgetDTO payload, int budgetId){
        Budget budget = budgetRepository.findByIdAndUser(budgetId, getCurrentUser()).orElseThrow(()->new ItemNotFoundException("budget not found"));
        modelMapper.map(payload, budget);
        budgetRepository.save(budget);
        return budget;
    }

    @Transactional
    public void deleteBudget(int budgetId){
        Budget budget = budgetRepository.findByIdAndUser(budgetId, getCurrentUser()).orElseThrow(()->new ItemNotFoundException("budget not found"));
        budgetRepository.delete(budget);
    }



    //budget item endpoints
    @Transactional
    public BudgetItem createBudgetItem(BudgetItemDTO payload, int budgetId){
        BudgetItem budgetItem = new BudgetItem();
        modelMapper.map(payload, budgetItem);
        Budget budget = budgetRepository.findByIdAndUser(budgetId, getCurrentUser()).orElseThrow(()->new ItemNotFoundException("budget not found"));
        Category category = categoryRepository.findByIdAndUser(payload.getCategory_id(),getCurrentUser()).orElseThrow(()-> new ItemNotFoundException("category not found"));
        budgetItem.setUser(getCurrentUser());
        budgetItem.setBudget(budget);
        budgetItem.setCategory(category);
        budgetItemRepository.save(budgetItem);
        return budgetItem;
    }


    public List<BudgetItem> getBudgetItemsOfBudget(int budgetId){
        List<BudgetItem> budgetItemList = budgetItemRepository.findByBudgetIdAndUser(budgetId, getCurrentUser());
        return budgetItemList;
    }

    @Transactional
    public BudgetItem updateBudgetItem(BudgetItemDTO payload, int budgetItemId){
        BudgetItem budgetItem = budgetItemRepository.findByIdAndUser(budgetItemId, getCurrentUser());
        modelMapper.map(payload, budgetItem);
        Category category = categoryRepository.findByIdAndUser(payload.getCategory_id(),getCurrentUser()).orElseThrow(()-> new ItemNotFoundException("category not found"));
        budgetItem.setCategory(category);
        budgetItemRepository.save(budgetItem);
        return budgetItem;
    }

    @Transactional
    public void deleteBudgetItem(int budgetItemId){
        BudgetItem budgetItem = budgetItemRepository.findByIdAndUser(budgetItemId, getCurrentUser());
        budgetItemRepository.delete(budgetItem);
    }



    public Budget getBudgetDetails(int id) {
        console.log("user iid is "+getCurrentUser().getId());
        console.log("budget iid is "+id);
        Budget budget = budgetRepository.getBudgetWithItems(id, getCurrentUser().getId());
        if(budget == null)
            throw new ItemNotFoundException("budget not found");
        return budget;
    }

    public BudgetSummaryBoardDTO getMyBudgetSummary() {
        //need the budget of the month
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        Budget budgetOfMonth = budgetQRepository.getBudgetOfMonth(getCurrentUser().getId(),currentMonth);
        var summary = BudgetSummaryBoardDTO.builder();
        //check maxspend
        summary.budget(budgetOfMonth);
        //get all expense of this month
        Double totalSpent = expenseRepository.getTotalExpenseOfMonth(currentMonth, getCurrentUser().getId());
        summary.total_spent(totalSpent);
        // how many categories = how many budget items associated with this budget, so nothing new retireval
        //budget breakdown part
        List<CategoryWiseBudgetSummary> summaryList = budgetQRepository.getBudgetSummaryList(getCurrentUser().getId(),budgetOfMonth.getId(),currentMonth);
        console.log(summaryList);
        summary.category_wise_budget_summary(summaryList);
        //get list of all budget items of this budget
        //for each item get the category
        //for each category find the cost with this category in this month
        return summary.build();
//        return null;
    }
}
