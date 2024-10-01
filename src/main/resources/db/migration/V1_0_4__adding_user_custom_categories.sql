alter table e_categories
add column created_by int;

alter table e_categories
add constraint fk_categories_user foreign key(created_by) references e_user(id);
