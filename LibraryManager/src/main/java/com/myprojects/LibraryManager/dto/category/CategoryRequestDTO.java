package com.myprojects.LibraryManager.dto.category;

import com.myprojects.LibraryManager.entities.Category;

public class CategoryRequestDTO {
    private long id;
    private String name;

    public CategoryRequestDTO() {

    }

    public CategoryRequestDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryRequestDTO(Category category) {
        id = category.getId();
        name = category.getName();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
