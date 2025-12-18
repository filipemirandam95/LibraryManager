INSERT INTO tb_user (name, email, password, birth_date) VALUES ('João Silva', 'joao.silva@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1985-03-20');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Maria Oliveira', 'maria.oliveira@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1990-07-15');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Carlos Pereira', 'carlos.pereira@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1980-11-05');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Ana Souza', 'ana.souza@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1995-02-28');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Felipe Costa', 'felipe.costa@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1982-09-12');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Juliana Santos', 'juliana.santos@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1992-12-22');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Lucas Almeida', 'lucas.almeida@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1997-04-10');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Gabriela Lima', 'gabriela.lima@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1993-01-18');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Eduardo Fernandes', 'eduardo.fernandes@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1988-10-05');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Patrícia Mendes', 'patricia.mendes@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1984-08-03');
INSERT INTO tb_user (name, email, password, birth_date) VALUES ('Lucas Andrade', 'lucas.andrade@example.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', '1990-09-01');

INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('O Senhor dos Anéis', '9788550303058', 1954, '1ª', 10, 0);
INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('Harry Potter e a Pedra Filosofal', '9788532530394', 1997, '1ª', 8, 8);
INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('1984', '9780451524935', 1949, '1ª', 15, 15);
INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('Dom Casmurro', '9788520900385', 1899, '1ª', 12, 12);
INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('O Pequeno Príncipe', '9788580572828', 1943, '1ª', 20, 20);
INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('A Moreninha', '9788503022395', 1844, '1ª', 10, 10);
INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('O Conde de Monte Cristo', '9788535900748', 1844, '1ª', 5, 5);
INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('O Hobbit', '9788550303072', 1937, '1ª', 7, 7);
INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('Moby Dick', '9781503280786', 1851, '1ª', 6, 6);
INSERT INTO tb_book (title, isbn, book_year, edition, total_copies, available_copies) VALUES ('Cem Anos de Solidão', '9788535910426', 1967, '1ª', 14, 14);


INSERT INTO tb_author (name, birth_date) VALUES ('J.R.R. Tolkien', '1892-01-03');
INSERT INTO tb_author (name, birth_date) VALUES ('J.K. Rowling', '1965-07-31');
INSERT INTO tb_author (name, birth_date) VALUES ('George Orwell', '1903-06-25');
INSERT INTO tb_author (name, birth_date) VALUES ('Machado de Assis', '1839-06-21');
INSERT INTO tb_author (name, birth_date) VALUES ('Antoine de Saint-Exupéry', '1900-06-29');
INSERT INTO tb_author (name, birth_date) VALUES ('Joaquim Manuel de Macedo', '1820-06-24');
INSERT INTO tb_author (name, birth_date) VALUES ('Alexandre Dumas', '1802-07-24');
INSERT INTO tb_author (name, birth_date) VALUES ('Herman Melville', '1819-08-01');
INSERT INTO tb_author (name, birth_date) VALUES ('Gabriel García Márquez', '1927-03-06');

INSERT INTO tb_category (name) VALUES ('Ficção Fantástica');
INSERT INTO tb_category (name) VALUES ('Ficção Científica');
INSERT INTO tb_category (name) VALUES ('Literatura Brasileira');
INSERT INTO tb_category (name) VALUES ('Literatura Infantil');
INSERT INTO tb_category (name) VALUES ('Aventura');
INSERT INTO tb_category (name) VALUES ('Clássicos');

INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (1, 1, '2025-03-14', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (2, 2, '2025-03-15', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (3, 3, '2025-03-16', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (4, 4, '2025-03-17', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (5, 5, '2025-03-18', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (6, 6, '2025-03-19', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (7, 7, '2025-03-20', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (8, 8, '2025-03-21', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (9, 9, '2025-03-22', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (10, 10, '2025-03-23', 2);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (9, 4, '2025-07-19', 0);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (7, 6, '2025-05-20', 3);
INSERT INTO tb_reservation (user_id, book_id, reservation_date, status) VALUES (4, 10, '2025-10-15', 0);

INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (1, 1, '2025-03-15', '2025-03-20');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (2, 2, '2025-03-16', '2025-03-21');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (3, 3, '2025-03-17', '2025-03-22');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (4, 4, '2025-03-18', '2025-03-23');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (5, 5, '2025-03-19', '2025-03-24');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (6, 6, '2025-03-20', '2025-03-25');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (7, 7, '2025-03-21', '2025-03-26');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (8, 8, '2025-03-22', '2025-04-02');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (9, 9, '2025-03-23', '2025-04-03');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (10, 10, '2025-03-24', '2025-04-04');
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (1, 2, '2025-06-10', null);
INSERT INTO tb_loan (user_id, book_id, loan_date, return_date) VALUES (1, 2, '2025-07-17', null);

INSERT INTO tb_fine (amount, applied_at, paid_at, paid, loan_id) VALUES (12, '2025-03-30', '2025-04-02', TRUE, 8);
INSERT INTO tb_fine (amount, applied_at, paid_at, paid, loan_id) VALUES (12, '2025-03-31', '2025-04-03', TRUE, 9);
INSERT INTO tb_fine (amount, applied_at, paid_at, paid, loan_id) VALUES (12, '2025-04-01', '2025-04-04', TRUE, 10);
INSERT INTO tb_fine (amount, applied_at, paid_at, paid, loan_id) VALUES (12, '2025-06-18', NULL, FALSE, 11);

INSERT INTO tb_book_author (author_id, book_id) VALUES (1, 1);
INSERT INTO tb_book_author (author_id, book_id) VALUES (2, 2);
INSERT INTO tb_book_author (author_id, book_id) VALUES (3, 3);
INSERT INTO tb_book_author (author_id, book_id) VALUES (4, 4);
INSERT INTO tb_book_author (author_id, book_id) VALUES (5, 5);
INSERT INTO tb_book_author (author_id, book_id) VALUES (6, 6);
INSERT INTO tb_book_author (author_id, book_id) VALUES (7, 7);
INSERT INTO tb_book_author (author_id, book_id) VALUES (1, 8);
INSERT INTO tb_book_author (author_id, book_id) VALUES (8, 9);
INSERT INTO tb_book_author (author_id, book_id) VALUES (9, 10);

INSERT INTO tb_book_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO tb_book_categories (book_id, category_id) VALUES (2, 2);
INSERT INTO tb_book_categories (book_id, category_id) VALUES (3, 2);
INSERT INTO tb_book_categories (book_id, category_id) VALUES (4, 3);
INSERT INTO tb_book_categories (book_id, category_id) VALUES (5, 4);
INSERT INTO tb_book_categories (book_id, category_id) VALUES (6, 3);
INSERT INTO tb_book_categories (book_id, category_id) VALUES (7, 5);
INSERT INTO tb_book_categories (book_id, category_id) VALUES (8, 1);
INSERT INTO tb_book_categories (book_id, category_id) VALUES (9, 6);
INSERT INTO tb_book_categories (book_id, category_id) VALUES (10, 6);

INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (4, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (5, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (6, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (7, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (8, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (9, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (10, 1);

