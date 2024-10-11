package com.sajib_4414.expense.tracker.controllers;

import com.sajib_4414.expense.tracker.models.expense.Expense;
import com.sajib_4414.expense.tracker.payload.CategoryExpense;
import com.sajib_4414.expense.tracker.payload.ExpenseDTO;
import com.sajib_4414.expense.tracker.payload.PagedResponse;
import com.sajib_4414.expense.tracker.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<Expense>> getAllMyExpenses(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size){

        return ResponseEntity.ok().body(expenseService.getCurrentUserExpense(page,size));
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

    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense> updateCategory(@Valid @RequestBody ExpenseDTO payload, @Valid @PathVariable int expenseId){
        return ResponseEntity.ok().body(expenseService.update(payload, expenseId));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(@Valid @PathVariable int expenseId){
        expenseService.delete(expenseId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/summary")
    public ResponseEntity<List<CategoryExpense>> getExpenseSummaryByMonth(){
        return ResponseEntity.ok().body(expenseService.getExpenseSummaryView());
    }
}
