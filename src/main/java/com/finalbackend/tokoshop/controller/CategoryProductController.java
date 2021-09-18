package com.finalbackend.tokoshop.controller;

import com.finalbackend.tokoshop.entity.Product;
import com.finalbackend.tokoshop.repository.CategoryProductRepository;
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
public class CategoryProductController {

    @Autowired
    CategoryProductRepository categoryProductRepository;
    @GetMapping("/category")
    public ResponseEntity<List<Product>> categoryProduct(
            @RequestParam String category,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort) {

        Pageable pageable;

        if (sort.equals("PRICE_ASC")) {
            pageable = PageRequest.of(page-1, size, Sort.by("price"));
        } else if (sort.equals("PRICE_DESC")) {
            pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "price"));
        } else {
            pageable = PageRequest.of(page-1, size);
        }

        List<Product> products = categoryProductRepository.findByDescriptionContains(category, pageable);

        for (Product product : products) {
            product.setUser(null);
        }

        return ResponseEntity.ok(products);
    }

}
