alter table budgets
alter column budget_month_year TYPE DATE
USING budget_month_year::DATE,
ALTER COLUMN budget_month_year SET NOT NULL;