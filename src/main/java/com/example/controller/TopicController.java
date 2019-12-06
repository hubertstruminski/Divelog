package com.example.controller;

import com.example.dto.FileDto;
import com.example.dto.TopicDto;
import com.example.dto.TopicTemplate;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.ClaimsConverter;
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
    private TopicRepository topicRepository;

    @Autowired
    private CustomFileRepository fileRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TopicVoteRepository topicVoteRepository;

    @Autowired
    private ClaimsConverter claimsConverter;

    @PostMapping("/add/topic")
    public ResponseEntity<?> addTopicToForum(@RequestBody TopicDto topicDto) {
        String jwtToken = topicDto.getJwtToken();
        Connection foundedUser = claimsConverter.findUser(jwtToken);

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
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/topics/all")
    public ResponseEntity<?> getAllTopics() {
        return new ResponseEntity<List<Topic>>(topicRepository.findAllAndOrderByCreatedAtAsc(), HttpStatus.OK);
    }

    @GetMapping("/get/topic/posts/{topicId}")
    public ResponseEntity<?> getTopicWithPostsById(@PathVariable Long topicId) {
        Topic topic = assignFilesToTopic(topicId, false);

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
        Connection foundedUser = claimsConverter.findUser(jwtToken);

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

    @GetMapping("/get/topic/{topicId}")
    public ResponseEntity<?> getTopicById(@PathVariable Long topicId) {
        Topic topic = assignFilesToTopic(topicId, true);
        if(topic == null) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Topic>(topic, HttpStatus.OK);
    }

    @PutMapping("/update/topic/{topicId}/{jwtToken}")
    public ResponseEntity<?> updateTopicById(@RequestBody TopicDto topicDto, @PathVariable Long topicId,
                                             @PathVariable String jwtToken) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);

        if(foundedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        Topic topic = topicRepository.getByIdAndUser(topicId, foundedUser);

        if(topic != null) {
            topic.setTitle(topicDto.getTitle());
            topic.setMessage(topicDto.getMessage());

            for(FileDto fileDto: topicDto.getFiles()) {
                CustomFile file = new CustomFile();

                file.setPost(null);
                file.setObjectId(fileDto.getObjectId());
                file.setSize(fileDto.getSize());
                file.setTopic(topic);
                file.setType(fileDto.getType());
                file.setUrl(fileDto.getUrl());
                file.setName(fileDto.getName());

                fileRepository.save(file);
            }

            topicRepository.save(topic);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/top/topics/all")
    public ResponseEntity<?> getAllTopTopics() {
        return new ResponseEntity<List<Topic>>(topicRepository.findAllAndOrderByLikesDesc(), HttpStatus.OK);
    }

    private Topic assignFilesToTopic(Long topicId, boolean isUpdating) {
        Topic topic = topicRepository.getById(topicId);

        if(topic == null) {
            return null;
        }

        if(!isUpdating) {
            int displays = topic.getDisplays() + 1;
            topic.setDisplays(displays);
            topicRepository.save(topic);
        }

        List<CustomFile> customFiles = fileRepository.getAllByTopic(topic);
        topic.setFiles(customFiles);

        return topic;
    }
}


