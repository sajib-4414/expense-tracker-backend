alter table budgets drop column end_date;
ALTER TABLE budgets RENAME COLUMN start_date to budget_month_year;