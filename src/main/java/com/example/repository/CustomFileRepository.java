package com.example.repository;

import com.example.model.CustomFile;
import com.example.model.Post;
import com.example.model.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomFileRepository extends CrudRepository<CustomFile, Long> {

    List<CustomFile> getAllByTopic(Topic topic);

    List<CustomFile> getAllByPost(Post post);
}
