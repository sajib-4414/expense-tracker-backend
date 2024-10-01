create table income_source(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    created_by int,
    foreign key(created_by) references e_user(id)
);

create index created_by_index on income_source(created_by);

