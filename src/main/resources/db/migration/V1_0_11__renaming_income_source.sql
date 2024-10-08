ALTER table income_source RENAME TO income_sources;
ALTER TABLE income_sources RENAME COLUMN created_by to user_id;