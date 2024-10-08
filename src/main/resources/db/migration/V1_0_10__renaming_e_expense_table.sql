ALTER table e_expense RENAME TO expenses;
ALTER TABLE expenses RENAME COLUMN owner_id to user_id;