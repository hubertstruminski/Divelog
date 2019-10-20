package com.example.controller;

import com.example.model.Connection;
import com.example.model.Topic;
import com.example.model.TopicVote;
import com.example.repository.TopicRepository;
import com.example.repository.TopicVoteRepository;
import com.example.service.ClaimsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TopicVoteController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicVoteRepository topicVoteRepository;

    @Autowired
    private ClaimsConverter claimsConverter;

    @PutMapping("/topic/likes/vote/{topicId}/{jwtToken}")
    public ResponseEntity<?> voteForTopic(@RequestBody boolean isUpVoted, @PathVariable Long topicId, @PathVariable String jwtToken) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        Topic topic = topicRepository.getById(topicId);
        TopicVote topicVote = topicVoteRepository.getByTopic(topic);

        if(topic == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        if(topicVote != null) {
            setVoteForTopicVote(isUpVoted, topicVote, topic);
            topicVote.setUser(foundedUser);
            topicVoteRepository.save(topicVote);

            return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
            TopicVote newTopicVote = new TopicVote();

            setVoteForTopicVote(isUpVoted, newTopicVote, topic);
            newTopicVote.setTopic(topic);
            newTopicVote.setUser(foundedUser);
            topicVoteRepository.save(newTopicVote);

            return new ResponseEntity<Void>(HttpStatus.OK);
        }
    }

    private void setVoteForTopicVote(boolean isUpVoted, TopicVote topicVote, Topic topic) {
        int likes = topic.getLikes();
        if(isUpVoted) {
            if(topicVote.getVote() == 0) {
                topicVote.setVote(1);
            } else if(topicVote.getVote() == -1) {
                topicVote.setVote(0);
            }
            likes++;
        } else {
            if(topicVote.getVote() == 0) {
                topicVote.setVote(-1);
            } else if(topicVote.getVote() == 1) {
                topicVote.setVote(0);
            }
            likes--;
        }
        topic.setLikes(likes);
        topicRepository.save(topic);
    }
}
