drop database if exists calorie_tracker;
create database calorie_tracker;
use calorie_tracker;

create table meal (
	meal_id int auto_increment primary key,
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