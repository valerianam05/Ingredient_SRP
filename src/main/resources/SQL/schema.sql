CREATE TYPE category AS ENUM ('VEGETABLE',
    'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');

CREATE TYPE dish_type AS ENUM ('START',  'MAIN', 'DESSERT');

CREATE TABLE Ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            price numeric,
                            category category


);

CREATE TABLE Dish(
                     id SERIAL PRIMARY KEY,
                     name varchar(100),
                     dish_type dish_type
);