-- liquibase formatted sql

-- Додаємо колонки аудиту до таблиці складів
ALTER TABLE warehouses
    ADD COLUMN created_by VARCHAR(50),
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN last_modified_by VARCHAR(50),
ADD COLUMN last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- До таблиці товарів
ALTER TABLE products
    ADD COLUMN created_by VARCHAR(50),
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN last_modified_by VARCHAR(50),
ADD COLUMN last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- До таблиці поставок
ALTER TABLE supplies
    ADD COLUMN created_by VARCHAR(50),
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN last_modified_by VARCHAR(50),
ADD COLUMN last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- До таблиці переміщень
ALTER TABLE shipments
    ADD COLUMN created_by VARCHAR(50),
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN last_modified_by VARCHAR(50),
ADD COLUMN last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- (Опціонально) До таблиці залишків (Stock Levels)
ALTER TABLE stock_levels
    ADD COLUMN created_by VARCHAR(50),
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN last_modified_by VARCHAR(50),
ADD COLUMN last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;