package com.finalbackend.tokoshop.controller;

import com.finalbackend.tokoshop.entity.Product;
import com.finalbackend.tokoshop.repository.SearchProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchProductController {
    
    @Autowired
    SearchProductRepository searchProductRepository;
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(
            @RequestParam String q,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort) {

        Pageable pageable;

        if (sort.equals("PRICE_ASC")) {
            pageable = PageRequest.of(page-1, size, Sort.by("price"));
        } else if (sort.equals("PRICE_DESC")) {
            pageable = PageRequest.of(page-1, size, Sort.Direction.DESC, "price");
        } else {
            pageable = PageRequest.of(page-1, size);

        }
        
        List<Product> products = searchProductRepository.findByNameContains(q, pageable);
        
        for (Product product: products){
            product.setUser(null);
        }
        
        return ResponseEntity.ok(products);
    }

    
}
