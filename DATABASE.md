# Restaurant Billing System - Database Setup Guide

## 🗄️ Database Schema & Queries

This document contains all the necessary database queries to set up and populate the restaurant billing system database.

## 📋 Database Tables

### 1. Menu Items Table
Stores all menu items with their details.

```sql
-- Create menu_items table
CREATE TABLE menu_item (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample menu items
INSERT INTO menu_item (name, category, price, available) VALUES
('Burger', 'Fast Food', 150.00, true),
('Pizza', 'Fast Food', 250.00, true),
('Pasta', 'Italian', 200.00, true),
('Sandwich', 'Fast Food', 120.00, true),
('French Fries', 'Snacks', 80.00, true),
('Cold Coffee', 'Beverages', 60.00, true),
('Ice Cream', 'Desserts', 90.00, true),
('Salad', 'Healthy', 110.00, true),
('Soup', 'Starters', 85.00, true),
('Noodles', 'Chinese', 180.00, true),
('Spring Roll', 'Chinese', 130.00, true),
('Manchurian', 'Chinese', 160.00, true),
('Biryani', 'Indian', 220.00, true),
('Butter Chicken', 'Indian', 280.00, true),
('Naan', 'Indian', 40.00, true),
('Dal Makhani', 'Indian', 180.00, true),
('Samosa', 'Snacks', 50.00, true),
('Pakora', 'Snacks', 70.00, true),
('Lassi', 'Beverages', 45.00, true),
('Tea', 'Beverages', 25.00, true);
```

### 2. Orders Table
Stores order information and customer details.

```sql
-- Create orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(20) DEFAULT 'created',
    total_amount DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    customer_email VARCHAR(255),
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    payment_method VARCHAR(20),
    customer_name VARCHAR(255),
    customer_phone VARCHAR(20)
);

-- Create index for better performance
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_customer_email ON orders(customer_email);
```

### 3. Order Items Table
Stores items included in each order.

```sql
-- Create order_items table
CREATE TABLE order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Create indexes for better performance
CREATE INDEX idx_order_items_order_id ON order_item(order_id);
CREATE INDEX idx_order_items_menu_item_id ON order_item(menu_item_id);
```

## 🔧 Database Setup Commands

### PostgreSQL Setup

```sql
-- Create database (run as postgres user)
CREATE DATABASE restaurant_db;

-- Connect to the database
\c restaurant_db;

-- Create all tables (run the above CREATE TABLE statements)

-- Verify tables were created
\dt

-- Check table structures
\d menu_items
\d orders
\d order_items
```

### MySQL Setup (Alternative)

```sql
-- Create database
CREATE DATABASE restaurant_db;

-- Use the database
USE restaurant_db;

-- Create menu_items table
CREATE TABLE menu_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(20) DEFAULT 'created',
    total_amount DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    customer_email VARCHAR(255),
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    payment_method VARCHAR(20),
    customer_name VARCHAR(255),
    customer_phone VARCHAR(20)
);

-- Create order_items table
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Insert sample data (same as above)
```

## 📊 Sample Data for Testing

### Sample Orders with Items

```sql
-- Sample Order 1
INSERT INTO orders(customer_name, customer_email, customer_phone, status, total_amount, payment_status) 
VALUES ('John Doe', 'john@example.com', '9876543210', 'PAID', 570.00, 'PAID');

-- Get the order ID (assuming it's 1)
INSERT INTO order_item(order_id, menu_item_id, quantity, price) VALUES
(1, 1, 2, 150.00), -- 2 Burgers
(1, 2, 1, 250.00), -- 1 Pizza
(1, 6, 1, 70.00);  -- 1 Cold Coffee

-- Sample Order 2
INSERT INTO orders (customer_name, customer_email, customer_phone, status, total_amount, payment_status) 
VALUES ('Jane Smith', 'jane@example.com', '9876543211', 'created', 440.00, 'PENDING');

-- Get the order ID (assuming it's 2)
INSERT INTO order_item(order_id, menu_item_id, quantity, price) VALUES
(2, 3, 1, 200.00), -- 1 Pasta
(2, 4, 2, 120.00); -- 2 Sandwiches

-- Sample Order 3
INSERT INTO orders (customer_name, customer_email, customer_phone, status, total_amount, payment_status) 
VALUES ('Bob Wilson', 'bob@example.com', '9876543212', 'PAID', 760.00, 'PAID');

-- Get the order ID (assuming it's 3)
INSERT INTO order_item(order_id, menu_item_id, quantity, price) VALUES
(3, 13, 1, 220.00), -- 1 Biryani
(3, 14, 1, 280.00), -- 1 Butter Chicken
(3, 15, 2, 40.00),  -- 2 Naan
(3, 18, 1, 180.00); -- 1 Dal Makhani
```

## 🔍 Useful Queries for Dashboard

### Get Dashboard Statistics

```sql
-- Total Orders Count
SELECT COUNT(*) as total_orders FROM orders;

-- Total Revenue (from paid orders only)
SELECT COALESCE(SUM(total_amount), 0) as total_revenue 
FROM orders 
WHERE payment_status = 'PAID';

-- Total Customers (unique email addresses)
SELECT COUNT(DISTINCT customer_email) as total_customers FROM orders;

-- Total Menu Items
SELECT COUNT(*) as total_menu_items FROM menu_items;

-- Recent Orders (last 5)
SELECT 
    o.id,
    o.customer_name,
    o.customer_email,
    o.total_amount,
    o.status,
    o.payment_status,
    o.created_at
FROM orders o
ORDER BY o.created_at DESC
LIMIT 5;

-- Popular Menu Items (most ordered)
SELECT 
    mi.name,
    SUM(oi.quantity) as total_quantity,
    COUNT(oi.order_id) as order_count
FROM menu_items mi
JOIN order_items oi ON mi.id = oi.menu_item_id
GROUP BY mi.id, mi.name
ORDER BY total_quantity DESC
LIMIT 10;
```

### Order Management Queries

```sql
-- Get Order Details with Items
SELECT 
    o.id,
    o.customer_name,
    o.customer_email,
    o.customer_phone,
    o.status,
    o.total_amount,
    o.payment_status,
    o.payment_method,
    o.created_at,
    oi.quantity,
    oi.price as item_price,
    mi.name as item_name
FROM orders o
LEFT JOIN order_items oi ON o.id = oi.order_id
LEFT JOIN menu_items mi ON oi.menu_item_id = mi.id
WHERE o.id = 1
ORDER BY oi.id;

-- Get All Orders with Item Count
SELECT 
    o.id,
    o.customer_name,
    o.customer_email,
    o.total_amount,
    o.status,
    o.payment_status,
    COUNT(oi.id) as item_count,
    o.created_at
FROM orders o
LEFT JOIN order_items oi ON o.id = oi.order_id
GROUP BY o.id, o.customer_name, o.customer_email, o.total_amount, o.status, o.payment_status, o.created_at
ORDER BY o.created_at DESC;
```

### Menu Management Queries

```sql
-- Get All Menu Items
SELECT * FROM menu_items ORDER BY category, name;

-- Get Available Menu Items Only
SELECT * FROM menu_items WHERE available = true ORDER BY category, name;

-- Get Menu Items by Category
SELECT * FROM menu_items WHERE category = 'Fast Food' ORDER BY name;

-- Update Menu Item Availability
UPDATE menu_items SET available = false WHERE id = 1;
UPDATE menu_items SET available = true WHERE id = 1;

-- Update Menu Item Price
UPDATE menu_items SET price = 180.00 WHERE id = 1;
```

## 🚀 Database Connection Configuration

### Spring Boot application.properties

```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/restaurant_db
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### MySQL Configuration (Alternative)

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

## 📈 Performance Optimization

### Indexes for Better Performance

```sql
-- Additional indexes for performance
CREATE INDEX idx_menu_items_category ON menu_items(category);
CREATE INDEX idx_menu_items_available ON menu_items(available);
CREATE INDEX idx_orders_payment_status ON orders(payment_status);
CREATE INDEX idx_order_items_composite ON order_items(order_id, menu_item_id);
```

### Views for Dashboard

```sql
-- Create a view for dashboard statistics
CREATE VIEW dashboard_stats AS
SELECT 
    (SELECT COUNT(*) FROM orders) as total_orders,
    (SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE payment_status = 'PAID') as total_revenue,
    (SELECT COUNT(DISTINCT customer_email) FROM orders) as total_customers,
    (SELECT COUNT(*) FROM menu_items) as total_menu_items;

-- Query the dashboard stats
SELECT * FROM dashboard_stats;
```

## 🔧 Maintenance Queries

### Clean Up Old Data

```sql
-- Delete orders older than 1 year (if needed)
DELETE FROM orders WHERE created_at < NOW() - INTERVAL '1 year';

-- Reset auto-increment (PostgreSQL)
TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_items RESTART IDENTITY;
TRUNCATE TABLE menu_items RESTART IDENTITY;

-- Reset auto-increment (MySQL)
TRUNCATE TABLE orders;
TRUNCATE TABLE order_items;
TRUNCATE TABLE menu_items;
```

## 📝 Notes

1. **Foreign Key Constraints**: Ensure order_items references valid orders and menu_items
2. **Data Types**: Use appropriate data types for your database system
3. **Indexes**: Add more indexes based on your query patterns
4. **Backup**: Always backup your database before making changes
5. **Transactions**: Use transactions for multi-table operations

## 🎯 Quick Start

1. Run the database creation commands
2. Execute the table creation scripts
3. Insert sample data for testing
4. Verify with the dashboard queries
5. Start your Spring Boot application

The database is now ready to work with your restaurant billing system frontend!
