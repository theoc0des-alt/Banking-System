CREATE TABLE IF NOT EXISTS staff (
    staff_id VARCHAR(20) PRIMARY KEY,
    staff_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS customers (
    customer_id VARCHAR(20) PRIMARY KEY,
    customer_type VARCHAR(12) NOT NULL,
    address VARCHAR(200),
    phone VARCHAR(30),
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    first_name VARCHAR(60),
    surname VARCHAR(60),
    id_number VARCHAR(30),
    employer_name VARCHAR(100),
    employer_address VARCHAR(200),
    company_name VARCHAR(100),
    registration_number VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    customer_id VARCHAR(20) NOT NULL,
    account_type VARCHAR(12) NOT NULL,
    branch VARCHAR(60),
    balance DECIMAL(15,2) NOT NULL,
    date_opened DATE NOT NULL,
    status VARCHAR(12) NOT NULL,
    lock_period_months INT,
    overdraft_limit DECIMAL(15,2),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id VARCHAR(20) PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(12) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    txn_date DATE NOT NULL,
    txn_time TIME NOT NULL,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);