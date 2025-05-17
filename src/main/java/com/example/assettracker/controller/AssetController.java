package com.example.assettracker.controller;

import com.example.assettracker.model.AppUser;
import com.example.assettracker.model.AssetRecord;
import com.example.assettracker.repository.AppUserRepository;
import com.example.assettracker.repository.AssetRecordRepository;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/asset")
public class AssetController {

    private final AssetRecordRepository repository;
    private final AppUserRepository userRepository;

    // ユーザーごとの初期資産を一時的に保持（今回は簡易な構成）
    private final Map<Long, Integer> initialAssets = new HashMap<>();

    public AssetController(AssetRecordRepository repository, AppUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // ✅ 初期資産を設定（ユーザーごと）
    @PostMapping("/initial")
    public void setInitialAsset(@RequestParam int amount, @RequestParam Long userId) {
        initialAssets.put(userId, amount);
    }

    // ✅ 月ごとの収支データを保存（userId含む）
    @PostMapping("/record")
    public AssetRecord addOrUpdateRecord(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        String monthStr = payload.get("month").toString();
        int income = Integer.parseInt(payload.get("income").toString());
        int expense = Integer.parseInt(payload.get("expense").toString());

        AppUser user = userRepository.findById(userId).orElseThrow();
        YearMonth month = YearMonth.parse(monthStr);

        Optional<AssetRecord> existing = repository.findByUserAndMonth(user, month);
        AssetRecord record;

        if (existing.isPresent()) {
            record = existing.get();
            record.setIncome(income);
            record.setExpense(expense);
        } else {
            record = AssetRecord.builder()
                    .user(user)
                    .month(month)
                    .income(income)
                    .expense(expense)
                    .build();
        }

        return repository.save(record);
    }

    @GetMapping("/summary")
    public List<Map<String, Object>> getSummary(@RequestParam Long userId) {
        AppUser user = userRepository.findById(userId).orElseThrow();
        List<AssetRecord> records = repository.findAllByUserOrderByMonthAsc(user);

        int currentAsset = initialAssets.getOrDefault(userId, 0);
        List<Map<String, Object>> summary = new ArrayList<>();

        for (AssetRecord record : records) {
            currentAsset += record.getIncome() - record.getExpense();

            Map<String, Object> entry = new HashMap<>();
            entry.put("month", record.getMonth().toString());
            entry.put("asset", currentAsset);
            entry.put("income", record.getIncome());
            entry.put("expense", record.getExpense());
            summary.add(entry);
        }

        return summary;
    }

}
