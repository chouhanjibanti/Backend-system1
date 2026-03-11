package com.restaurant.billing.config;

import com.restaurant.billing.entity.MenuItem;
import com.restaurant.billing.entity.Order;
import com.restaurant.billing.entity.OrderItem;
import com.restaurant.billing.entity.User;
import com.restaurant.billing.repository.MenuItemRepository;
import com.restaurant.billing.repository.OrderRepository;
import com.restaurant.billing.repository.OrderItemRepository;
import com.restaurant.billing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    public CommandLineRunner initData() {
        return args -> {
            log.info("Initializing database data...");

            // Initialize admin user if table is empty
            if (userRepository.count() == 0) {
                initializeAdminUser();
                log.info("Admin user initialized successfully");
            }

            // Initialize menu items if table is empty
            if (menuItemRepository.count() == 0) {
                initializeMenuItems();
                log.info("Menu items initialized successfully");
            }

            // Initialize sample orders if table is empty
            if (orderRepository.count() == 0) {
                initializeSampleOrders();
                log.info("Sample orders initialized successfully");
            }

            log.info("Database initialization completed");
        };
    }

    private void initializeAdminUser() {
        User admin = User.builder()
                .username("admin")
                .email("admin@restaurant.com")
                .password(passwordEncoder.encode("admin123"))
                .fullName("Restaurant Administrator")
                .role("ADMIN")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
        log.info("Created default admin user: username=admin, password=admin123");
    }

    private void initializeMenuItems() {
        List<MenuItem> menuItems = List.of(
                MenuItem.builder().name("Burger").category("Fast Food").price(150.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Pizza").category("Fast Food").price(250.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Pasta").category("Italian").price(200.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Sandwich").category("Fast Food").price(120.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("French Fries").category("Snacks").price(80.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Cold Coffee").category("Beverages").price(60.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Ice Cream").category("Desserts").price(90.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Salad").category("Healthy").price(110.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Soup").category("Starters").price(85.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Noodles").category("Chinese").price(180.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Spring Roll").category("Chinese").price(130.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Manchurian").category("Chinese").price(160.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Biryani").category("Indian").price(220.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Butter Chicken").category("Indian").price(280.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Naan").category("Indian").price(40.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Dal Makhani").category("Indian").price(180.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Samosa").category("Snacks").price(50.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Pakora").category("Snacks").price(70.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Lassi").category("Beverages").price(45.00).available(true).createdAt(LocalDateTime.now()).build(),
                MenuItem.builder().name("Tea").category("Beverages").price(25.00).available(true).createdAt(LocalDateTime.now()).build()
        );

        menuItemRepository.saveAll(menuItems);
        log.info("Saved {} menu items", menuItems.size());
    }

    private void initializeSampleOrders() {
        // Get some menu items for sample orders
        List<MenuItem> menuItems = menuItemRepository.findAll();
        if (menuItems.size() < 5) return; // Need at least some menu items

        // Sample Order 1 - John Doe (COMPLETED, PAID)
        Order order1 = Order.builder()
                .customerName("John Doe")
                .customerEmail("john@example.com")
                .customerPhone("9876543210")
                .status("COMPLETED")
                .totalAmount(570.00)
                .paymentStatus("PAID")
                .paymentMethod("CASH")
                .createdAt(LocalDateTime.now())
                .build();
        order1 = orderRepository.save(order1);

        // Add items to order 1
        OrderItem item1_1 = OrderItem.builder()
                .order(order1)
                .menuItem(menuItems.get(0)) // Burger
                .quantity(2)
                .price(300.00)
                .build();

        OrderItem item1_2 = OrderItem.builder()
                .order(order1)
                .menuItem(menuItems.get(1)) // Pizza
                .quantity(1)
                .price(250.00)
                .build();

        OrderItem item1_3 = OrderItem.builder()
                .order(order1)
                .menuItem(menuItems.get(5)) // Cold Coffee
                .quantity(1)
                .price(60.00)
                .build();

        orderItemRepository.saveAll(List.of(item1_1, item1_2, item1_3));

        // Sample Order 2 - Jane Smith (created, PENDING)
        Order order2 = Order.builder()
                .customerName("Jane Smith")
                .customerEmail("jane@example.com")
                .customerPhone("9876543211")
                .status("created")
                .totalAmount(440.00)
                .paymentStatus("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
        order2 = orderRepository.save(order2);

        // Add items to order 2
        OrderItem item2_1 = OrderItem.builder()
                .order(order2)
                .menuItem(menuItems.get(2)) // Pasta
                .quantity(1)
                .price(200.00)
                .build();

        OrderItem item2_2 = OrderItem.builder()
                .order(order2)
                .menuItem(menuItems.get(3)) // Sandwich
                .quantity(2)
                .price(240.00)
                .build();

        orderItemRepository.saveAll(List.of(item2_1, item2_2));

        log.info("Sample orders created successfully");
    }
}
