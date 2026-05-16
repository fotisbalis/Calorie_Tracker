drop database if exists calorie_tracker;
create database calorie_tracker;
use calorie_tracker;

create table meal (
	meal_id int auto_increment primary key,
    meal_name varchar(200) not null,
	meal_date date not null,
	calories decimal(10,2) not null,
    fat_gr decimal(10,2) not null,
    carbs_gr decimal(10,2) not null,
    protein_gr decimal(10,2) not null
);

create table saved_meal (
	saved_meal_id int auto_increment primary key,
    meal_name varchar(200) not null unique,
    calories decimal(10,2) not null,
    fat_gr decimal(10,2) not null,
    carbs_gr decimal(10,2) not null,
    protein_gr decimal(10,2) not null
);

create table per_100_food (
	per_100_food_id int auto_increment primary key,
    food_name varchar(200) not null unique,
    calories decimal(10,2) not null,
    fat_gr decimal(10,2) not null,
    carbs_gr decimal(10,2) not null,
    protein_gr decimal(10,2) not null,
    is_favorite boolean not null default false,
    is_manual boolean not null default false
);	

create view daily_totals as select
	meal_date,
    SUM(calories) as calories,
    SUM(fat_gr) as fat,
    SUM(carbs_gr) as carbs,
    SUM(protein_gr) as protein
from meal 
group by meal_date;

create view today_totals as select *
from daily_totals
where meal_date = CURDATE();

delimiter $$

drop procedure if exists new_saved_meal;
$$
create procedure new_saved_meal(
	in p_meal_name varchar(200),
    in p_calories decimal(10,2),
    in p_fat_gr decimal(10,2),
    in p_carbs_gr decimal(10,2),
    in p_protein_gr decimal(10,2)
)
begin
    insert into saved_meal(meal_name, calories, fat_gr, carbs_gr, protein_gr)
    values (p_meal_name, p_calories, p_fat_gr, p_carbs_gr, p_protein_gr);
end $$

drop procedure if exists list_saved_meals; 
$$
create procedure list_saved_meals()
begin
	select *
    from saved_meal
    order by meal_name;
end $$

drop procedure if exists delete_saved_meal;
$$
create procedure delete_saved_meal(
    in p_meal_name varchar(200)
)
begin
    delete from saved_meal
    where meal_name = p_meal_name;
end $$

drop procedure if exists new_per_100_meal;
$$
create procedure new_per_100_meal(
	in p_meal_name varchar(200),
    in p_quantity decimal(10,2),
    in p_calories decimal(10,2),
    in p_fat_gr decimal(10,2),
    in p_carbs_gr decimal(10,2),
    in p_protein_gr decimal(10,2)
)
begin
    insert into per_100_food(food_name, calories, fat_gr, carbs_gr, protein_gr, is_manual)
    values (
    p_meal_name, 
    (p_calories * 100) / p_quantity, 
    (p_fat_gr * 100) / p_quantity, 
    (p_carbs_gr * 100) / p_quantity, 
    (p_protein_gr * 100) / p_quantity,
    true);
end $$

drop procedure if exists list_per_100; 
$$
create procedure list_per_100()
begin
	select *
    from per_100_food
    where is_manual = false
    order by food_name;
end $$

drop procedure if exists list_favorite_per_100; 
$$
create procedure list_favorite_per_100()
begin
	select *
    from per_100_food
    where is_favorite = true and is_manual = false
    order by food_name;
end $$

drop procedure if exists list_manual_per_100; 
$$
create procedure list_manual_per_100()
begin
	select *
    from per_100_food
    where is_manual = true
    order by food_name;
end $$

drop procedure if exists delete_manual_per_100_food;
$$
create procedure delete_manual_per_100_food(
    in p_per_100_food_id int
)
begin
    delete from per_100_food
    where per_100_food_id = p_per_100_food_id
      and is_manual = true;
end $$

drop procedure if exists add_meal_to_today; 
$$
create procedure add_meal_to_today(
	in p_meal_name varchar(200),
    in p_calories decimal(10,2),
    in p_fat_gr decimal(10,2),
    in p_carbs_gr decimal(10,2),
    in p_protein_gr decimal(10,2)
)
begin
    insert into meal(meal_name, meal_date, calories, fat_gr, carbs_gr, protein_gr)
    values (p_meal_name, curdate(), p_calories, p_fat_gr, p_carbs_gr, p_protein_gr);
end $$

drop procedure if exists add_saved_meal_to_today; 
$$
create procedure add_saved_meal_to_today(
    in p_meal_name varchar(200)
)
begin
    insert into meal(meal_name, meal_date, calories, fat_gr, carbs_gr, protein_gr)
    select meal_name, curdate(), calories, fat_gr, carbs_gr, protein_gr
    from saved_meal
    where meal_name = p_meal_name;
end $$

drop procedure if exists add_per_100_food_to_today; 
$$
create procedure add_per_100_food_to_today(
    in p_food_name varchar(200),
    in p_quantity decimal(10,2)
)
begin
    insert into meal(meal_name, meal_date, calories, fat_gr, carbs_gr, protein_gr)
    select 
		food_name, 
		curdate(), 
        (calories * p_quantity) / 100, 
        (fat_gr * p_quantity) / 100, 
        (carbs_gr * p_quantity) / 100, 
        (protein_gr * p_quantity) / 100
    from per_100_food
    where food_name = p_food_name;
end $$

drop procedure if exists show_today_totals; 
$$
create procedure show_today_totals()
begin
    select *
    from today_totals;
end $$

drop procedure if exists delete_today_meal;
$$
create procedure delete_today_meal(
    in p_meal_id int
)
begin
    delete from meal
    where meal_id = p_meal_id;
end $$

delimiter ;
