package com.sk.restaurant.controller;

import com.sk.restaurant.dto.TableResponse;
import com.sk.restaurant.entity.RestaurantTable;
import com.sk.restaurant.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    private boolean isAdmin() {
        for (org.springframework.security.core.GrantedAuthority a : SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()) {
            if (a.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }

    @GetMapping
    public ResponseEntity<List<TableResponse>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableResponse> getTableById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @PostMapping
    public ResponseEntity<?> createTable(@RequestBody RestaurantTable table) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied. Admin role required.");
        }
        try {
            return ResponseEntity.ok(tableService.createTable(table));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTable(@PathVariable Long id, @RequestBody RestaurantTable table) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied. Admin role required.");
        }
        try {
            return ResponseEntity.ok(tableService.updateTable(id, table));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable Long id) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).body("Access denied. Admin role required.");
        }
        try {
            tableService.deleteTable(id);
            return ResponseEntity.ok("Table deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
