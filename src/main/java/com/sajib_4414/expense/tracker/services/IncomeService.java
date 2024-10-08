package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.config.console;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.PermissionError;
import com.sajib_4414.expense.tracker.models.category.Category;
import com.sajib_4414.expense.tracker.models.expense.Expense;
import com.sajib_4414.expense.tracker.models.expense.ExpenseRepository;
import com.sajib_4414.expense.tracker.models.income.Income;
import com.sajib_4414.expense.tracker.models.income.IncomeRepository;
import com.sajib_4414.expense.tracker.models.income.IncomeSource;
import com.sajib_4414.expense.tracker.models.income.IncomeSourceRepository;
import com.sajib_4414.expense.tracker.payload.CategoryExpense;
import com.sajib_4414.expense.tracker.payload.IncomeDTO;
import com.sajib_4414.expense.tracker.payload.OverViewDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.sajib_4414.expense.tracker.config.Helper.getCurrentUser;

@AllArgsConstructor
@Service
public class IncomeService {


    private final ModelMapper modelMapper;
    private IncomeRepository incomeRepository;
    private IncomeSourceRepository incomeSourceRepository;
    private ExpenseRepository expenseRepository;

    public List<Income> getMyIncomes() {
        List<Income> incomes = incomeRepository.findByUser(getCurrentUser());
        return incomes;
    }

    public Income create(IncomeDTO incomeDTO){
        Income dbIncome = new Income();
        //use model mapper
        modelMapper.map(incomeDTO, dbIncome);


        //then do other properties
        //datetime, income source id, user
        IncomeSource incomeSource = incomeSourceRepository.findIncomeSourceWithUserAndId(getCurrentUser().getId(),incomeDTO.getIncome_source_id());
        if(incomeSource == null)
            throw new ItemNotFoundException("income source not with user found");

        dbIncome.setIncomeSource(incomeSource);
        dbIncome.setUser(getCurrentUser());


        incomeRepository.save(dbIncome);
        return dbIncome;

    }

    @Transactional
    public Income update(IncomeDTO incomeDTO,int id){
        //check owner
        Income dbIncome = getValidatedIncome(id);

        //use model mapper
        modelMapper.map(incomeDTO, dbIncome);

        //incomesource, it also checks if current user own the income source
        IncomeSource incomeSource = incomeSourceRepository.findIncomeSourceWithUserAndId(getCurrentUser().getId(),incomeDTO.getIncome_source_id());
        if(incomeSource == null)
            throw new ItemNotFoundException("income source not found");
        dbIncome.setIncomeSource(incomeSource);
        incomeRepository.save(dbIncome);
        return dbIncome;
    }

    @Transactional
    public void delete(int id){
        //check owner
        Income validatedIncome = getValidatedIncome(id);
        //delete by id
        incomeRepository.delete(validatedIncome);
    }

    //it also makes sure user is the owner
    public Income getValidatedIncome(int incomeId){
        Income income = incomeRepository.findIncomeWithUserAndId(getCurrentUser().getId(),incomeId);
        if(income == null)
            throw new ItemNotFoundException("income with this user not found");
        return income;
    }

    public OverViewDTO getOverViewData() {
        //get the sum(income) reported this month
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        Double totalIncomeThisMonth = incomeRepository.getTotalIncomeOfMonth(getCurrentUser().getId(),currentMonth).orElse(0.0);
        //get the total expense reported this month
        Double totalExpenseThisMonth = expenseRepository.getTotalExpenseOfMonth(currentMonth,getCurrentUser().getId());
        Double netBalance = totalIncomeThisMonth - totalExpenseThisMonth;

        List<CategoryExpense> top5 = expenseRepository.getTop5CategoryExpenseOfMonth(currentMonth, getCurrentUser().getId());

        return OverViewDTO.builder()
                .topCategoryExpense(top5)
                .netBalance(netBalance)
                .totalExpense(totalExpenseThisMonth)
                .totalIncome(totalIncomeThisMonth)
                .build();

    }
}
