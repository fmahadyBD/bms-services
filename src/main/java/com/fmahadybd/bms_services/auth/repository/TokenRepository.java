package com.fmahadybd.bms_services.auth.repository;

import com.fmahadybd.bms_services.auth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    // existing query — keep as-is
    @Query("""
        SELECT t FROM Token t
        WHERE t.userId = :userId
        AND t.userType = :userType
        AND t.expired = false
        AND t.revoked = false
    """)
    List<Token> findAllValidTokensByUser(
            @Param("userId") Long userId,
            @Param("userType") String userType
    );

    // FIX: new query that excludes the freshly saved token
    @Query("""
        SELECT t FROM Token t
        WHERE t.userId = :userId
        AND t.userType = :userType
        AND t.expired = false
        AND t.revoked = false
        AND t.token <> :excludeToken
    """)
    List<Token> findAllValidTokensByUserExcluding(
            @Param("userId") Long userId,
            @Param("userType") String userType,
            @Param("excludeToken") String excludeToken
    );
}