package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.dto.FileDto;
import com.example.dto.TopicDto;
import com.example.dto.TopicTemplate;
import com.example.model.*;
import com.example.repository.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class TopicController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CustomFileRepository fileRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicVoteRepository topicVoteRepository;

    @PostMapping("/add/topic")
    public ResponseEntity<?> addTopicToForum(@RequestBody TopicDto topicDto) {
        String jwtToken = topicDto.getJwtToken();

        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
            Long userID = (Long) claimsFromJwt.get("userID");

            Connection foundedUser = connectionRepository.findByUserID(userID);

            if(foundedUser != null) {
                Topic topic = new Topic();

                topic.setTitle(topicDto.getTitle());
                topic.setMessage(topicDto.getMessage());
                topic.setLanguageForum(topicDto.getLanguageForum());
                topic.setLikes(0);
                topic.setUser(foundedUser);
                topic.setDisplays(0);
                topic.setCreatedAt(new Date());

                Topic savedTopic = topicRepository.save(topic);

                if(topicDto.getFiles().size() != 0) {
                    for(FileDto element: topicDto.getFiles()) {
                        CustomFile file = new CustomFile();

                        file.setName(element.getName());
                        file.setObjectId(element.getObjectId());
                        file.setSize(element.getSize());
                        file.setTopic(savedTopic);
                        file.setType(element.getType());
                        file.setUrl(element.getUrl());

                        fileRepository.save(file);
                    }
                }
                return new ResponseEntity<Void>(HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/topics/all")
    public ResponseEntity<?> getAllTopics() {
        return new ResponseEntity<Iterable<Topic>>(topicRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/get/topic/posts/{topicId}")
    public ResponseEntity<?> getTopicById(@PathVariable Long topicId) {
        Topic topic = topicRepository.getById(topicId);

        int displays = topic.getDisplays() + 1;
        topic.setDisplays(displays);
        topicRepository.save(topic);

        List<CustomFile> customFiles = fileRepository.getAllByTopic(topic);
        topic.setFiles(customFiles);

        List<Post> posts = postRepository.getAllByTopicOrderByCreatedAtAsc(topic);
        for(Post post: posts) {
            List<CustomFile> postFiles = fileRepository.getAllByPost(post);
            post.setFiles(postFiles);
        }

        topic.setPosts(posts);
        return new ResponseEntity<Topic>(topic, HttpStatus.OK);
    }

    @GetMapping("/get/topic/number/comments/{topicId}/{jwtToken}")
    public ResponseEntity<?> fetchNumberOfComments(@PathVariable Long topicId, @PathVariable String jwtToken) {
        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
            Long userID = (Long) claimsFromJwt.get("userID");

            Connection foundedUser = connectionRepository.findByUserID(userID);

            Topic topic = topicRepository.getById(topicId);
            List<Post> posts = postRepository.getAllByTopic(topic);

            TopicVote topicVote = topicVoteRepository.getByTopicAndUser(topic, foundedUser);

            TopicTemplate topicTemplate = null;
            if(topicVote == null) {
                topicTemplate = new TopicTemplate(topic.getDisplays(), posts.size(), topic.getLikes(), 0);
            } else {
                topicTemplate = new TopicTemplate(topic.getDisplays(), posts.size(), topic.getLikes(), topicVote.getVote());
            }

            return new ResponseEntity<TopicTemplate>(topicTemplate, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/topic/likes/vote/{topicId}/{jwtToken}")
    public ResponseEntity<?> voteForTopic(@RequestBody boolean isUpVoted, @PathVariable Long topicId, @PathVariable String jwtToken) {
        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
            Long userID = (Long) claimsFromJwt.get("userID");

            Connection foundedUser = connectionRepository.findByUserID(userID);

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
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
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


