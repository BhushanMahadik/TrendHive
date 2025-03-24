package com.TrendHive.TrendHive.repository;

import com.TrendHive.TrendHive.entities.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {

    Optional<CartProduct> findByCartIdAndProductId(int cartId, int productId);


}
