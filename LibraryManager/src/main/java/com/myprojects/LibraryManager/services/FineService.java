package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.fine.FineRequestDTO;
import com.myprojects.LibraryManager.dto.fine.FineResponseDTO;
import com.myprojects.LibraryManager.entities.Fine;
import com.myprojects.LibraryManager.entities.Loan;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.repositories.FineRepository;
import com.myprojects.LibraryManager.repositories.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FineService {

    @Autowired
    private FineRepository fineRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public FineResponseDTO findById(Long id) {
        Fine fine = fineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        authService.validateSelfOrAdmin(fine.getLoan().getUser().getId());
        return new FineResponseDTO(fine);
    }

    @Transactional(readOnly = true)
    public Page<FineResponseDTO> findAll(Pageable pageable) {
        Page<Fine> result = fineRepository.findAll(pageable);
        return result.map(x -> new FineResponseDTO(x));
    }

    @Transactional
    public FineResponseDTO insert(FineRequestDTO fineRequestDTO) {

        Loan loan = loanRepository.findById(fineRequestDTO.getLoanId())
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));
        Fine fine = new Fine();
        DTOtoModel(fineRequestDTO, fine);
        fine.setLoan(loan);
        fine = fineRepository.save(fine);
        return new FineResponseDTO(fine);

    }

    @Transactional
    public FineResponseDTO update(Long id, FineRequestDTO fineRequestDTO) {
        try {
            Fine fine = fineRepository.getReferenceById(id);
            DTOtoModel(fineRequestDTO, fine);
            fine = fineRepository.save(fine);
            return new FineResponseDTO(fine);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");

        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!fineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        try {
            fineRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure");

        }
    }

    private void DTOtoModel(FineRequestDTO fineRequestDTO, Fine fine) {
        if (fineRequestDTO.getAmount() != null) { //If the field is null it means that this field will not be changed.
            fine.setAmount(fineRequestDTO.getAmount());
        }
        if (fineRequestDTO.getAppliedAt() != null) {
            fine.setAppliedAt(fineRequestDTO.getAppliedAt());
        }
        if (fineRequestDTO.getPaidAt() != null) {
            fine.setPaidAt(fineRequestDTO.getPaidAt());
        }
        if (fineRequestDTO.isPaid() != null) {
            fine.setPaid(fineRequestDTO.isPaid());
        }
    }

}
