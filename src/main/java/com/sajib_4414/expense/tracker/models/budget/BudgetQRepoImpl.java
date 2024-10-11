package com.sajib_4414.expense.tracker.models.budget;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
@AllArgsConstructor
public class BudgetQRepoImpl implements BudgetQRepository{


    private JPAQueryFactory queryFactory;
    @Override
    public Budget getBudgetOfMonth(Integer user_id, int month) {
        QBudget budget = QBudget.budget; //it is meant to import the generated q type
        return queryFactory.selectFrom(budget)
                .where(
                        budget.user.id.eq(user_id)
                       .and(budget.startDate.month().eq(month))
                ).fetchOne();

    }
}
