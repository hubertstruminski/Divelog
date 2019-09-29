package com.example.repository;

import com.example.model.Connection;
import com.example.model.Post;
import com.example.model.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> getAllByTopic(Topic topic);

    Post getByIdAndUser(Long id, Connection user);
}
