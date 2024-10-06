package com.sajib_4414.expense.tracker.controllers;

import com.sajib_4414.expense.tracker.models.expense.Expense;
import com.sajib_4414.expense.tracker.models.income.Income;
import com.sajib_4414.expense.tracker.payload.IncomeDTO;
import com.sajib_4414.expense.tracker.services.IncomeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/income")
@AllArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @GetMapping
    public ResponseEntity<List<Income>> getMyIncomes(){

        return ResponseEntity.ok().body(incomeService.getMyIncomes());
    }

    @PostMapping("")
    public ResponseEntity<Income> createIncome(@Valid @RequestBody IncomeDTO payload){

        return ResponseEntity.status(HttpStatus.CREATED).body(incomeService.create(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Income> updateIncome(@Valid @RequestBody IncomeDTO payload, @Valid @PathVariable int id){

        return ResponseEntity.ok().body(incomeService.update(payload,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable int id){
        incomeService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
