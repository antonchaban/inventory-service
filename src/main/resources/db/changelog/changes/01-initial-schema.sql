-- liquibase formatted sql

-- 1. Warehouses
CREATE TABLE warehouses (
                            id BIGSERIAL PRIMARY KEY,
                            total_capacity DOUBLE PRECISION NOT NULL
);

-- 2. Products
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          volume_m3 DOUBLE PRECISION NOT NULL
);

-- 3. Stock Levels
CREATE TABLE stock_levels (
                              warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
                              product_id BIGINT NOT NULL REFERENCES products(id),
                              quantity INTEGER NOT NULL DEFAULT 0,
                              PRIMARY KEY (warehouse_id, product_id)
);

-- 4. Supplies (Input)
CREATE TABLE supplies (
                          id BIGSERIAL PRIMARY KEY,
                          warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
                          status VARCHAR(50) NOT NULL, -- 'RECEIVED', 'DISTRIBUTED'
                          arrival_date TIMESTAMP
);

CREATE TABLE supply_items (
                              id BIGSERIAL PRIMARY KEY,
                              supply_id BIGINT NOT NULL REFERENCES supplies(id),
                              product_id BIGINT NOT NULL REFERENCES products(id),
                              quantity INTEGER NOT NULL
);

-- 5. Shipments (Output)
CREATE TABLE shipments (
                           id BIGSERIAL PRIMARY KEY,
                           source_id BIGINT NOT NULL REFERENCES warehouses(id),
                           destination_id BIGINT NOT NULL REFERENCES warehouses(id), -- Critical: matches Go Spec
                           status VARCHAR(50) NOT NULL -- 'PLANNED', 'IN_TRANSIT', 'DELIVERED'
);

CREATE TABLE shipment_items (
                                id BIGSERIAL PRIMARY KEY,
                                shipment_id BIGINT NOT NULL REFERENCES shipments(id),
                                product_id BIGINT NOT NULL REFERENCES products(id),
                                quantity INTEGER NOT NULL
);

CREATE INDEX idx_shipments_status_dest ON shipments(destination_id, status);
CREATE INDEX idx_supply_items_supply ON supply_items(supply_id);