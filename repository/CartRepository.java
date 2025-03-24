package com.TrendHive.TrendHive.repository;

import com.TrendHive.TrendHive.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(int userId);

//    @Modifying
//    @Query("UPDATE Cart c SET c.quantity = c.quantity + :quantity WHERE c.cart.id = :cartId AND c.product.id = :productId")
//    int updateCartProductQuantity(@Param("cartId") int cartId, @Param("productId") int productId, @Param("quantity") int quantity);

//    Optional<CartProduct> findByCartIdAndProductId(int cartId, int productId);


}
