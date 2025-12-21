package com.myprojects.LibraryManager.services;

import com.myprojects.LibraryManager.dto.user.LoggedUserDTO;
import com.myprojects.LibraryManager.dto.user.UserRequestDTO;
import com.myprojects.LibraryManager.dto.user.UserResponseDTO;
import com.myprojects.LibraryManager.entities.Role;
import com.myprojects.LibraryManager.entities.User;
import com.myprojects.LibraryManager.exceptions.DatabaseException;
import com.myprojects.LibraryManager.exceptions.ForbiddenException;
import com.myprojects.LibraryManager.exceptions.ResourceNotFoundException;
import com.myprojects.LibraryManager.projections.UserDetailsProjection;
import com.myprojects.LibraryManager.repositories.LoanRepository;
import com.myprojects.LibraryManager.repositories.ReservationRepository;
import com.myprojects.LibraryManager.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        User loggedUser = authenticated();
        if (!loggedUser.hasRole("ROLE_ADMIN") && !loggedUser.getId().equals(user.getId())) {
            throw new ForbiddenException("Access denied");
        }
        return new UserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findByName(String name) {
        try {
            List<User> users = userRepository.findByNameContainingIgnoreCase(name);
            List<UserResponseDTO> usersResponseDTO = new ArrayList<>();
            for (User user : users) {
                usersResponseDTO.add(new UserResponseDTO(user));
            }
            return usersResponseDTO;
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {  // This query is purposely exaggerated
        Page<User> usersPage = userRepository.findAll(pageable);
        userRepository.findUserLoansReservations(usersPage.stream().collect(Collectors.toList()));
        return usersPage.map(user -> new UserResponseDTO(user));
    }

    @Transactional
    public UserResponseDTO insert(UserRequestDTO userRequestDTO) {
        try {
            User user = new User();
            DTOtoModel(userRequestDTO, user);
            user = userRepository.save(user);
            return new UserResponseDTO(user);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Email already exists: " + userRequestDTO.getEmail());
        }
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
        try {
            User user = userRepository.getReferenceById(id);
            DTOtoModel(userRequestDTO, user);
            user = userRepository.save(user);
            return new UserResponseDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure");
        }
    }

    private void DTOtoModel(UserRequestDTO userRequestDTO, User user) {

        if (userRequestDTO.getName() != null) {
            user.setName(userRequestDTO.getName());
        }
        if (userRequestDTO.getEmail() != null) {
            user.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        if (userRequestDTO.getBirthDate() != null) {
            user.setBirthDate(userRequestDTO.getBirthDate());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> userProjection = userRepository.searchUserAndRolesByEmail(username);
        if (userProjection.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = new User();
        user.setEmail(username);
        user.setPassword(userProjection.getFirst().getPassword());
        for (UserDetailsProjection projection : userProjection) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findByEmail(username).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public LoggedUserDTO getLoggedUser() {
        User user = authenticated();
        return new LoggedUserDTO(user);
    }
}
