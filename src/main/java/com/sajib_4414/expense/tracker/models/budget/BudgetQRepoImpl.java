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
                       .and(budget.budgetPeriod.month().eq(month))
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
                " bi.id as budget_item_id,bi.maxSpend,bi.warningSpend, " +
                "bi.category.id,ec.name as category_name, coalesce(total,0) as total_expense ) " +
                "from BudgetItem bi " +
                "left join " +
                " (select sum(ex.cost) as total, ex.category.id as expense_category_id from " +
                " Expense ex" +
                " where ex.user.id=:userId and extract(month from ex.dateTime)=:month\n" +
                " group by ex.category.id) as total_expense_table " +

                "on bi.category.id=expense_category_id " +
                "left join Category ec " +
                "on bi.category.id= ec.id " +
                "where bi.budget.id=:budgetId  ", CategoryWiseBudgetSummary.class);
        query.setParameter("userId",userId);
        query.setParameter("month",month);
        query.setParameter("budgetId",budgetId);
        return query.getResultList();
    }

    @Override
    public List<CategoryWiseBudgetSummary> getBudgetSummaryListWithNoBudgetItem(Integer userId, Integer budgetId, int month) {
        Query query = entityManager.createQuery("SELECT new com.sajib_4414.expense.tracker.payload.CategoryWiseBudgetSummary(" +
                " 0 as budget_item_id, 0 as max_spend, 0 as warning_spend, ex.category.id, cc.name as category_name, sum(ex.cost) as total_expense,true ) " +
                "from Expense ex inner join Category cc on cc.id=ex.category.id " +
                "where ex.category.id " +
                "not in ( " +
                "select category.id from BudgetItem bi " +
                "where bi.user.id=:userId and bi.budget.id=:budgetId " +
                ") " +
                "and extract(month from ex.dateTime)=:month " +
                "group by ex.category.id,cc.name"+
                " ", CategoryWiseBudgetSummary.class);
        query.setParameter("budgetId",budgetId);
        query.setParameter("userId",userId);
        query.setParameter("month",month);

        return query.getResultList();
    }


    @Override
    public List<CategoryWiseBudgetSummary> getBudgetSummaryExpenseListNoCategory(Integer userId, int month) {
        Query query = entityManager.createQuery("SELECT new com.sajib_4414.expense.tracker.payload.CategoryWiseBudgetSummary(" +
                " 0 as budget_item_id, 0 as max_spend, 0 as warning_spend, 0 as category_id, 'Uncategorized' as category_name, sum(ex.cost) as total_expense,true ) " +
                "from Expense ex " +
                "where ex.category.id is NULL " +
                "and extract(month from ex.dateTime)=:month and ex.user.id=:userId " +
                "group by ex.category.id"+
                " ", CategoryWiseBudgetSummary.class);
        query.setParameter("userId",userId);
        query.setParameter("month",month);

        return query.getResultList();
    }


}
