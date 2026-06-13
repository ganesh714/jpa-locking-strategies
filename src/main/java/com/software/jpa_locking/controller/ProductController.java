package com.software.jpa_locking.controller;

import com.software.jpa_locking.dto.ResetResponse;
import com.software.jpa_locking.exception.ProductNotFoundException;
import com.software.jpa_locking.model.Product;
import com.software.jpa_locking.repository.ProductRepository;
import com.software.jpa_locking.service.OrderStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderStatsService statsService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return ResponseEntity.ok(product);
    }

    @PostMapping("/reset")
    @Transactional
    public ResponseEntity<ResetResponse> resetSystem() {
        if (!productRepository.existsById(1L)) {
             productRepository.save(new Product(1L, "Test Product", 20, 0L));
        } else {
             productRepository.resetProductStockAndVersion(1L, 20);
        }

        statsService.reset();

        return ResponseEntity.ok(new ResetResponse("success", "System has been reset to initial state."));
    }
}
