package com.example.repository;

import com.example.model.Connection;
import com.example.model.Marker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MarkerRepository extends CrudRepository<Marker, Long> {

    public List<Marker> findAllByUser(Connection user);

    public Marker findByIdAndUser(Long id, Connection user);

    @Transactional
    void deleteByIdAndUser(Long id, Connection user);
}
