package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.StockLevel;
import org.example.repository.StockLevelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final StockLevelRepository stockLevelRepository;

    // Перегляд залишків доступний всім авторизованим (Комірник, Логіст, Адмін) [cite: 177]
    @GetMapping("/stocks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<StockLevel>> getCurrentStocks() {
        return ResponseEntity.ok(stockLevelRepository.findAll());
    }

    // Реєстрація надходження (Inbound) - тільки Комірник [cite: 83, 177]
    // (Тут спрощена логіка, в реальності треба оновлювати StockLevel транзакційно)
    @PostMapping("/inbound")
    @PreAuthorize("hasRole('STOREKEEPER')")
    public ResponseEntity<String> registerInbound() {
        // TODO: Реалізувати логіку з розділу 3.2.1 ТЗ
        return ResponseEntity.ok("Inbound registered successfully");
    }
}