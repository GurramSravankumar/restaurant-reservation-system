package com.sk.restaurant.service;

import com.sk.restaurant.dto.TableResponse;
import com.sk.restaurant.entity.RestaurantTable;
import com.sk.restaurant.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;

    private TableResponse mapToResponse(RestaurantTable table) {
        return TableResponse.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .build();
    }

    @Override
    public List<TableResponse> getAllTables() {
        List<RestaurantTable> tables = tableRepository.findAll();
        List<TableResponse> responses = new java.util.ArrayList<>();
        for (RestaurantTable t : tables) {
            responses.add(mapToResponse(t));
        }
        return responses;
    }

    @Override
    public TableResponse getTableById(Long id) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
        return mapToResponse(table);
    }

    @Override
    public TableResponse createTable(RestaurantTable table) {
        if (tableRepository.existsByTableNumber(table.getTableNumber())) {
            throw new RuntimeException("Table number already exists: " + table.getTableNumber());
        }
        RestaurantTable saved = tableRepository.save(table);
        return mapToResponse(saved);
    }

    @Override
    public TableResponse updateTable(Long id, RestaurantTable tableDetails) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));

        if (!table.getTableNumber().equals(tableDetails.getTableNumber()) &&
                tableRepository.existsByTableNumber(tableDetails.getTableNumber())) {
            throw new RuntimeException("Table number already exists: " + tableDetails.getTableNumber());
        }

        table.setTableNumber(tableDetails.getTableNumber());
        table.setCapacity(tableDetails.getCapacity());
        RestaurantTable updated = tableRepository.save(table);
        return mapToResponse(updated);
    }

    @Override
    public void deleteTable(Long id) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
        tableRepository.delete(table);
    }
}
