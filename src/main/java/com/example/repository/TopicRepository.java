package com.example.repository;

import com.example.model.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {

    Topic getById(Long id);
}
