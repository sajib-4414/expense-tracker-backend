package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.config.console;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.models.budget.Budget;
import com.sajib_4414.expense.tracker.models.budget.BudgetItem;
import com.sajib_4414.expense.tracker.models.budget.BudgetItemRepository;
import com.sajib_4414.expense.tracker.models.budget.BudgetRepository;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.category.CategoryRepository;
import com.sajib_4414.expense.tracker.payload.BudgetDTO;
import com.sajib_4414.expense.tracker.payload.BudgetItemDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sajib_4414.expense.tracker.config.Helper.getCurrentUser;

@Service
@AllArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetItemRepository budgetItemRepository;
    private final CategoryRepository categoryRepository;
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
}
