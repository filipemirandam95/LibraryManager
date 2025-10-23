package com.myprojects.LibraryManager.entities;

import com.myprojects.LibraryManager.dto.category.CategoryRequestDTO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToMany(mappedBy = "categories")
    private Set<Book> books = new HashSet<Book>();

    public Category() {

    }

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(CategoryRequestDTO categoryRequestDTO) {
        this.id = categoryRequestDTO.getId();
        this.name = categoryRequestDTO.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }
}
