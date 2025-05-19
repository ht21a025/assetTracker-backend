package com.example.assettracker.controller;

import com.example.assettracker.model.AppUser;
import com.example.assettracker.model.AssetRecord;
import com.example.assettracker.model.AssetInitial;
import com.example.assettracker.repository.AppUserRepository;
import com.example.assettracker.repository.AssetRecordRepository;
import com.example.assettracker.repository.AssetInitialRepository;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.*;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://asset-tracker-frontend.vercel.app"
})
@RestController
@RequestMapping("/api/asset")
public class AssetController {

    private final AssetRecordRepository recordRepo;
    private final AppUserRepository userRepo;
    private final AssetInitialRepository initialRepo;

    public AssetController(
            AssetRecordRepository recordRepo,
            AppUserRepository userRepo,
            AssetInitialRepository initialRepo) {
        this.recordRepo = recordRepo;
        this.userRepo = userRepo;
        this.initialRepo = initialRepo;
    }

    @PostMapping("/initial")
    public void setInitialAsset(@RequestParam int amount, @RequestParam Long userId) {
        AppUser user = userRepo.findById(userId).orElseThrow();
        AssetInitial initial = initialRepo.findByUser(user)
                .orElse(AssetInitial.builder().user(user).build());
        initial.setAmount(amount);
        initialRepo.save(initial);
    }

    @GetMapping("/summary")
    public List<Map<String, Object>> getSummary(@RequestParam Long userId) {
        AppUser user = userRepo.findById(userId).orElseThrow();
        List<Map<String, Object>> summary = new ArrayList<>();

        int currentAsset = initialRepo.findByUser(user)
                .map(AssetInitial::getAmount)
                .orElse(0);

        Map<String, Object> initialEntry = new HashMap<>();
        initialEntry.put("month", "初期資産");
        initialEntry.put("asset", currentAsset);
        initialEntry.put("income", 0);
        initialEntry.put("expense", 0);
        summary.add(initialEntry);

        List<AssetRecord> records = recordRepo.findAllByUserOrderByMonthAsc(user);
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

    @PostMapping("/record")
    public AssetRecord addOrUpdateRecord(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        String monthStr = payload.get("month").toString();
        int income = Integer.parseInt(payload.get("income").toString());
        int expense = Integer.parseInt(payload.get("expense").toString());

        AppUser user = userRepo.findById(userId).orElseThrow();
        YearMonth month = YearMonth.parse(monthStr);

        Optional<AssetRecord> existing = recordRepo.findByUserAndMonth(user, month);
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

        return recordRepo.save(record);
    }
}
