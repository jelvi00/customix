CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    username   VARCHAR(30) NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    role       VARCHAR(10),
    status     SMALLINT,
    created_by VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_by VARCHAR(30),
    updated_at TIMESTAMP WITHOUT TIME ZONE
);
INSERT INTO users (id, username, password, role, status, created_by, created_at)
VALUES ('84e6cd62-9415-4db5-9596-edd934660e26',
        '_system_',
        'todo_define',
        'ADMIN', 1, '_system_', now());

CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    email VARCHAR(40),
    status SMALLINT,
    created_by VARCHAR(30),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(30),
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(30) NOT NULL,
    postal_code VARCHAR(30),
    customer_id UUID,
    status SMALLINT,
    created_by VARCHAR(30),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(30),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);
