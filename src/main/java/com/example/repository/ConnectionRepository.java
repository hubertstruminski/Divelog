package com.example.repository;

import com.example.enums.Provider;
import com.example.model.Connection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ConnectionRepository extends CrudRepository<Connection, Long> {

    @Query("SELECT c FROM Connection c WHERE c.email = ?1")
    public Connection findByEmail(String email);

    public Connection findByUserIDAndEmailAndAuthenticated(Long userID, String email, boolean isAuthenticated);

    public Connection findByUserIDAndEmailAndProviderId(Long userID, String email, String provider);
}
