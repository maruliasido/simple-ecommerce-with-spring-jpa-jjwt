/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.finalbackend.tokoshop.repository;

import com.finalbackend.tokoshop.entity.Cart;
import com.finalbackend.tokoshop.entity.User;
import org.springframework.data.repository.CrudRepository;


public interface CartRepository extends CrudRepository<Cart, Integer>{
    public Cart findCartByStatusAndUser(String status, User user);
}
