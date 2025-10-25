package com.myprojects.LibraryManager.repositories;

import com.myprojects.LibraryManager.entities.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @EntityGraph(attributePaths = {"user", "book", "fine"})
    Page<Loan> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "book", "fine"})
    List<Loan> findAll();

    @EntityGraph(attributePaths = {"user", "book", "fine"})
    @Query(value = "SELECT l FROM Loan l WHERE l.loanDate >= :dateThreshold AND l.returnDate IS NULL")
    List<Loan> findAllOpenedLoans(@Param("dateThreshold") LocalDate dateThreshold);

    @EntityGraph(attributePaths = {"user", "book", "fine"})
    @Query(value = "SELECT l FROM Loan l WHERE l.loanDate < :loanThreshold AND l.returnDate IS NULL")
    List<Loan> findOverdueLoans(@Param("loanThreshold") LocalDate loanThreshold);

   // @Query(value = "SELECT b.title, COUNT(l.id) FROM Loan l JOIN l.book b " +
   //                "WHERE l.loanDate BETWEEN :startDate AND :endDate " +
   //                "GROUP BY b.title ORDER BY COUNT(l.id) DESC")
   // List<Loan> findMostLoanedBooksInPeriod(@Param("startDate") LocalDate startDate,
    //                                       @Param("endDate") LocalDate endDate, Pageable pageable);

//    @Query(value = "SELECT * FROM tb_loan l"+
//                   "WHERE l.return_date IS NOT NULL"+
//                   "AND l.return_date > DATEADD('DAY', 7, l.loan_date)", nativeQuery = true)
//    List<Loan> findReturnedLoansWithDelay(@Param("dateThreshold") LocalDate dateThreshold);




}
