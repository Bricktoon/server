package bricktoon.server.book.repository;

import bricktoon.server.book.domain.Book;
import bricktoon.server.user.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByNameContainsAndPlaceOrderByNameAsc(String name, Place place);
    List<Book> findAllByGenreAndPlaceOrderByNameAsc(String genre, Place place);
    List<Book> findAllByPlaceOrderByNameAsc(Place place);
    Boolean existsByName(String name);
    Optional<Book> findByName(String name);
}
