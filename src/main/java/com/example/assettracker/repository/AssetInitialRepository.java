package com.example.assettracker.repository;

import com.example.assettracker.model.AppUser;
import com.example.assettracker.model.AssetInitial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetInitialRepository extends JpaRepository<AssetInitial, Long> {
    Optional<AssetInitial> findByUser(AppUser user);
}
