package com.example.repository;

import com.example.model.Connection;
import com.example.model.Marker;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarkerRepository extends CrudRepository<Marker, Long> {

    public List<Marker> findAllByUser(Connection user);
}
