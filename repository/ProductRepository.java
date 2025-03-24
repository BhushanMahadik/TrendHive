package com.TrendHive.TrendHive.repository;

import com.TrendHive.TrendHive.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @EntityGraph(value = "product.categories_merchant")
    List<Product> findByTitle(String title);

    @EntityGraph(value = "products.categories_merchant")
//    @Query("SELECT c FROM categories c WHERE c.name LIKE %:name%")
    Page<Product> findByCategoriesName(String category,Pageable pageable);

    @Override
    @EntityGraph(value = "products.categories_merchant")
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(value = "products.categories_merchant")
    @Query("SELECT p FROM products p WHERE p.title LIKE %:title%")
    Page<Product> findByTitleContaining(@Param("title") String title, Pageable pageable);


    @Transactional
    @Modifying
    @Query("DELETE FROM products p WHERE p.merchant.id = :merchantId")
    void deleteByMerchantId(@Param("merchantId") int merchantId);
}
