package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or SEQUENCE if needed
    private Long id;

    @Column(name = "total_capacity", nullable = false)
    private Double totalCapacity;
}