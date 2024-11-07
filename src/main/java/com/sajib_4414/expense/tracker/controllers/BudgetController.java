package com.sajib_4414.expense.tracker.controllers;

import com.sajib_4414.expense.tracker.config.console;
import com.sajib_4414.expense.tracker.models.budget.Budget;
import com.sajib_4414.expense.tracker.models.budget.BudgetItem;
import com.sajib_4414.expense.tracker.payload.BudgetDTO;
import com.sajib_4414.expense.tracker.payload.BudgetItemDTO;
import com.sajib_4414.expense.tracker.payload.BudgetSummaryBoardDTO;
import com.sajib_4414.expense.tracker.services.BudgetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    // Create budget
    @PostMapping
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody BudgetDTO payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(payload));
        // Implementation for creating a budget

    }

    @GetMapping
    public ResponseEntity<Page<Budget>> getMyBudgets() {
        // Implementation for retrieving budget details
        return ResponseEntity.ok().body(budgetService.getMyBudgets());
    }

    // Create budget item
    @PostMapping("/{budgetId}/items")
    public ResponseEntity<BudgetItem> createBudgetItem(@Valid @RequestBody BudgetItemDTO payload, @Valid @PathVariable int budgetId) {
        // Implementation for creating a budget item

        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudgetItem(payload, budgetId));
    }

    // Get budget details by id
    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudgetDetails(@PathVariable int id) {
        // Implementation for retrieving budget details
        return ResponseEntity.ok().body(budgetService.getBudgetDetails(id));
    }



    // Delete budget by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBudget(@PathVariable int id) {
        // Implementation for deleting a budget
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    // Update budget by id
    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable int id, @Valid @RequestBody BudgetDTO payload) {
        // Implementation for updating a budget
        console.log("inside updating.....");
        return ResponseEntity.ok().body(budgetService.updateBudget(payload,id));
    }

    // Update budgetitem by id
    @PutMapping("/budget-items/{id}")
    public ResponseEntity<BudgetItem> updateBudgetItem(@PathVariable int id, @Valid @RequestBody BudgetItemDTO payload) {
        // Implementation for updating a budgetitem

        return ResponseEntity.ok().body(budgetService.updateBudgetItem(payload,id));
    }

    @DeleteMapping("/budget-items/{id}")
    public ResponseEntity<Object> deleteBudgetItem(@PathVariable int id) {

        budgetService.deleteBudgetItem(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/summary")
    public ResponseEntity<BudgetSummaryBoardDTO> getBudgetBoardSummary() {
        ;
        return ResponseEntity.ok().body(budgetService.getMyBudgetSummary());
    }
}
