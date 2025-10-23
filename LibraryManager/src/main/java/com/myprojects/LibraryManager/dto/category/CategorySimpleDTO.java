package com.myprojects.LibraryManager.dto.category;

import com.myprojects.LibraryManager.entities.Category;

public class CategorySimpleDTO {
    private long id;
    private String name;

    public CategorySimpleDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
