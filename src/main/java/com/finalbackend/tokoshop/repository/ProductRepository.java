
package com.finalbackend.tokoshop.repository;

import com.finalbackend.tokoshop.entity.Product;
import com.finalbackend.tokoshop.entity.User;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;


public interface ProductRepository extends CrudRepository<Product, Integer>{
    
//    public Product findAllByUser(User user);
    @Override
    @Cacheable(value = "getUserProducts")
    public Iterable<Product> findAll();
    

}
