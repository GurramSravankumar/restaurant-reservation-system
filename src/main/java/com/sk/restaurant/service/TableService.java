package com.sk.restaurant.service;

import com.sk.restaurant.dto.TableResponse;
import com.sk.restaurant.entity.RestaurantTable;
import java.util.List;

public interface TableService {
    List<TableResponse> getAllTables();

    TableResponse getTableById(Long id);

    TableResponse createTable(RestaurantTable table);

    TableResponse updateTable(Long id, RestaurantTable tableDetails);

    void deleteTable(Long id);
}
