package com.software.jpa_locking.service;

import com.software.jpa_locking.dto.OrderRequest;
import com.software.jpa_locking.entity.Product;
import com.software.jpa_locking.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;

    @Transactional
    public String placeOrderPessimistic(OrderRequest request) {
        Product product = productRepository.findByIdWithPessimisticLock(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            return "Failed: Not enough stock for product " + product.getName();
        }

        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);
        
        return "Order placed successfully for User " + request.getUserId() + ". Remaining stock: " + product.getStock();
    }

    @Transactional
    public String placeOrderOptimistic(OrderRequest request) {
        try {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < request.getQuantity()) {
                return "Failed: Not enough stock for product " + product.getName();
            }

            product.setStock(product.getStock() - request.getQuantity());
            productRepository.save(product);

            return "Order placed successfully for User " + request.getUserId() + ". Remaining stock: " + product.getStock();
        } catch (ObjectOptimisticLockingFailureException e) {
            return "Failed: Order conflicted with another transaction. Please try again.";
        }
    }
}
