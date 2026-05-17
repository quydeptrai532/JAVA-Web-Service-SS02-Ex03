package com.example.ex03.controller;

import com.example.ex03.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private List<Item> items = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong(1);

    // GET: Lấy thông tin Item theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        Optional<Item> item = items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        if (item.isPresent()) {
            return new ResponseEntity<>(item.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // POST: Tạo mới Item
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        item.setId(nextId.getAndIncrement());
        items.add(item);
        return new ResponseEntity<>(item, HttpStatus.CREATED); // 201 Created
    }

    // PUT: Cập nhật Item
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        Optional<Item> existingItem = items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        if (existingItem.isPresent()) {
            Item itemToUpdate = existingItem.get();
            itemToUpdate.setName(itemDetails.getName());
            itemToUpdate.setQuantity(itemDetails.getQuantity());
            itemToUpdate.setPrice(itemDetails.getPrice());
            return new ResponseEntity<>(itemToUpdate, HttpStatus.OK); // 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // DELETE: Xóa Item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        boolean isRemoved = items.removeIf(i -> i.getId().equals(id));

        if (isRemoved) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}