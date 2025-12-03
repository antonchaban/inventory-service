package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CalculateRequest;
import org.example.dto.DistributionEvent;
import org.example.entity.Supply;
import org.example.repository.SupplyRepository;
import org.example.service.EventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/distribution")
@RequiredArgsConstructor
public class DistributionController {

    private final SupplyRepository supplyRepository;
    private final EventPublisher eventPublisher;

    @PostMapping("/calculate")
    public ResponseEntity<?> triggerCalculation(@RequestBody CalculateRequest request) {
        // 1. Валідація: Чи існує така поставка?
        Supply supply = supplyRepository.findById(request.getSupplyId())
                .orElseThrow(() -> new RuntimeException("Supply not found: " + request.getSupplyId()));

        // 2. Генерація ID запиту
        String requestId = UUID.randomUUID().toString();

        // 3. Формування події
        DistributionEvent event = DistributionEvent.builder()
                .requestId(requestId)
                .supplyId(supply.getId())
                .sourceWarehouseId(supply.getWarehouseId())
                .build();

        // 4. Відправка в RabbitMQ -> Go Service
        eventPublisher.sendCalculationRequest(event);

        // 5. Повертаємо ID клієнту (щоб він міг пулити статус)
        return ResponseEntity.accepted().body(Map.of(
                "message", "Calculation triggered successfully",
                "request_id", requestId
        ));
    }
}