package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.author.AuthorRequestDTO;
import com.myprojects.LibraryManager.dto.author.AuthorResponseDTO;
import com.myprojects.LibraryManager.entities.Author;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public AuthorResponseDTO findById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return new AuthorResponseDTO(author);
    }

    @Transactional(readOnly = true)
    public Page<AuthorResponseDTO> findAll(Pageable pageable) {
        Page<Author> result = authorRepository.findAll(pageable);
        return result.map(x -> new AuthorResponseDTO(x));
    }

    @Transactional
    public AuthorResponseDTO insert(AuthorRequestDTO authorRequestDTO) {
        Author author = new Author();
        DTOToModel(authorRequestDTO, author);
        author = authorRepository.save(author);
        return new AuthorResponseDTO(author);
    }

    @Transactional
    public AuthorResponseDTO update(Long id, AuthorRequestDTO authorRequestDTO) {
        try {
            Author author = authorRepository.getReferenceById(id);
            DTOToModel(authorRequestDTO, author);
            author = authorRepository.save(author);
            return new AuthorResponseDTO(author);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        try {
            authorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure");

        }

    }

    private void DTOToModel(AuthorRequestDTO authorRequestDTO, Author author) {
        if (author.getName() != null) {
            author.setName(authorRequestDTO.getName());
        }
        if (authorRequestDTO.getBirthDate() != null) {
            author.setBirthDate(authorRequestDTO.getBirthDate());
        }
    }
}
