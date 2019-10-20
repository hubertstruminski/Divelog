package com.example.repository;

import com.example.model.Connection;
import com.example.model.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {

    Topic getById(Long id);

    Topic getByIdAndUser(Long id, Connection user);

    @Query("SELECT t FROM Topic t ORDER BY t.createdAt ASC")
    List<Topic> findAllAndOrderByCreatedAtAsc();

    @Query("SELECT t FROM Topic t ORDER BY t.likes DESC")
    List<Topic> findAllAndOrderByLikesDesc();
}
