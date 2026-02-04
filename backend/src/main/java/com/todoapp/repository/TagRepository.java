package com.todoapp.repository;

import com.todoapp.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    Optional<Tag> findByNameIgnoreCase(String name);
    List<Tag> findByNameContainingIgnoreCase(String name);
}
