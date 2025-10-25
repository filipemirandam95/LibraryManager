package com.myprojects.LibraryManager.repositories;

import com.myprojects.LibraryManager.entities.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author> findByName(String name); // Used to check if the Author already exists.

    @EntityGraph(attributePaths = {"books"})
    Page<Author> findAll(Pageable pageable);

  //  @Query("SELECT a FROM Author a JOIN FETCH a.books")
  //  Page<Author> findAllCustom(Pageable pageable);

}
