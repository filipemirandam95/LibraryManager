package com.myprojects.LibraryManager.repositories;

import com.myprojects.LibraryManager.entities.User;
import com.myprojects.LibraryManager.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    @EntityGraph(attributePaths = {"loans", "loans.book", "reservations", "reservations.book"})
//    Page<User> findAll(Pageable pageable);

    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.loans l JOIN FETCH l.book b LEFT JOIN FETCH l.fine JOIN FETCH b.authors" +
            " LEFT JOIN FETCH u.reservations r JOIN FETCH r.book WHERE u IN :users")
    List<User> findUserLoansReservations(List<User> users);

    List<User> findByNameContainingIgnoreCase(String name);

    @Query(nativeQuery = true, value = """
			SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleId, tb_role.authority
			FROM tb_user
			INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
			INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
			WHERE tb_user.email = :email
		""")
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);

	Optional<User> findByEmail(String email);

}

