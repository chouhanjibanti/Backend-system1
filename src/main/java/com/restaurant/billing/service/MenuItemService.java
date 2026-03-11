package com.restaurant.billing.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.restaurant.billing.entity.MenuItem;
import com.restaurant.billing.repository.MenuItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuItemService {
	private final MenuItemRepository repo;
	
	public MenuItem add(MenuItem item) {
		item.setCreatedAt(LocalDateTime.now());
		return repo.save(item);
	}
	
	public List<MenuItem> getAll(){
		return repo.findAll();
	}
	
	public MenuItem update(Long id, MenuItem item) {
		MenuItem existing = repo.findById(id).orElseThrow();
		existing.setName(item.getName());
		existing.setCategory(item.getCategory());
		existing.setPrice(item.getPrice());
		existing.setAvailable(item.isAvailable());
		return repo.save(existing);
	}
	
	public void delete(Long id) {
		repo.deleteById(id);
	}
	
}
