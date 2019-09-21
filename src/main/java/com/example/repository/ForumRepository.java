package com.example.repository;

import com.example.model.LanguageForum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends CrudRepository<LanguageForum, Long> {
}
