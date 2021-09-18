
package com.finalbackend.tokoshop.repository;

import com.finalbackend.tokoshop.entity.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer> {
    
    public User getUserByUsername(String username);
    
}
