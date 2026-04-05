CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cpf VARCHAR(16) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(10) NOT NULL,
    zip_code VARCHAR(15) NOT NULL,
    city VARCHAR(100) NOT NULL,
    neighborhood VARCHAR(100) NOT NULL
);

CREATE TABLE deliverers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    vehicle_type ENUM('CAR', 'MOTORCYCLE', 'BICYCLE') NOT NULL
);

CREATE TABLE establishments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(10) NOT NULL,
    zip_code VARCHAR(15) NOT NULL,
    city VARCHAR(100) NOT NULL,
    neighborhood VARCHAR(100) NOT NULL
);

CREATE TABLE deliveries(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tracking_code VARCHAR(15) NOT NULL UNIQUE,
    status ENUM('RECEIVED', 'PREPARING', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED') NOT NULL,
    customer_id BIGINT NOT NULL,
    deliverer_id BIGINT,
    establishment_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    delivered_at DATETIME,

    CONSTRAINT fk_customer_id FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_deliverer_id FOREIGN KEY (deliverer_id) REFERENCES deliverers(id),
    CONSTRAINT fk_establishment_id FOREIGN KEY (establishment_id) REFERENCES establishments(id)
);