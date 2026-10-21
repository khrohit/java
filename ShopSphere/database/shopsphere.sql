CREATE DATABASE IF NOT EXISTS shopsphere
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopsphere;

CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  mobile VARCHAR(30),
  role ENUM('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
  status TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS addresses (
  address_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  address_line VARCHAR(255) NOT NULL,
  city VARCHAR(100) NOT NULL,
  state VARCHAR(100) NOT NULL,
  pincode VARCHAR(20) NOT NULL,
  address_type VARCHAR(30) NOT NULL DEFAULT 'HOME',
  CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS categories (
  category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(500),
  status TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS products (
  product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  category_id BIGINT,
  name VARCHAR(180) NOT NULL,
  brand VARCHAR(100),
  description TEXT,
  price DECIMAL(12,2) NOT NULL,
  discount DECIMAL(5,2) NOT NULL DEFAULT 0,
  stock INT NOT NULL DEFAULT 0,
  image_url VARCHAR(1000),
  status TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_product_search(name, brand),
  CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cart (
  cart_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  UNIQUE KEY uq_cart_user_product(user_id, product_id),
  CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS wishlist (
  wishlist_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_wishlist_user_product(user_id, product_id),
  CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS orders (
  order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  address_id BIGINT NULL,
  total_amount DECIMAL(12,2) NOT NULL,
  discount DECIMAL(12,2) NOT NULL DEFAULT 0,
  payment_method VARCHAR(40) NOT NULL,
  payment_status VARCHAR(30) NOT NULL,
  order_status VARCHAR(30) NOT NULL DEFAULT 'PLACED',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_order_user_created(user_id, created_at),
  CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS order_items (
  order_item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  product_name VARCHAR(180) NOT NULL,
  quantity INT NOT NULL,
  price DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_item_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
  CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES products(product_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS payments (
  payment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  payment_method VARCHAR(40) NOT NULL,
  transaction_reference VARCHAR(100) NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  payment_status VARCHAR(30) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS reviews (
  review_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  rating TINYINT NOT NULL,
  review_text VARCHAR(1000),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS coupons (
  coupon_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(40) NOT NULL UNIQUE,
  discount_type ENUM('PERCENT','FIXED') NOT NULL,
  discount_value DECIMAL(10,2) NOT NULL,
  minimum_order DECIMAL(12,2) NOT NULL DEFAULT 0,
  maximum_discount DECIMAL(12,2),
  expiry_date DATE,
  status TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS coupon_usage (
  usage_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  coupon_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  order_id BIGINT NOT NULL,
  used_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_coupon_usage_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id),
  CONSTRAINT fk_coupon_usage_user FOREIGN KEY (user_id) REFERENCES users(user_id),
  CONSTRAINT fk_coupon_usage_order FOREIGN KEY (order_id) REFERENCES orders(order_id)
) ENGINE=InnoDB;

INSERT IGNORE INTO categories(name, description) VALUES
('Audio', 'Headphones, speakers and audio accessories'),
('Wearables', 'Smart watches and wearable technology'),
('Accessories', 'Everyday tech essentials'),
('Computing', 'Useful gear for work, study and play'),
('Home', 'Thoughtful upgrades for everyday spaces'),
('Fitness', 'Gear to support active days');

INSERT INTO products(category_id, name, brand, description, price, discount, stock, image_url)
SELECT c.category_id, seed.name, seed.brand, seed.description, seed.price, seed.discount, seed.stock, seed.image_url
FROM (
  SELECT 'Audio' category_name, 'Studio Wireless Headphones' name, 'Aural' brand, 'Comfortable over-ear wireless sound with rich bass and all-day battery life.' description, 6499.00 price, 15.00 discount, 24 stock, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=900&q=85' image_url
  UNION ALL SELECT 'Audio', 'Compact Bluetooth Speaker', 'Soundwave', 'Portable waterproof speaker for your desk, room or weekend trip.', 3299.00, 10.00, 38, 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Wearables', 'Pulse Smartwatch', 'Northstar', 'A bright AMOLED display, activity tracking and helpful daily insights.', 8999.00, 12.00, 17, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Accessories', 'Everyday Backpack', 'Fieldwork', 'A clean, durable carry with a padded pocket for your laptop.', 4599.00, 0.00, 21, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Accessories', 'Minimal Desk Lamp', 'Form & Function', 'Warm adjustable light with an uncluttered profile for focused work.', 2799.00, 8.00, 30, 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Audio', 'Everyday Earbuds', 'Aural', 'True wireless earbuds with a pocket-size charging case.', 3999.00, 5.00, 42, 'https://images.unsplash.com/photo-1606220945770-b5b6c2c55bf1?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Computing', 'Mechanical Keyboard', 'Keycraft', 'A compact mechanical keyboard with tactile switches and a clean aluminum finish.', 5499.00, 12.00, 19, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Computing', 'Precision Wireless Mouse', 'Northstar', 'A comfortable wireless mouse with adjustable tracking for focused work.', 2499.00, 8.00, 31, 'https://images.unsplash.com/photo-1527814050087-3793815479db?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Computing', 'USB-C Multiport Hub', 'Fieldwork', 'A travel-friendly hub with HDMI, USB and card reader connections.', 3199.00, 10.00, 26, 'https://images.unsplash.com/photo-1625842268584-8f3296236761?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Computing', 'Laptop Stand', 'Form & Function', 'An adjustable aluminum stand that lifts your screen to a comfortable height.', 2899.00, 0.00, 22, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Home', 'Ceramic Pour-Over Set', 'Morrow', 'A simple ceramic dripper and mug set for a slower morning coffee.', 1899.00, 5.00, 16, 'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Home', 'Insulated Travel Bottle', 'Fieldwork', 'A stainless steel bottle designed to keep drinks at temperature on the go.', 1499.00, 0.00, 40, 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Home', 'Soft Glow Table Light', 'Form & Function', 'A warm, compact lamp that brings gentle light to a bedside or reading nook.', 3499.00, 15.00, 13, 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Fitness', 'Everyday Yoga Mat', 'Movewell', 'A cushioned, non-slip mat for stretching, yoga and home workouts.', 2199.00, 10.00, 28, 'https://images.unsplash.com/photo-1592432678016-e910b452f9a2?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Fitness', 'Active Loop Band Set', 'Movewell', 'Three resistance levels for warmups, mobility and strength sessions.', 999.00, 0.00, 45, 'https://images.unsplash.com/photo-1598289431512-b97b0917affc?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Accessories', 'Everyday Sunglasses', 'Morrow', 'Lightweight everyday frames with a classic shape and UV protection.', 1799.00, 8.00, 20, 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Accessories', 'Canvas Weekender Tote', 'Fieldwork', 'A roomy, durable tote for daily errands and short weekend trips.', 2399.00, 5.00, 18, 'https://images.unsplash.com/photo-1547949003-9792a18a2601?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Audio', 'Noise-Cancelling Headphones', 'Soundwave', 'Over-ear headphones with soft cushions and focused listening for commutes or study.', 7999.00, 18.00, 14, 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Audio', 'Studio USB Microphone', 'Aural', 'A clear-sounding desktop microphone for calls, streaming and home recording.', 4299.00, 10.00, 12, 'https://images.unsplash.com/photo-1590602847861-f357a9332bbc?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Wearables', 'Active Fitness Watch', 'Move well', 'A lightweight activity watch with daily movement and sleep tracking.', 6999.00, 8.00, 20, 'https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Computing', 'Portable SSD 1TB', 'Northstar', 'A compact solid-state drive for carrying project files and backups.', 7499.00, 12.00, 15, 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Computing', '1080p Webcam', 'Keycraft', 'A plug-and-play webcam with a built-in privacy shutter for clear video calls.', 2799.00, 5.00, 23, 'https://images.unsplash.com/photo-1587826080692-f439cd0b70da?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Computing', 'Laptop Sleeve 14-inch', 'Fieldwork', 'A padded, water-resistant sleeve to protect your laptop while travelling.', 1699.00, 0.00, 32, 'https://images.unsplash.com/photo-1554456854-55a089fd4cb2?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Home', 'Cotton Cushion Cover Set', 'Morrow', 'Two soft woven covers that add a calm, textured touch to a living space.', 1299.00, 10.00, 25, 'https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Home', 'Glass Water Carafe', 'Form & Function', 'A simple glass carafe for keeping water ready on the dining or bedside table.', 1199.00, 0.00, 18, 'https://images.unsplash.com/photo-1513558161293-cdaf765edfd7?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Fitness', 'Adjustable Dumbbell Pair', 'Movewell', 'Space-saving adjustable weights for strength sessions at home.', 5999.00, 15.00, 9, 'https://images.unsplash.com/photo-1583454110551-21f2fa2afe61?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Fitness', 'Insulated Sports Bottle', 'Move well', 'A durable, easy-carry bottle for workouts, walks and busy days.', 899.00, 0.00, 36, 'https://images.unsplash.com/photo-1523362628745-0c100150b504?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Accessories', 'Compact Travel Organizer', 'Fieldwork', 'A zippered organizer to keep cables, chargers and small essentials together.', 1399.00, 5.00, 27, 'https://images.unsplash.com/photo-1622560480654-d96214fdc887?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Accessories', 'Classic Analog Watch', 'Morrow', 'A clean everyday watch with a simple dial and comfortable strap.', 3299.00, 12.00, 11, 'https://images.unsplash.com/photo-1524592094714-0f0654e20314?auto=format&fit=crop&w=900&q=85'
) seed
JOIN categories c ON c.name = seed.category_name
WHERE NOT EXISTS (SELECT 1 FROM products WHERE products.name = seed.name);

INSERT IGNORE INTO coupons(code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date)
VALUES ('WELCOME10', 'PERCENT', 10, 1000, 1000, DATE_ADD(CURDATE(), INTERVAL 1 YEAR));
