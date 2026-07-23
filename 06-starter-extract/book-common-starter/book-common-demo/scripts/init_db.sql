CREATE DATABASE IF NOT EXISTS testdb;
USE testdb;
CREATE TABLE book (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    author VARCHAR(50),
    price DECIMAL(10,2)
);
INSERT INTO book (name, author, price) VALUES ('Spring实战', 'Craig Walls', 69.0);