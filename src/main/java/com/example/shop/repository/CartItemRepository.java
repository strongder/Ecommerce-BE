package com.example.shop.repository;

import com.example.shop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

//	@Query("Select count(c.id) from CartItem c where c.isDelete = false and c.cart.id = :id")
//	int numberProduct(@Param("id") Long id);
//
//	@Query("Select c from CartLItem c where c.cart.id = :id and c.isDelete = false")
//	Set<CartItem> findByCart(@Param("id") Long id);
}
