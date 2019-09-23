package com.example.repository;

import com.example.model.CustomFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFileRepository extends CrudRepository<CustomFile, Long> {
}
