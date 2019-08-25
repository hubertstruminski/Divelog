package com.example.repository;

import com.example.model.Connection;
import com.example.model.Marker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkerRepository extends CrudRepository<Marker, Long> {

    public List<Marker> findAllByUser(Connection user);

    void deleteById(Long id);
}
