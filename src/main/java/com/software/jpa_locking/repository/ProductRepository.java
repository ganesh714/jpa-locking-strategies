package com.software.jpa_locking.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.software.jpa_locking.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Product p SET p.stock = :stock, p.version = 0 WHERE p.id = :id")
    void resetProductStockAndVersion(@Param("id") Long id, @Param("stock") int stock);
}
