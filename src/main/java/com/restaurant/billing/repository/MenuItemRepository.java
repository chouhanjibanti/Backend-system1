package com.restaurant.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restaurant.billing.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{

}
