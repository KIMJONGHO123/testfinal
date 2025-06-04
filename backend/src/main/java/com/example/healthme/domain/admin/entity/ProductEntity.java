package com.example.healthme.domain.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private String category;

    @Column(length=300)
    private String productName;

    private int productPrice;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = true)
    private Integer amount;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "detail_url")
    private String detailUrl;
}
