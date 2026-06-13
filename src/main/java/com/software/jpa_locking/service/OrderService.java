package com.software.jpa_locking.service;

import com.software.jpa_locking.dto.OrderRequest;
import com.software.jpa_locking.dto.OrderResponse;
import com.software.jpa_locking.exception.OutOfStockException;
import com.software.jpa_locking.exception.ProductNotFoundException;
import com.software.jpa_locking.model.Product;
import com.software.jpa_locking.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    ProductRepository productRepository;
    
    @Autowired
    OrderStatsService statsService;

    @Transactional
    public OrderResponse placeOrderPessimistic(OrderRequest request) {
        Product product = productRepository.findByIdWithPessimisticLock(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            statsService.incrementFailedOutOfStock();
            throw new OutOfStockException("Not enough stock available");
        }

        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);
        statsService.incrementSuccessfulPessimistic();

        return new OrderResponse(
                UUID.randomUUID().toString(),
                product.getId(),
                request.getQuantity(),
                product.getStock()
        );
    }

    @Transactional
    public OrderResponse placeOrderOptimistic(OrderRequest request) {
        try {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            if (product.getStock() < request.getQuantity()) {
                statsService.incrementFailedOutOfStock();
                throw new OutOfStockException("Not enough stock available");
            }

            product.setStock(product.getStock() - request.getQuantity());
            productRepository.save(product);
            statsService.incrementSuccessfulOptimistic();

            return new OrderResponse(
                    UUID.randomUUID().toString(),
                    product.getId(),
                    request.getQuantity(),
                    product.getStock()
            );
        } catch (ObjectOptimisticLockingFailureException e) {
            statsService.incrementFailedConflict();
            throw e; // Controller advice will handle this
        }
    }
}
