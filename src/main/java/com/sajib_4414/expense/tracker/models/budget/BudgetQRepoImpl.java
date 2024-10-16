package com.sajib_4414.expense.tracker.models.budget;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sajib_4414.expense.tracker.config.console;
import com.sajib_4414.expense.tracker.models.category.QCategory;
import com.sajib_4414.expense.tracker.models.expense.QExpense;
import com.sajib_4414.expense.tracker.payload.CategoryExpense;
import com.sajib_4414.expense.tracker.payload.CategoryWiseBudgetSummary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Repository
@AllArgsConstructor
public class BudgetQRepoImpl implements BudgetQRepository{


    private JPAQueryFactory queryFactory;
    private EntityManager entityManager;
    @Override
    public Budget getBudgetOfMonth(Integer user_id, int month) {
        QBudget budget = QBudget.budget; //it is meant to import the generated q type
        return queryFactory.selectFrom(budget)
                .where(
                        budget.user.id.eq(user_id)
                       .and(budget.startDate.month().eq(month))
                ).fetchOne();

    }

    @Override
    public List<CategoryWiseBudgetSummary> getBudgetSummaryList(Integer userId, int budgetId, int month) {
        /*
select
case when
budget_items.id is NULL then 0
else budget_items.id
end as budget_item_id, budget_items.max_spend,budget_items.warning_spend,
coalesce (sum(expense_cost),0) as total_expense,
case
when expense_category_id is NULL then 0
else expense_category_id
end

, case
when expense_categtory_name is NULL then 'Undefined'
else expense_categtory_name
end
from budget_items
full join

(select expenses.cost as expense_cost, expense_categories.id as expense_category_id, expense_categories.name as expense_categtory_name
from expenses
left join expense_categories
on expenses.category_id=expense_categories.id
where expenses.user_id=50
and extract(month from expenses.date_time)=10
) as expense_combined

on
budget_items.category_id=expense_category_id
where (budget_items.budget_id=2 or budget_items.budget_id is NULL)
group by budget_items.id,expense_category_id,expense_categtory_name
         */
        Query query = entityManager.createQuery("SELECT new com.sajib_4414.expense.tracker.payload.CategoryWiseBudgetSummary(" +
                "case when bi.id is NULL then 0 ELSE bi.id end " +
                ", case when bi.maxSpend is NULL then 0 else bi.maxSpend end" +
                ", case when bi.warningSpend is NULL then 0 else bi.warningSpend end, " +
                "COALESCE(SUM(ec.expenseCost), 0)," +
                " CASE WHEN ec.expenseCategoryId IS NULL THEN 0 ELSE ec.expenseCategoryId END, " +
                "CASE WHEN ec.expenseCategoryName IS NULL THEN 'Undefined' ELSE ec.expenseCategoryName END  ) " +
                "from BudgetItem bi " +
                "FULL OUTER JOIN (SELECT ex.cost AS expenseCost, ct.id AS expenseCategoryId, ct.name AS expenseCategoryName " +
                "FROM Expense ex FULL OUTER JOIN Category ct ON ex.category.id = ct.id " +
                "WHERE " +
                "(ex.user.id = :userId or ex.user.id is NULL)" +
                "and " +
                "( EXTRACT(month from ex.dateTime) = :month OR ex.dateTime is NULL)" +
                "and " +
                "( ct.user.id= :userId or ct.user.id is NULL)" +
                ") ec " +
                "ON bi.category.id = ec.expenseCategoryId " +
                "WHERE (bi.budget.id = :budgetId OR bi.budget.id IS NULL) " +
                "and (bi.maxSpend is not null or ec.expenseCategoryName is null) " +
                "GROUP BY bi.id, ec.expenseCategoryId, ec.expenseCategoryName", CategoryWiseBudgetSummary.class);
        query.setParameter("userId",userId);
        query.setParameter("month",month);
        query.setParameter("budgetId",budgetId);
        return query.getResultList();
    }




}
