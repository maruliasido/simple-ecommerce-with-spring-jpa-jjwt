package com.finalbackend.tokoshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import com.finalbackend.tokoshop.TokoshopStorefrontApplication;
import com.finalbackend.tokoshop.entity.Cart;
import com.finalbackend.tokoshop.entity.CartItem;
import com.finalbackend.tokoshop.entity.User;
import com.finalbackend.tokoshop.model.Order;
import com.finalbackend.tokoshop.repository.CartItemRepository;
import com.finalbackend.tokoshop.repository.CartRepository;
import com.finalbackend.tokoshop.repository.UserRepository;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CartController {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_PROCESSED = "PROCESSED";

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkOut(Principal principal) throws Exception {

        User loggedInUser = userRepository.getUserByUsername(principal.getName());
        Cart wantToUseCart = cartRepository.findCartByStatusAndUser(STATUS_ACTIVE, loggedInUser);
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(wantToUseCart.getId());
        List<Integer> ix = new ArrayList<>();
        for (CartItem ci : cartItems) {
            ix.add(ci.getPrice());
        }
        Integer totalPrice = ix.stream().mapToInt(Integer::intValue).sum();

        List<CartItem> ci = cartItemRepository.findAllByCartId(wantToUseCart.getId());

        if (wantToUseCart != null) {
            wantToUseCart.setStatus(STATUS_PROCESSED);
            cartRepository.save(wantToUseCart);

            Order order = new Order();
            ObjectMapper objectMapper = new ObjectMapper();
            order.setUserId(loggedInUser.getId().toString());

            List<String> orderList = new ArrayList<>();
            for (CartItem cis: cartItems){
                orderList.add(cis.getProduct().getName());
            }
            
            order.setCartItem(orderList);
            order.setTotalPrice(totalPrice.toString());
            order.setCartId(wantToUseCart.getId().toString());

            String json = objectMapper.writeValueAsString(order);
            rabbitTemplate.convertAndSend(TokoshopStorefrontApplication.TOPIC_EXCHANGE_NAME,
                    "tokoshop.checkout", json);

        } else {
            return ResponseEntity.badRequest().body("You don't have an active cart, please make the new one..");
        }

        return ResponseEntity.ok("Item has been checkout");
    }

    @PostMapping("/cart")
    public ResponseEntity<String> createCart(Principal principal) {

        User loggedinUser = userRepository.getUserByUsername(principal.getName());
        Cart cart = new Cart();

        cart.setUser(loggedinUser);

        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();
        Date transactionDate = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        cart.setTransactionDate(transactionDate);

        cart.setStatus(STATUS_ACTIVE);

        cartRepository.save(cart);

        return ResponseEntity.ok("Cart has been created");
    }

}
