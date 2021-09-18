/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.finalbackend.tokoshop.repository;

import com.finalbackend.tokoshop.entity.CartItem;
import java.util.List;
import org.springframework.data.repository.CrudRepository;


public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
    
    public List<CartItem> findAllByCartId(Integer cart);

}
