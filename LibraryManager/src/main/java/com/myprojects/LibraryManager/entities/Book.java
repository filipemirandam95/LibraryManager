package com.myprojects.LibraryManager.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String isbn;
    @JsonProperty("year")
    @Column(name = "book_year")
    private int year;
    private String edition;
    private int totalCopies;
    private int availableCopies;
    @OneToMany(mappedBy = "book")
    private Set<Loan> loans;
    @OneToMany(mappedBy = "book")
    private Set<Reservation> reservation;
    @ManyToMany
    @JoinTable(name = "tb_book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<Author>();
    @ManyToMany
    @JoinTable(name = "tb_book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<Category>();

    public Book() {

    }

    public Book(long id) {
        this.id = id;

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {

        return year;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setYear(int year) {

        this.year = year;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public void setReservation(Set<Reservation> reservation) {
        this.reservation = reservation;
    }


    public void setLoans(Set<Loan> loans) {
        this.loans = loans;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Set<Reservation> getReservation() {
        return reservation;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

}
