package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.PermissionError;
import com.sajib_4414.expense.tracker.models.income.IncomeSource;
import com.sajib_4414.expense.tracker.models.income.IncomeSourceRepository;
import com.sajib_4414.expense.tracker.models.user.User;
import com.sajib_4414.expense.tracker.payload.IncomeSourceDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sajib_4414.expense.tracker.config.Helper.getCurrentUser;

@Service
@AllArgsConstructor
public class IncomeSourceService {
    private IncomeSourceRepository incomeSourceRepository;

    public List<IncomeSource> getIncomeSources(){
        return incomeSourceRepository.findByUserIsNullOrUser(getCurrentUser());
    }

    @Transactional
    public IncomeSource create(IncomeSourceDTO payload) {
        IncomeSource incomeSource = IncomeSource.builder().name(payload.getName()).user(getCurrentUser()).build();
        incomeSourceRepository.save(incomeSource);
        return incomeSource;
    }

    @Transactional
    public IncomeSource update(IncomeSourceDTO payload, int incomeSourceId) {
        checkOwnershipAndThrow(incomeSourceId);
        IncomeSource incomeSource = incomeSourceRepository.findById(incomeSourceId).orElseThrow(()->new ItemNotFoundException("invalid income source id"));
        incomeSource.setName(payload.getName());
        incomeSourceRepository.save(incomeSource);
        return incomeSource;
    }

    public void checkOwnershipAndThrow(int incomeSourceId){
        Boolean isOwner = incomeSourceRepository.isUserObjectOwner(getCurrentUser().getUsername(), incomeSourceId);
        if (!isOwner)
            throw new PermissionError("You are not autorized to modify this category");
    }



    @Transactional
    public void delete(@Valid int incomeSourceId) {
        checkOwnershipAndThrow(incomeSourceId);
        incomeSourceRepository.deleteById(incomeSourceId);
    }
}
