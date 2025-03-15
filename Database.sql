CREATE DATABASE cafe; 
USE cafe;

CREATE TABLE employee (
    E_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    security_question VARCHAR(255) NOT NULL,
    security_answer VARCHAR(255) NOT NULL
);

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prod_id INT NOT NULL,
    prod_name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(255) NOT NULL,
    image VARCHAR(500) NOT NULL,
    date DATE NOT NULL
);

-- Rename column in employee table
ALTER TABLE employee RENAME COLUMN security_question TO question;

-- Select all records from employee table
SELECT * FROM employee;