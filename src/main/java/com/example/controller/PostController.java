package com.example.controller;

import com.example.config.JwtTokenProvider;
import com.example.dto.FileDto;
import com.example.dto.PostDto;
import com.example.model.*;
import com.example.repository.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CustomFileRepository fileRepository;

    @PostMapping("/add/post")
    public ResponseEntity<?> addPost(@RequestBody PostDto postDto) {
        String jwtToken = postDto.getJwtToken();

        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
            Long userID = (Long) claimsFromJwt.get("userID");

            Connection foundedUser = connectionRepository.findByUserID(userID);
            Topic topic = topicRepository.getById(postDto.getTopicId());

            if(foundedUser != null && topic != null) {
                Post post = new Post();

                post.setMessage(postDto.getMessage());
                post.setTopic(topic);
                post.setCreatedAt(new Date());
                post.setUser(foundedUser);

                Post savedPost = postRepository.save(post);


                if(postDto.getFiles().size() != 0) {
                    for(FileDto element: postDto.getFiles()) {
                        CustomFile file = new CustomFile();

                        file.setName(element.getName());
                        file.setUrl(element.getUrl());
                        file.setType(element.getType());
                        file.setTopic(topic);
                        file.setSize(element.getSize());
                        file.setObjectId(element.getObjectId());
                        file.setPost(savedPost);

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

    @DeleteMapping("/delete/post/{postId}/{jwtToken}")
    public ResponseEntity<?> deletePostById(@PathVariable Long postId, @PathVariable String jwtToken) {

        if(jwtTokenProvider.validateToken(jwtToken)) {
            Claims claimsFromJwt = jwtTokenProvider.getClaimsFromJwt(jwtToken);
            Long userID = (Long) claimsFromJwt.get("userID");

            Connection foundedUser = connectionRepository.findByUserID(userID);
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
            } else {
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
}
