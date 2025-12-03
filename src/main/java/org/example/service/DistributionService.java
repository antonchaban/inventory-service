package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.*;
import org.example.grpc.DistributionPlan;
import org.example.grpc.Move;
import org.example.repository.ShipmentRepository;
import org.example.repository.SupplyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionService {

    private final ShipmentRepository shipmentRepository;
    private final SupplyRepository supplyRepository;

    /**
     * Головний метод обробки результату від Go.
     * Виконується в одній транзакції: або все зберігається, або нічого.
     */
    @Transactional
    public void applyDistributionPlan(DistributionPlan plan) {
        log.info("Applying distribution plan for Request ID: {}, Moves: {}",
                plan.getRequestId(), plan.getMovesCount());

        // 1. Групуємо переміщення по Destination Warehouse ID.
        // Go надсилає список Moves (товар -> куди).
        // Нам треба створити один Shipment (машина) на один напрямок, що містить багато товарів.
        Map<Long, List<Move>> movesByDestination = plan.getMovesList().stream()
                .collect(Collectors.groupingBy(move -> Long.parseLong(move.getWarehouseId())));

        List<Shipment> shipmentsToSave = new ArrayList<>();

        // 2. Створюємо Shipments
        for (Map.Entry<Long, List<Move>> entry : movesByDestination.entrySet()) {
            Long destinationId = entry.getKey();
            List<Move> moves = entry.getValue();

            // Створюємо заголовок Shipment
            Shipment shipment = new Shipment();
            shipment.setDestinationId(destinationId);
            shipment.setStatus(ShipmentStatus.PLANNED); // Резервує місце!

            // TODO: У реальному житті sourceId треба брати з Supply або Request.
            // Для спрощення хардкодимо ID головного хабу або беремо з контексту.
            shipment.setSourceId(1L);

            // Створюємо вміст (Items)
            List<ShipmentItem> items = new ArrayList<>();
            for (Move move : moves) {
                ShipmentItem item = new ShipmentItem();
                item.setShipment(shipment);
                item.setProductId(Long.parseLong(move.getProductId()));
                item.setQuantity(move.getQuantity());
                items.add(item);
            }

            shipment.setItems(items);
            shipmentsToSave.add(shipment);
        }

        // 3. Зберігаємо все в БД (Cascade TYPE.ALL збереже і items)
        shipmentRepository.saveAll(shipmentsToSave);

        log.info("Successfully created {} shipments from plan", shipmentsToSave.size());
    }
}