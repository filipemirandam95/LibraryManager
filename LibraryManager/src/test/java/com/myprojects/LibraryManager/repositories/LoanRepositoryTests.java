package com.myprojects.LibraryManager.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
public class LoanRepositoryTests {

    @Autowired
    private LoanRepository loanRepository;

    private LocalDate validDateThreshold;

    @BeforeEach
    public void setUp() {
        validDateThreshold = LocalDate.now().minusDays(7);
    }

    public void findAllOpenedLoansShouldReturnListWhenValidLocalDate() {



    }
}
