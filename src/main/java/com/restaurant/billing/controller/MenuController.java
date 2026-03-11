package com.restaurant.billing.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.billing.entity.MenuItem;
import com.restaurant.billing.service.MenuItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {
	private final MenuItemService service;
	
	@PostMapping
	public MenuItem add(@RequestBody MenuItem item){
		return service.add(item);
	}
	
	@GetMapping
	public List<MenuItem> all(){
		return service.getAll();
	}
	
	@PutMapping("/{id}")
	public MenuItem update(
			@PathVariable Long id,
			@RequestBody MenuItem item) {
		return service.update(id, item);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
