-- Restaurant Billing System - Database Setup Script
-- Run this script in PostgreSQL to create tables and insert sample data

-- Drop existing tables if they exist (for fresh setup)
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS menu_items CASCADE;
DROP TABLE IF EXISTS menu_item CASCADE; -- Also drop the old table if it exists

-- Create menu_items table (correct name)
CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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

-- Create order_items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Insert sample menu items
INSERT INTO menu_items (name, category, price, available) VALUES
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

-- Insert sample orders
INSERT INTO orders (customer_name, customer_email, customer_phone, status, total_amount, payment_status) 
VALUES 
('John Doe', 'john@example.com', '9876543210', 'COMPLETED', 570.00, 'PAID'),
('Jane Smith', 'jane@example.com', '9876543211', 'created', 440.00, 'PENDING'),
('Bob Wilson', 'bob@example.com', '9876543212', 'COMPLETED', 760.00, 'PAID');

-- Insert sample order items
INSERT INTO order_items (order_id, menu_item_id, quantity, price) VALUES
(1, 1, 2, 300.00), -- 2 Burgers for Order 1
(1, 2, 1, 250.00), -- 1 Pizza for Order 1
(1, 6, 1, 20.00),  -- 1 Cold Coffee for Order 1 (price adjusted)

(2, 3, 1, 200.00), -- 1 Pasta for Order 2
(2, 4, 2, 240.00), -- 2 Sandwiches for Order 2

(3, 13, 1, 220.00), -- 1 Biryani for Order 3
(3, 14, 1, 280.00), -- 1 Butter Chicken for Order 3
(3, 15, 2, 80.00),  -- 2 Naan for Order 3
(3, 16, 1, 180.00); -- 1 Dal Makhani for Order 3

-- Create indexes for better performance
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_customer_email ON orders(customer_email);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_menu_item_id ON order_items(menu_item_id);
CREATE INDEX idx_menu_items_category ON menu_items(category);
CREATE INDEX idx_menu_items_available ON menu_items(available);

-- Verify data insertion
SELECT 'Menu Items Count: ' || COUNT(*) FROM menu_items;
SELECT 'Orders Count: ' || COUNT(*) FROM orders;
SELECT 'Order Items Count: ' || COUNT(*) FROM order_items;

-- Show sample data
SELECT 'Sample Menu Items:' as info;
SELECT id, name, category, price, available FROM menu_items LIMIT 5;

SELECT 'Sample Orders:' as info;
SELECT id, customer_name, total_amount, status, payment_status FROM orders;

SELECT 'Sample Order Items:' as info;
SELECT oi.order_id, mi.name, oi.quantity, oi.price 
FROM order_items oi 
JOIN menu_items mi ON oi.menu_item_id = mi.id 
LIMIT 5;

-- Check if tables exist with correct names
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public' AND table_name IN ('menu_items', 'orders', 'order_items');
