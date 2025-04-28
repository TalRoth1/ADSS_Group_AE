CREATE DATABASE EmployeeDB;

USE EmployeeDB;

CREATE TABLE Employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    salary DECIMAL(10, 2) NOT NULL
);

    MANAGER,
    CASHIER,
    DRIVER,
    SALES,
    CLEANING,
    SECURITY
}

CREATE TABLE Shift (
    id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES Employee(id)
);


