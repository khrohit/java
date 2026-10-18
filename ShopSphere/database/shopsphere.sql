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
('Accessories', 'Everyday tech essentials');

INSERT INTO products(category_id, name, brand, description, price, discount, stock, image_url)
SELECT c.category_id, seed.name, seed.brand, seed.description, seed.price, seed.discount, seed.stock, seed.image_url
FROM (
  SELECT 'Audio' category_name, 'Studio Wireless Headphones' name, 'Aural' brand, 'Comfortable over-ear wireless sound with rich bass and all-day battery life.' description, 6499.00 price, 15.00 discount, 24 stock, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=900&q=85' image_url
  UNION ALL SELECT 'Audio', 'Compact Bluetooth Speaker', 'Soundwave', 'Portable waterproof speaker for your desk, room or weekend trip.', 3299.00, 10.00, 38, 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Wearables', 'Pulse Smartwatch', 'Northstar', 'A bright AMOLED display, activity tracking and helpful daily insights.', 8999.00, 12.00, 17, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Accessories', 'Everyday Backpack', 'Fieldwork', 'A clean, durable carry with a padded pocket for your laptop.', 4599.00, 0.00, 21, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Accessories', 'Minimal Desk Lamp', 'Form & Function', 'Warm adjustable light with an uncluttered profile for focused work.', 2799.00, 8.00, 30, 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=900&q=85'
  UNION ALL SELECT 'Audio', 'Everyday Earbuds', 'Aural', 'True wireless earbuds with a pocket-size charging case.', 3999.00, 5.00, 42, 'https://images.unsplash.com/photo-1606220945770-b5b6c2c55bf1?auto=format&fit=crop&w=900&q=85'
) seed
JOIN categories c ON c.name = seed.category_name
WHERE NOT EXISTS (SELECT 1 FROM products WHERE products.name = seed.name);

INSERT IGNORE INTO coupons(code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date)
VALUES ('WELCOME10', 'PERCENT', 10, 1000, 1000, DATE_ADD(CURDATE(), INTERVAL 1 YEAR));
