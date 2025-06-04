package com.example.healthme.domain.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transHistory_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Column(length=300)
    private String productName;

    private int productPrice;

    private String transcationPeople;

    private String transcationBank;

    private LocalDateTime transcationTime;

    private String cancel;

    private String success;
}
