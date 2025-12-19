package com.myprojects.LibraryManager.repositories;

import com.myprojects.LibraryManager.entities.Fine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine,Long> {
    @EntityGraph(attributePaths = {"loan"})
    Page<Fine> findAll(Pageable pageable);

    //  @Query("SELECT f FROM Fine f JOIN FETCH f.loan l JOIN FETCH l.user u JOIN FETCH l.book b")
   // Page<Fine> findAll(Pageable pageable);

}
