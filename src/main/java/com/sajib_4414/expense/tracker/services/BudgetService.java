package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.config.console;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.BadDataException;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.SystemException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        console.log(payload);



        console.log("start date's month is ",payload.getBudget_period().getMonth() +1);
        console.log("start date's month is ",payload.getBudget_period().getYear() +1900);
        //check if budget exists already
        boolean ifBudgetExistForMonth = budgetRepository.ifBudgetExistForUserAndMonth(getCurrentUser().getId(), payload.getBudget_period().getMonth()+1, payload.getBudget_period().getYear()+1900);
        if(ifBudgetExistForMonth)
            throw new BadDataException("Budget already exists for the month");


        Budget newBudget = new Budget();
        modelMapper.map(payload,newBudget );
        newBudget.setUser(getCurrentUser());
        newBudget.setBudgetPeriod(payload.getBudget_period());
        budgetRepository.save(newBudget);
        newBudget.setBudgetItemList(new ArrayList<>());
        return newBudget;
    }

    public Page<Budget> getMyBudgets(){
        return budgetRepository.findByUser(PageRequest.of(0, 5), getCurrentUser());
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
        //also check a budget item with this category is already there
        Category category = categoryRepository.findByIdAndUser(payload.getCategory_id(),getCurrentUser()).orElseThrow(()-> new ItemNotFoundException("category not found"));
        BudgetItem existingBudgetItem = budgetItemRepository.findByCategoryAndBudgetAndUser(category, budget, getCurrentUser());
        if(existingBudgetItem != null)
            throw new BadDataException("Budget item with this category already exists");
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

        //this gets all budget items and their associated cost.
        List<CategoryWiseBudgetSummary> summaryList = budgetQRepository.getBudgetSummaryList(getCurrentUser().getId(),budgetOfMonth.getId(),currentMonth);
        //we also need to get expenses that are in other categories for which budget is not yet defined
        List<CategoryWiseBudgetSummary> summaryListWithNobudgetItem =  budgetQRepository.getBudgetSummaryListWithNoBudgetItem(getCurrentUser().getId(),budgetOfMonth.getId(),  currentMonth);
        //also need to get expenses where category id is null
        List<CategoryWiseBudgetSummary> summaryListExpenseUnCategorized =  budgetQRepository.getBudgetSummaryExpenseListNoCategory(getCurrentUser().getId(), currentMonth);
        List<CategoryWiseBudgetSummary> totalExpenseSummary = Stream.concat(Stream.concat(summaryList.stream(), summaryListWithNobudgetItem.stream()),summaryListExpenseUnCategorized.stream()).toList();
        summary.category_wise_budget_summary(totalExpenseSummary);

        return summary.build();
//        return null;
    }
}
