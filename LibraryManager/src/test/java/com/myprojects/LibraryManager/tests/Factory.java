package com.myprojects.LibraryManager.tests;

import com.myprojects.LibraryManager.dto.author.AuthorRequestDTO;
import com.myprojects.LibraryManager.dto.author.AuthorResponseDTO;
import com.myprojects.LibraryManager.dto.book.BookRequestDTO;
import com.myprojects.LibraryManager.dto.book.BookResponseDTO;
import com.myprojects.LibraryManager.dto.category.CategoryRequestDTO;
import com.myprojects.LibraryManager.dto.category.CategoryResponseDTO;
import com.myprojects.LibraryManager.dto.fine.FineRequestDTO;
import com.myprojects.LibraryManager.dto.fine.FineResponseDTO;
import com.myprojects.LibraryManager.dto.loan.LoanRequestDTO;
import com.myprojects.LibraryManager.dto.loan.LoanResponseDTO;
import com.myprojects.LibraryManager.dto.reservation.ReservationRequestDTO;
import com.myprojects.LibraryManager.dto.reservation.ReservationResponseDTO;
import com.myprojects.LibraryManager.dto.user.UserRequestDTO;
import com.myprojects.LibraryManager.dto.user.UserResponseDTO;
import com.myprojects.LibraryManager.entities.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class Factory {

    public static User createUserOperator() {
        User user = createUser();
        user.setName("novoUser");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(createOperatorRole());
        user.setRoles(roleSet);
        return user;
    }

    public static User createUserOperator(long id) {
        User user = new User(id);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(createOperatorRole());
        user.setRoles(roleSet);
        return user;
    }

    public static User createUserAdministrator() {
        User user = new User(1L);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(createAdministratorRole());
        user.setRoles(roleSet);
        return user;
    }

    public static User createUserAdministrator(long id) {
        User user = new User(id);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(createAdministratorRole());
        user.setRoles(roleSet);
        return user;
    }

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("newEmail@email.com");
        user.setPassword("password123");
        user.setBirthDate(LocalDate.of(1980, 8, 9));
        return user;
    }

    public static User createUser(long id) {
        User user = new User(id);
        user.setEmail("newEmail@email.com");
        user.setPassword("password123");
        user.setBirthDate(LocalDate.of(1980, 8, 9));
        return user;
    }

    public static User createUser(String email, String password, LocalDate birthDate) {
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword(password);
        user.setBirthDate(birthDate);
        return user;
    }

    public static Role createOperatorRole() {
        Role operatorRole = new Role();
        operatorRole.setId(1L);
        operatorRole.setAuthority("ROLE_OPERATOR");
        return operatorRole;
    }

    public static Role createAdministratorRole() {
        Role operatorRole = new Role();
        operatorRole.setId(1L);
        operatorRole.setAuthority("ROLE_ADMIN");
        return operatorRole;
    }

    public static UserRequestDTO createUserRequestDTO() {
        UserRequestDTO userRequestDTO = new UserRequestDTO(createUserOperator());
        return userRequestDTO;
    }

    public static UserRequestDTO createUserRequestDTO(String email, String password, LocalDate birthDate) {
        UserRequestDTO user = new UserRequestDTO(createUser(email, password, birthDate));
        return user;
    }

    public static UserResponseDTO createUserResponseDTO() {
        UserResponseDTO userResponseDTO = new UserResponseDTO(createUserOperator());
        return userResponseDTO;
    }

    public static Reservation createReservation(Long id, Long userId, Long bookId, LocalDate reservationDate, ReservationStatus reservationStatus) {
        Reservation reservation = new Reservation(id, userId, bookId, reservationDate, reservationStatus);
        return reservation;
    }

    public static Reservation createReservationWithUserOperator() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(createUserOperator());
        reservation.setBook(new Book(1L));
        reservation.setReservationDate(LocalDate.of(2025, 9, 15));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservation;
    }

    public static Reservation createReservationWithUserOperator(Long id) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setUser(createUserOperator());
        reservation.setBook(new Book(1L));
        reservation.setReservationDate(LocalDate.of(2025, 9, 15));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservation;
    }

    public static Reservation createReservationWithUserOperator(long userId) {
        Reservation reservation = new Reservation();
        reservation.setId(userId + 1);
        reservation.setUser(createUserOperator(userId));
        reservation.setBook(new Book(1L));
        reservation.setReservationDate(LocalDate.of(2025, 9, 15));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservation;
    }

    public static ReservationRequestDTO createReservationRequestDTO() {
        ReservationRequestDTO reservation = new ReservationRequestDTO(1L, LocalDate.of(2025, 9, 15), 1L, 1L);
        return reservation;
    }

    public static ReservationRequestDTO createReservationRequestDTO(Long id) {
        ReservationRequestDTO reservation = new ReservationRequestDTO(id, LocalDate.of(2025, 9, 15), 1L, 1L);
        return reservation;
    }

    public static ReservationResponseDTO createReservationResponseDTO(Long id) {
        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO(createReservationWithUserOperator(id));
        return reservationResponseDTO;
    }

    public static Book createBookWithAvailableCopies() {
        Book book = new Book();
        book.setId(1L);
        book.setAvailableCopies(10);
        return book;
    }

    public static Book createBookWithoutAvailableCopies() {
        Book book = new Book();
        book.setId(1L);
        book.setAvailableCopies(0);
        return book;
    }

    public static Loan createLoanWithUserOperator() {
        Loan loan = new Loan();
        loan.setUser(createUserOperator());
        loan.setBook(createBookWithAvailableCopies());
        loan.setLoanDate(LocalDate.of(2025, 9, 18));
        return loan;
    }

    public static Loan createLoanFromLoanRequestDTO(LoanRequestDTO loanRequestDTO) {
        Loan loan = new Loan(loanRequestDTO);

        return loan;
    }

    public static LoanRequestDTO createLoanRequestDTO(User user, Book book) {
        LoanRequestDTO loanRequestDTO = new LoanRequestDTO(1L, LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 4), user.getId(), book.getId());
        return loanRequestDTO;
    }

    public static LoanRequestDTO createLoanRequestDTO(Long id) {
        User user = createUserOperator();
        Book book = createBookWithAvailableCopies();
        LoanRequestDTO loanRequestDTO = new LoanRequestDTO(id, LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 4), user.getId(), book.getId());
        return loanRequestDTO;
    }

    public static LoanResponseDTO createLoanResponseDTO(Long id) {
        Loan loan = createLoanWithUserOperator();
        loan.setId(id);
        LoanResponseDTO loanResponseDTO = new LoanResponseDTO(loan);
        return loanResponseDTO;
    }

    public static LoanRequestDTO createLoanRequestDTO(Long id, User user, Book book) {
        LoanRequestDTO loanRequestDTO = new LoanRequestDTO(id, LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 4), user.getId(), book.getId());
        return loanRequestDTO;
    }

    public static Fine createFine(Long id) {
        Loan loan = createLoanWithUserOperator();
        Fine fine = new Fine(id);
        fine.setPaid(false);
        fine.setAmount(BigDecimal.valueOf(9));
        fine.setAppliedAt(
                loan.getLoanDate()
                        .plusDays(10)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );
        fine.setLoan(loan);

        return fine;
    }

    public static Fine createFine(Long id, Loan loan) {
        Fine fine = new Fine(id);
        fine.setId(1L);
        fine.setPaid(false);
        fine.setAmount(BigDecimal.valueOf(9));
        fine.setAppliedAt(
                loan.getLoanDate()
                        .plusDays(10)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );
        fine.setLoan(loan);

        return fine;
    }

    public static FineRequestDTO createFineRequestDTO(Fine fine) {
        FineRequestDTO fineRequestDTO = new FineRequestDTO(fine);
        return fineRequestDTO;
    }

    public static FineRequestDTO createFineRequestDTO(Long loanId) {
        FineRequestDTO fineRequestDTO = new FineRequestDTO(loanId);
        return fineRequestDTO;
    }

    public static FineResponseDTO createFineResponseDTO(Long id) {
        Fine fine = createFine(id);
        FineResponseDTO fineResponseDTO = new FineResponseDTO(fine);
        return fineResponseDTO;
    }

    public static CategoryResponseDTO createCategoryResponseDTO(Long categoryId) {
        Category category = new Category(categoryId, "novaCategoria");
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO(category);
        return categoryResponseDTO;
    }

    public static Category createCategory(Long categoryId) {
        Category category = new Category(categoryId, "novaCategoria");
        return category;
    }

    public static CategoryRequestDTO createCategoryRequestDTO(Long categoryId) {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO(categoryId, "novaCategoria");
        return categoryRequestDTO;
    }

    public static Book createBook(Long id) {
        Book book = new Book(id);
        return book;
    }

    public static Book createCompleteBook(Long id) {
        Book book = new Book(id);
        book.getCategories().add(createCategory(1L));
        book.getAuthors().add(createAuthor(1L));
        book.setTitle("Valid Book Title"); // at least 3 chars
        book.setIsbn("1234567890"); // 10 chars, valid
        book.setYear(2023);
        book.setEdition("First");
        book.setTotalCopies(5);
        book.setAvailableCopies(5);

        return book;
    }

    public static BookResponseDTO createBookResponseDTO(Book book) {
        BookResponseDTO bookResponseDTO = new BookResponseDTO(book);
        return bookResponseDTO;
    }

    public static BookResponseDTO createBookResponseDTO(Long id) {
        Book book = new Book(id);
        BookResponseDTO bookResponseDTO = new BookResponseDTO(book);
        return bookResponseDTO;
    }

    public static BookRequestDTO createBookRequestDTO(Long id) {
        Book book = createCompleteBook(id);
        BookRequestDTO bookRequestDTO = new BookRequestDTO(book);
        return bookRequestDTO;
    }

    public static Author createAuthor(Long bookId) {
        Author author = new Author(bookId,"novoAutor",LocalDate.of(1990,1,1));
        return author;
    }

    public static AuthorRequestDTO createAuthorRequestDTO(Long id) {
        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO(id, "novoAutor");
        return authorRequestDTO;
    }
    public static AuthorResponseDTO createAuthorResponseDTO(Long id) {
        Author author = new Author(id);
        author.setName("novoAutor");
        AuthorResponseDTO authorResponseDTO = new AuthorResponseDTO(author);
        return authorResponseDTO;
    }


}
