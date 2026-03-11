//package com.restaurant.billing.repository;
//
//import com.restaurant.billing.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//    
//    Optional<User> findByUsername(String username);
//    
//    Optional<User> findByEmail(String email);
//    
//    Optional<User> findByUsernameOrEmail(String username, String email);
//    
//    boolean existsByUsername(String username);
//    
//    boolean existsByEmail(String email);
//    
//    @Modifying
//    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
//    void updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);
//    
//    @Query("SELECT u FROM User u WHERE u.username = :username AND u.active = true")
//    Optional<User> findActiveByUsername(@Param("username") String username);
//    
//    @Query("SELECT u FROM User u WHERE u.email = :email AND u.active = true")
//    Optional<User> findActiveByEmail(@Param("email") String email);
//}




package com.restaurant.billing.repository;

import com.restaurant.billing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId,
                         @Param("lastLogin") LocalDateTime lastLogin);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.active = true")
    Optional<User> findActiveByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.active = true")
    Optional<User> findActiveByEmail(@Param("email") String email);
}
