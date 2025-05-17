package com.example.assettracker.repository;

import com.example.assettracker.model.AppUser;
import com.example.assettracker.model.AssetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface AssetRecordRepository extends JpaRepository<AssetRecord, Long> {
    List<AssetRecord> findAllByOrderByMonthAsc();

    @Query("SELECT a FROM AssetRecord a WHERE a.month = ?1")
    Optional<AssetRecord> findByMonth(YearMonth month);

    List<AssetRecord> findAllByUserOrderByMonthAsc(AppUser user);
    Optional<AssetRecord> findByUserAndMonth(AppUser user, YearMonth month);
}
