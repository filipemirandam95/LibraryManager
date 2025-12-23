package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.category.CategoryRequestDTO;
import com.myprojects.LibraryManager.dto.category.CategoryResponseDTO;
import com.myprojects.LibraryManager.entities.Category;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.CategoryRepository;
import com.myprojects.LibraryManager.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    private long validCategoryId;
    private long invalidCategoryId;
    private long dependentCategoryId;
    private Category validCategory;
    private CategoryRequestDTO validCategoryRequestDTO;
    private CategoryResponseDTO validCategoryResponseDTO;

    @BeforeEach
    void setUp() {
        validCategoryId = 1L;
        invalidCategoryId = 900L;
        validCategory = Factory.createCategory(validCategoryId);
        validCategoryRequestDTO = Factory.createCategoryRequestDTO(validCategoryId);
        validCategoryResponseDTO = Factory.createCategoryResponseDTO(validCategoryId);
    }

    @Test
    void findByIdShouldReturnCategoryResponseDTOWhenIdExists() {
        when(categoryRepository.findById(validCategoryResponseDTO.getId())).thenReturn(Optional.ofNullable(validCategory));

        CategoryResponseDTO categoryResponseDTO = categoryService.findById(validCategoryResponseDTO.getId());

        assertNotNull(categoryResponseDTO);
        verify(categoryRepository).findById(validCategoryResponseDTO.getId());

    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(categoryRepository).findById(invalidCategoryId);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(invalidCategoryId));

        verify(categoryRepository).findById(invalidCategoryId);
    }

    @Test
    void insertShouldReturnCategoryResponseDTOWhenCategoryIsNew() {
        when(categoryRepository.save(any(Category.class))).thenReturn(validCategory);

        CategoryResponseDTO categoryResponseDTO = categoryService.insert(validCategoryRequestDTO);

        assertNotNull(categoryResponseDTO);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateShouldReturnCategoryResponseDTOWhenCategoryRequestDTOisValid() {
        when(categoryRepository.getReferenceById(validCategoryRequestDTO.getId())).thenReturn((validCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(validCategory);

        CategoryResponseDTO categoryResponseDTO = categoryService.update(validCategoryId, validCategoryRequestDTO);

        assertNotNull(categoryResponseDTO);
        verify(categoryRepository).getReferenceById(validCategoryRequestDTO.getId());
        verify(categoryRepository).save(any(Category.class));

    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenCategoryIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(categoryRepository).getReferenceById(invalidCategoryId);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.update(invalidCategoryId, validCategoryRequestDTO));

        verify(categoryRepository).getReferenceById(invalidCategoryId);
        verify(categoryRepository, times(0)).save(any(Category.class));

    }

    @Test
    void deleteShouldDoNothingWhenCategoryIdExists() {
        when(categoryRepository.existsById(validCategoryRequestDTO.getId())).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(validCategoryRequestDTO.getId());

        assertDoesNotThrow(() -> categoryService.delete(validCategoryRequestDTO.getId()));

        verify(categoryRepository).existsById(validCategoryRequestDTO.getId());
        verify(categoryRepository).deleteById(validCategoryRequestDTO.getId());
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenCategoryIdDoesNotExist() {
        doThrow(ResourceNotFoundException.class).when(categoryRepository).existsById(invalidCategoryId);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(invalidCategoryId));

        verify(categoryRepository).existsById(invalidCategoryId);
        verify(categoryRepository, times(0)).deleteById(invalidCategoryId);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenCategoryHasDependencies() {
        when(categoryRepository.existsById(dependentCategoryId)).thenReturn(true);
        doThrow(DatabaseException.class).when(categoryRepository).deleteById(dependentCategoryId);

        assertThrows(DatabaseException.class, () -> categoryService.delete(dependentCategoryId));

        verify(categoryRepository).existsById(dependentCategoryId);
        verify(categoryRepository).deleteById(dependentCategoryId);
    }
}
