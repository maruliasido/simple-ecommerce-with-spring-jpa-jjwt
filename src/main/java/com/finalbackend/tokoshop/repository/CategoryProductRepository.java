/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.finalbackend.tokoshop.repository;

import com.finalbackend.tokoshop.entity.Product;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface CategoryProductRepository extends PagingAndSortingRepository<Product, Integer> {
    public List<Product> findByDescriptionContains(String description, Pageable pageable);
}
