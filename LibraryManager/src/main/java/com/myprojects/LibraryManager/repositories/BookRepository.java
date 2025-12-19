package com.myprojects.LibraryManager.repositories;

import com.myprojects.LibraryManager.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    @EntityGraph(attributePaths = {"authors", "categories"})
    Page<Book> findAll(Pageable pageable);
    @Query("""
    SELECT DISTINCT b FROM Book b
    LEFT JOIN FETCH b.authors a
    LEFT JOIN FETCH b.categories c
    WHERE
        (:title = true AND LOWER(b.title) LIKE CONCAT('%', LOWER(:query), '%'))
        OR (:author = true AND LOWER(a.name) LIKE CONCAT('%', LOWER(:query), '%'))
        OR (:isbn = true AND LOWER(b.isbn) LIKE CONCAT('%', LOWER(:query), '%'))
""")
    List<Book> findAllBooksCustom(@Param("query") String query,
                                  @Param("title") boolean title,
                                  @Param("author") boolean author,
                                  @Param("isbn") boolean isbn);


}
