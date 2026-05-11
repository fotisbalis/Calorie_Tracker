drop database if exists calorie_tracker;
create database calorie_tracker;
use calorie_tracker;

create table meal (
	meal_id int auto_increment primary key,
    meal_name varchar(200) not null,
	meal_date date not null,
	calories int not null,
    fat_gr int not null,
    carbs_gr int not null,
    protein_gr int not null
);

create table saved_meal (
	saved_meal_id int auto_increment primary key,
    meal_name varchar(200) not null unique,
    calories int not null,
    fat_gr int not null,
    carbs_gr int not null,
    protein_gr int not null
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
create procedure new_saved_meal(
	in p_meal_name varchar(200),
    in p_calories int,
    in p_fat_gr int,
    in p_carbs_gr int,
    in p_protein_gr int
)
begin
    insert into saved_meal(meal_name, calories, fat_gr, carbs_gr, protein_gr)
    values (p_meal_name, p_calories, p_fat_gr, p_carbs_gr, p_protein_gr);
end $$

drop procedure if exists list_saved_meals;
create procedure list_saved_meals()
begin
	select *
    from saved_meal
    order by meal_name;
end $$

drop procedure if exists add_meal_to_today;
create procedure add_meal_to_today(
	in p_meal_name varchar(200),
    in p_calories int,
    in p_fat_gr int,
    in p_carbs_gr int,
    in p_protein_gr int
)
begin
    insert into meal(meal_name, meal_date, calories, fat_gr, carbs_gr, protein_gr)
    values (p_meal_name, curdate(), p_calories, p_fat_gr, p_carbs_gr, p_protein_gr);
end $$

drop procedure if exists add_saved_meal_to_today;
create procedure add_saved_meal_to_today(
    in p_meal_name varchar(200)
)
begin
    insert into meal(meal_name, meal_date, calories, fat_gr, carbs_gr, protein_gr)
    select meal_name, curdate(), calories, fat_gr, carbs_gr, protein_gr
    from saved_meal
    where meal_name = p_meal_name;
end $$

create procedure show_today_totals()
begin
    select *
    from today_totals;
end $$

delimiter ;