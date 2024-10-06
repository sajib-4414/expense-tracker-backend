CREATE TABLE income (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    amount INT,
    notes VARCHAR(100),
    date_time TIMESTAMP NOT NULL,
    income_source_id INT,
    FOREIGN KEY (user_id) REFERENCES e_user (id),
    FOREIGN KEY (income_source_id) REFERENCES income_source (id)
);

CREATE INDEX user_index ON income (user_id);


