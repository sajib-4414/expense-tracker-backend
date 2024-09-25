--DO $$
--BEGIN
--    -- Create index if it does not exist
--    IF NOT EXISTS (
--        SELECT 1
--        FROM pg_indexes
--        WHERE tablename = 'e_expense' AND indexname = 'expense_owner_index'
--    ) THEN
--        EXECUTE 'CREATE INDEX expense_owner_index ON e_expense(owner_id)';
--    END IF;
--
--    -- Rename column if it exists and is named 'owner'
--    IF EXISTS (
--            SELECT 1
--            FROM information_schema.columns
--            WHERE table_name = 'e_expense' AND column_name = 'owner'
--        ) THEN
--            EXECUTE 'ALTER TABLE e_expense RENAME COLUMN owner TO owner_id';
--        END IF;
--
--    -- Add column if it does not exist
--    IF NOT EXISTS (
--        SELECT 1
--        FROM information_schema.columns
--        WHERE table_name = 'e_expense' AND column_name = 'category_id'
--    ) THEN
--        EXECUTE 'ALTER TABLE e_expense ADD COLUMN category_id INTEGER NOT NULL';
--    END IF;
--
--    -- Add foreign key constraint if it does not exist
--    IF NOT EXISTS (
--        SELECT 1
--        FROM information_schema.table_constraints
--        WHERE table_name = 'e_expense' AND constraint_name = 'fk_expense_category'
--    ) THEN
--        EXECUTE 'ALTER TABLE e_expense ADD CONSTRAINT fk_expense_category FOREIGN KEY (category_id) REFERENCES e_categories(id)';
--    END IF;
--
--END $$;