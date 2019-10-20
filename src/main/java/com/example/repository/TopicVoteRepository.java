package com.example.repository;

import com.example.model.Connection;
import com.example.model.Topic;
import com.example.model.TopicVote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicVoteRepository extends CrudRepository<TopicVote, Long> {

    TopicVote getByTopic(Topic topic);

    TopicVote getByTopicAndUser(Topic topic, Connection user);
}
