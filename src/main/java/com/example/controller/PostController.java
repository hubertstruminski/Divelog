package com.example.controller;

import com.example.dto.FileDto;
import com.example.dto.PostDto;
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
public class PostController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CustomFileRepository fileRepository;

    @Autowired
    private ClaimsConverter claimsConverter;

    @PostMapping(value = "/add/post", produces = "application/json")
    public ResponseEntity<?> addPost(@RequestBody PostDto postDto) {
        String jwtToken = postDto.getJwtToken();

        Connection foundedUser = claimsConverter.findUser(jwtToken);
        Topic topic = topicRepository.getById(postDto.getTopicId());

        if(foundedUser != null && topic != null) {
            Post post = new Post();

            post.setMessage(postDto.getMessage());
            post.setTopic(topic);
            post.setCreatedAt(new Date());
            post.setUpdatedAt(null);
            post.setUser(foundedUser);

            Post savedPost = postRepository.save(post);
            saveFiles(postDto, null, savedPost);

            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/delete/post/{postId}/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> deletePostById(@PathVariable Long postId, @PathVariable String jwtToken) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);
        Post post = postRepository.getByIdAndUser(postId, foundedUser);

        if(foundedUser != null && post != null) {
            List<CustomFile> customFiles = fileRepository.getAllByPost(post);
            if(customFiles.size() != 0) {
                for(CustomFile file: customFiles) {
                    fileRepository.delete(file);
                }
            }
            postRepository.deleteById(postId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/post/{postId}/{jwtToken}", produces = "application/json")
    public ResponseEntity<?> updatePostById(@RequestBody PostDto postDto, @PathVariable Long postId, @PathVariable String jwtToken) {
        Connection foundedUser = claimsConverter.findUser(jwtToken);
        Post post = postRepository.getByIdAndUser(postId, foundedUser);

        if(post != null && foundedUser != null) {
            post.setMessage(postDto.getMessage());
            post.setUpdatedAt(new Date());

            Post updatedPost = postRepository.save(post);

            saveFiles(postDto, null, updatedPost);

            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    private void saveFiles(PostDto postDto, Topic topic, Post post) {
        for(FileDto element: postDto.getFiles()) {
            CustomFile file = new CustomFile();

            file.setName(element.getName());
            file.setUrl(element.getUrl());
            file.setType(element.getType());
            file.setTopic(topic);
            file.setSize(element.getSize());
            file.setObjectId(element.getObjectId());
            file.setPost(post);

            fileRepository.save(file);
        }
    }
}
