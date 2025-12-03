package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Data
@Entity
@Table(name = "stock_levels")
public class StockLevel {

    @EmbeddedId
    private StockLevelId id;

    @Column(nullable = false)
    private Integer quantity;

    // --- Composite Key Class ---
    @Data
    @Embeddable
    public static class StockLevelId implements Serializable {
        @Column(name = "warehouse_id")
        private Long warehouseId;

        @Column(name = "product_id")
        private Long productId;
    }
}