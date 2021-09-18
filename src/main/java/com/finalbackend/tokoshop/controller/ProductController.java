package com.finalbackend.tokoshop.controller;

import com.finalbackend.tokoshop.entity.Product;
import com.finalbackend.tokoshop.entity.User;
import com.finalbackend.tokoshop.model.Stock;
import com.finalbackend.tokoshop.repository.ProductRepository;
import com.finalbackend.tokoshop.repository.UserRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/product")
    public ResponseEntity<String> addProduct(@RequestBody Product product, Principal principal) {
        User loggedIn = userRepository.getUserByUsername(principal.getName());
        product.setUser(loggedIn);
        productRepository.save(product);

        return ResponseEntity.ok("Product has been added..");
    }

    @GetMapping("/product/id/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "id") Integer id) {
        Product product = productRepository.findById(id).get();
        product.getUser().setPassword(null);

        return ResponseEntity.ok(product);
    }

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getUserProducts(Principal principal) {

        User loggedInUser = userRepository.getUserByUsername(principal.getName());
        Iterable<Product> product = productRepository.findAll();
        List<Product> listProducts = StreamSupport.stream(product.spliterator(), false).collect(Collectors.toList());
        List<Product> userPro = new ArrayList<>();
        for (Product p : listProducts) {
            if (p.getUser().equals(loggedInUser)) {
                userPro.add(p);
            }
        }

        return ResponseEntity.ok(userPro);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable(name="id")Integer id,
            @RequestBody Product product, 
            Principal principal) {
        
        User loggedInUser = userRepository.getUserByUsername(principal.getName());
        Boolean isContinue = isThisProductTheirs(id, principal.getName());
        Product updatedProduct = productRepository.findById(id).get();
        if (isContinue) {
            updatedProduct.setId(id);
            updatedProduct.setUser(loggedInUser);
            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setStock(product.getStock());
            updatedProduct.setPrice(product.getPrice());
            productRepository.save(updatedProduct);
            
            return ResponseEntity.ok("Product has been updated..");
        }
        
        return ResponseEntity.badRequest().body("Invailid User parameter, you can't edit this product ..");

    }
    
    @PutMapping("/product/stock/{id}")
    public ResponseEntity<String> updateStock(@PathVariable (name="id") Integer id,@RequestBody Stock stock, Principal principal){
        
        Boolean isContinue = isThisProductTheirs(id, principal.getName());
        Product updatedStock = productRepository.findById(id).get();
        if(isContinue){
            updatedStock.setStock(stock.getStock());
            productRepository.save(updatedStock);
            return ResponseEntity.ok("Stock has been updated..");
        }
        
        return ResponseEntity.badRequest().body("Invailid User parameter, you can't edit stock of this product ..");
    }
    
    @DeleteMapping("/product/id/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable (name="id") Integer id, Principal principal){
        Boolean isContinue = isThisProductTheirs(id, principal.getName());
        Product toDelete = productRepository.findById(id).get();
        if(isContinue){
            productRepository.delete(toDelete);
            return ResponseEntity.ok("Product has been deleted ..");
        }
        
        
        return ResponseEntity.badRequest().body("Invailid User parameter, you can't delete this product..");
    }
    
    

    public Boolean isThisProductTheirs(Integer productID, String username) {
        Product product;
        User loggedInUser = userRepository.getUserByUsername(username);

        if (loggedInUser != null) {
            product = productRepository.findById(productID).get();
            if (product != null
                    && product.getUser().getId().equals(loggedInUser.getId())) {
                return true;
            }
        }
        return false;
    }

}
