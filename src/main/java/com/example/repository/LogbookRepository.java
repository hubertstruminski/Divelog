package com.example.repository;

import com.example.model.Connection;
import com.example.model.Logbook;
import com.example.model.Marker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogbookRepository extends CrudRepository<Logbook, Long> {

    Logbook findByMarker(Marker marker);

    List<Logbook> findAllByUser(Connection user);

    Logbook findByIdAndUser(Long id, Connection user);
}
