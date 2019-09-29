package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.dto.FileDto;
import com.example.dto.TopicDto;
import com.example.model.Connection;
import com.example.model.CustomFile;
import com.example.model.Topic;
import com.example.model.Post;
import com.example.repository.ConnectionRepository;
import com.example.repository.CustomFileRepository;
import com.example.repository.PostRepository;
import com.example.repository.TopicRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

        List<CustomFile> customFiles = fileRepository.getAllByTopic(topic);
        topic.setFiles(customFiles);

        List<Post> posts = postRepository.getAllByTopic(topic);
        for(Post post: posts) {
            List<CustomFile> postFiles = fileRepository.getAllByPost(post);
            post.setFiles(postFiles);
        }

        topic.setPosts(posts);
        return new ResponseEntity<Topic>(topic, HttpStatus.OK);
    }
}
