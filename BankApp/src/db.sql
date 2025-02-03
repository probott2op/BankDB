-- Create the bank database
CREATE DATABASE BankDB;
USE BankDB;

-- Table to store customer information
CREATE TABLE customers (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(15),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table to store account information
CREATE TABLE accounts (
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    balance DECIMAL(15, 2) DEFAULT 0.00,
    opened_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Table to store transaction information
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_date TIMESTAMP,
    description VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);

-- Table to store loan information
CREATE TABLE loans (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    loan_type VARCHAR(100),
    loan_amount DECIMAL(15, 2) NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL,
    loan_term INT NOT NULL, -- Loan term in months
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Table to store Login Information
CREATE TABLE login (
    login_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    pass VARCHAR(255) NOT NULL, 
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);


INSERT INTO customers (first_name, last_name, email, phone_number, address, city, state, zip_code)
VALUES
('Jaiwant', 'Tummala', 'jaiwant.tummala@example.com', '9704012349', '123 maddison square', 'DELHI', 'DL', '100001'),
('Nishant', 'Dahiya', 'nishant.dahiya@example.com', '9703012349', '456 brent towers', 'DElHI', 'DL', '900801'),
('Kevin', 'Perreira', 'kevin.perreira@example.com', '9704013349', '789 Larry downs', 'DELHI', 'DL', '607601');

select * from customers;

INSERT INTO login (customer_id, username, pass)
VALUES
(1, 'jaiwant_t', 'j123'),
(2, 'nishant_d', 'n123'),
(3, 'kevin_p', 'k123');

select * from accounts;
