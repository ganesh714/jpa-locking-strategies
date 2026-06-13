package com.software.jpa_locking.controller;

import com.software.jpa_locking.dto.OrderRequest;
import com.software.jpa_locking.dto.OrderResponse;
import com.software.jpa_locking.dto.StatsResponse;
import com.software.jpa_locking.service.OrderService;
import com.software.jpa_locking.service.OrderStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    OrderService orderService;
    
    @Autowired
    OrderStatsService statsService;

    @PostMapping("/pessimistic")
    public ResponseEntity<OrderResponse> placeOrderPessimistic(@RequestBody OrderRequest request) {
        OrderResponse result = orderService.placeOrderPessimistic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/optimistic")
    public ResponseEntity<OrderResponse> placeOrderOptimistic(@RequestBody OrderRequest request) {
        OrderResponse result = orderService.placeOrderOptimistic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
