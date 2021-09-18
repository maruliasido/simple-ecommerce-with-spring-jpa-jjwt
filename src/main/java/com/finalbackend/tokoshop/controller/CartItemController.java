package com.finalbackend.tokoshop.controller;

import com.finalbackend.tokoshop.entity.Cart;
import com.finalbackend.tokoshop.entity.CartItem;
import com.finalbackend.tokoshop.entity.Product;
import com.finalbackend.tokoshop.entity.User;
import com.finalbackend.tokoshop.model.CartItemModel;
import com.finalbackend.tokoshop.repository.CartItemRepository;
import com.finalbackend.tokoshop.repository.CartRepository;
import com.finalbackend.tokoshop.repository.ProductRepository;
import com.finalbackend.tokoshop.repository.UserRepository;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")

public class CartItemController {
    
    @Autowired
    ProductRepository productRepository;
    
    @Autowired
    CartRepository cartRepository;
    
    @Autowired
    CartItemRepository cartItemRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@RequestBody CartItemModel cartItemModel, Principal principal){

        Cart cart = cartRepository.findById(cartItemModel.getCartId()).get();
        Product product = productRepository.findById(cartItemModel.getProductId()).get();
        
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setQuantity(cartItemModel.getQuantity());
        cartItem.setPrice(product.getPrice()*cartItemModel.getQuantity());
        cartItem.setProduct(product);
        cartItemRepository.save(cartItem);
        
        product.setStock(product.getStock()-cartItemModel.getQuantity());
        productRepository.save(product);
        
        
        
        return ResponseEntity.ok("Item has been added to cart ..");
    }
    
}