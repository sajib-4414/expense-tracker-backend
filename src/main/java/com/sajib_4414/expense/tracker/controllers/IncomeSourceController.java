package com.sajib_4414.expense.tracker.controllers;

import com.sajib_4414.expense.tracker.models.income.IncomeSource;
import com.sajib_4414.expense.tracker.payload.IncomeSourceDTO;
import com.sajib_4414.expense.tracker.services.IncomeSourceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/income/income-source")
public class IncomeSourceController {

    private final IncomeSourceService incomeSourceService;

    @GetMapping
    public ResponseEntity<List<IncomeSource>> getIncomeSources(){
        return ResponseEntity.ok().body(incomeSourceService.getIncomeSources());
    }

    @PostMapping
    public ResponseEntity<IncomeSource> createIncomeSource(@RequestBody @Valid IncomeSourceDTO payload){
        return ResponseEntity.status(HttpStatus.CREATED).body(incomeSourceService.create(payload));
    }

    @PutMapping("/{incomeSourceId}")
    public ResponseEntity<IncomeSource> updateIncomeSource(@RequestBody @Valid IncomeSourceDTO payload, @PathVariable @Valid int incomeSourceId)
    {
        return ResponseEntity.ok().body(incomeSourceService.update(payload,incomeSourceId));
    }

    @DeleteMapping("/{incomeSourceId}")
    public ResponseEntity<IncomeSource> deleteIncomeResource(@PathVariable @Valid int incomeSourceId)
    {
        incomeSourceService.delete(incomeSourceId);
        return ResponseEntity.noContent().build();
    }
}
