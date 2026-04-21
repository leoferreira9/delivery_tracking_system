CREATE TABLE delivery_history(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    delivery_id BIGINT NOT NULL,
    status ENUM('RECEIVED', 'PREPARING', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED') NOT NULL,
    changed_at DATETIME NOT NULL,

    CONSTRAINT fk_delivery_id FOREIGN KEY (delivery_id) REFERENCES deliveries(id)
);