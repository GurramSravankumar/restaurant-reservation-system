package com.sk.restaurant.config;

import com.sk.restaurant.entity.RestaurantTable;
import com.sk.restaurant.entity.User;
import com.sk.restaurant.enums.Role;
import com.sk.restaurant.repository.TableRepository;
import com.sk.restaurant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TableRepository tableRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Admin Manager");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("password123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            User customer = new User();
            customer.setName("John Doe");
            customer.setEmail("customer@example.com");
            customer.setPassword(passwordEncoder.encode("password123"));
            customer.setRole(Role.USER);
            userRepository.save(customer);

            System.out.println(
                    ">>> Database seeded: Admin (admin@example.com) and Customer (customer@example.com) created with password: password123");
        }

        if (tableRepository.count() == 0) {
            RestaurantTable t1 = new RestaurantTable();
            t1.setTableNumber("101");
            t1.setCapacity(2);
            tableRepository.save(t1);

            RestaurantTable t2 = new RestaurantTable();
            t2.setTableNumber("102");
            t2.setCapacity(4);
            tableRepository.save(t2);

            RestaurantTable t3 = new RestaurantTable();
            t3.setTableNumber("103");
            t3.setCapacity(6);
            tableRepository.save(t3);

            RestaurantTable t4 = new RestaurantTable();
            t4.setTableNumber("104");
            t4.setCapacity(8);
            tableRepository.save(t4);

            System.out.println(">>> Database seeded: Tables T-101, T-102, T-103, T-104 initialized.");
        }
    }
}
