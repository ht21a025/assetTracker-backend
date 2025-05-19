package com.example.assettracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "month" }))
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

    @Column(nullable = false)
    private YearMonth month;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
}
