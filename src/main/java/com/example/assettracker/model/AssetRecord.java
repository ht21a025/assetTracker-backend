package com.example.assettracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int income;
    private int expense;

    @Column(unique = true)
    private YearMonth month;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}
