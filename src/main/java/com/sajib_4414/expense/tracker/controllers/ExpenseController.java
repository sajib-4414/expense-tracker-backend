package com.sajib_4414.expense.tracker.controllers;

import com.sajib_4414.expense.tracker.models.expense.Expense;
import com.sajib_4414.expense.tracker.payload.ExpenseDTO;
import com.sajib_4414.expense.tracker.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private ExpenseService expenseService;

    @GetMapping("")
    public ResponseEntity<List<Expense>> getAllMyExpenses(){

        return ResponseEntity.ok().body(expenseService.getCurrentUserExpense());
    }

    @PostMapping("")
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody ExpenseDTO payload){
        Expense createdExpense = expenseService.createExpense(payload);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdExpense.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdExpense);
    }

    @PutMapping("/updatecost/{expenseId}")
    public ResponseEntity<Expense> updateCost(@Valid @RequestBody ExpenseDTO payload, @Valid @PathVariable int expenseId){

        return ResponseEntity.ok().body(expenseService.updateCost(payload, expenseId));
    }

    @PutMapping("/updatecategory/{expenseId}")
    public ResponseEntity<Expense> updateCategory(@Valid @RequestBody ExpenseDTO payload, @Valid @PathVariable int expenseId){
        return ResponseEntity.ok().body(expenseService.updateCategory(payload, expenseId));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(@Valid @PathVariable int expenseId){
        expenseService.delete(expenseId);
        return ResponseEntity.noContent().build();
    }

}
