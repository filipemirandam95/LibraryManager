package com.myprojects.LibraryManager.repositories;


import com.myprojects.LibraryManager.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name); // used on book insert

    @EntityGraph(attributePaths = {"books", "books.authors"})
    List<Category> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"books", "books.authors"})
    Page<Category> findAll(Pageable pageable);
}
