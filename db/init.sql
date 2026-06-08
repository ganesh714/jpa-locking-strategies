CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

INSERT INTO products (name, stock, version) VALUES ('Limited Edition Widget', 100, 0);
