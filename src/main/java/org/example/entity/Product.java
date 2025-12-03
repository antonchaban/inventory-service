package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "volume_m3", nullable = false)
    private Double volumeM3;

    // Optional: Name (not strictly in Spec v2, but useful)
    // private String name;
}