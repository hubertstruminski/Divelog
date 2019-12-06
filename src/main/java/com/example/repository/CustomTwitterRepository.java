package com.example.repository;

import com.example.model.Connection;
import com.example.model.CustomTwitter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomTwitterRepository extends CrudRepository<CustomTwitter, Long> {

    CustomTwitter findByUser(Connection user);
}
