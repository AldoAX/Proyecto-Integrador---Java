package com.axity.curso.retailservice.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.axity.curso.retailservice.model.Product;

public interface ProductoRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndActivoTrue(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id and p.activo = true")
    Optional<Product> buscarActivoPorIdParaActualizar(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> buscarPorIdParaActualizar(@Param("id") Long id);

    boolean existsBySkuIgnoreCase(String sku);

    boolean existsBySkuIgnoreCaseAndIdNot(String sku, Long id);

    List<Product> findByActivoTrue();

    List<Product> findByCategoryIgnoreCaseAndActivoTrue(String category);

    List<Product> findByActivoTrueAndPriceLessThanEqual(BigDecimal price);

    List<Product> findByCategoryIgnoreCaseAndActivoTrueAndPriceLessThanEqual(
            String category, BigDecimal price);
}
