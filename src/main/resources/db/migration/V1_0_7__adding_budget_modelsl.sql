CREATE TABLE budget (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    estimated_income int,
    max_spend int,
    warning_spend int,
    FOREIGN KEY (user_id) REFERENCES e_user (id)
);

CREATE INDEX user_budget_index ON budget (user_id);
CREATE INDEX user_budget_start_date_index ON budget (start_date);
CREATE INDEX user_budget_end_date_index ON budget (end_date);


CREATE TABLE budget_item (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    category_id int,
    budget_id int not null,
    max_spend int,
    warning_spend int,
    FOREIGN KEY (budget_id) REFERENCES budget (id),
    FOREIGN KEY (user_id) REFERENCES e_user (id),
    FOREIGN KEY (category_id) REFERENCES e_categories (id)
);

CREATE INDEX user_budget_item_index ON budget_item (user_id);