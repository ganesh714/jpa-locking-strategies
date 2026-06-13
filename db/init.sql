DROP TABLE IF EXISTS products;

CREATE TABLE products (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL CHECK (stock >= 0),
    version BIGINT NOT NULL DEFAULT 0
);

INSERT INTO products (id, name, stock, version) VALUES (1, 'Test Product', 20, 0);
