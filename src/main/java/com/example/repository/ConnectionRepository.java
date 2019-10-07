package com.example.repository;

import com.example.model.Connection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Repository
@Transactional
public interface ConnectionRepository extends CrudRepository<Connection, Long> {

    @Query("SELECT c FROM Connection c WHERE c.email = ?1")
    public Connection findByEmail(String email);

    public Connection findByUserIDAndEmailAndAuthenticated(BigInteger userID, String email, boolean isAuthenticated);

    public Connection findByUserIDAndEmailAndProviderId(BigInteger userID, String email, String provider);

    public Connection findByEmailAndAuthenticated(String email, boolean isAuthenticated);
}
