ALTER table e_categories RENAME TO expense_categories;
ALTER TABLE expense_categories RENAME COLUMN created_by to user_id;