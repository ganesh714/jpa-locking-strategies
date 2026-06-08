package com.software.jpa_locking.controller;

import com.software.jpa_locking.dto.OrderRequest;
import com.software.jpa_locking.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/pessimistic")
    public ResponseEntity<String> placeOrderPessimistic(@RequestBody OrderRequest request) {
        String result = orderService.placeOrderPessimistic(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/optimistic")
    public ResponseEntity<String> placeOrderOptimistic(@RequestBody OrderRequest request) {
        String result = orderService.placeOrderOptimistic(request);
        return ResponseEntity.ok(result);
    }
}
