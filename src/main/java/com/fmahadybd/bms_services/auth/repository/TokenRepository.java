package com.fmahadybd.bms_services.auth.repository;

import com.fmahadybd.bms_services.auth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    
    Optional<Token> findByToken(String token);
    
    @Query("SELECT t FROM Token t WHERE t.userId = :userId AND t.userType = :userType AND t.expired = false AND t.revoked = false")
    List<Token> findAllValidTokensByUser(@Param("userId") Long userId, @Param("userType") String userType);
    
    @Query("SELECT t FROM Token t WHERE t.userId = :userId AND t.userType = :userType")
    List<Token> findAllByUserIdAndUserType(@Param("userId") Long userId, @Param("userType") String userType);
    
    void deleteByUserIdAndUserType(Long userId, String userType);
}