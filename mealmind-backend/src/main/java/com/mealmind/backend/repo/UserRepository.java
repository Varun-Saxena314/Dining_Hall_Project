package com.mealmind.backend.repo;

import com.mealmind.backend.entity.User; //import user class
import java.util.Optional; //may return a user
import org.springframework.data.jpa.repository.JpaRepository; //CRUD operations

public interface UserRepository extends JpaRepository<User, Long> { //extend JPARepo class
    Optional<User> findByUsername(String username); //function to find user by username
    boolean existsByUsername(String username); //function to check if user exists by username
}
