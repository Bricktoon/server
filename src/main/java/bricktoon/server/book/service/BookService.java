package bricktoon.server.book.service;

import bricktoon.server.book.domain.Book;
import bricktoon.server.book.dto.request.AddBookRequest;
import bricktoon.server.book.dto.response.AddBookResponse;
import bricktoon.server.book.dto.response.GetBookDetailsResponse;
import bricktoon.server.book.dto.response.GetBookListResponse;
import bricktoon.server.book.repository.BookRepository;
import bricktoon.server.global.response.BaseException;
import bricktoon.server.global.response.BaseResponseStatus;
import bricktoon.server.user.domain.User;
import bricktoon.server.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
    private final UserFindService userFindService;
    private final BookRepository bookRepository;

    public AddBookResponse addBook(UserDetails userDetails, AddBookRequest addBookRequest) {
        User user = userFindService.findByUserDetails(userDetails);
        Book book = Book.builder()
                .name(addBookRequest.name())
                .genre(addBookRequest.genre())
                .location(addBookRequest.location())
                .number(addBookRequest.number())
                .place(user.getPlace())
                .build();
        Book savedBook = bookRepository.save(book);
        return new AddBookResponse(savedBook.getId());
    }

    public void addAllBook(UserDetails userDetails, List<AddBookRequest> addBookRequestList) {
        User user = userFindService.findByUserDetails(userDetails);
        List<Book> bookList = new ArrayList<>();
        for (AddBookRequest addBookRequest: addBookRequestList) {
            Book book = Book.builder()
                    .name(addBookRequest.name())
                    .genre(addBookRequest.genre())
                    .location(addBookRequest.location())
                    .number(addBookRequest.number())
                    .place(user.getPlace())
                    .build();
            bookList.add(book);
        }
        bookRepository.saveAll(bookList);
    }

    public AddBookResponse updateBook(UserDetails userDetails, Long bookId, AddBookRequest addBookRequest) {
        User user = userFindService.findByUserDetails(userDetails);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty())
            throw new BaseException(BaseResponseStatus.BOOK_NOT_FOUND_ERROR);

        if (!book.get().getPlace().equals(user.getPlace()))
            throw new BaseException(BaseResponseStatus.BOOK_NOT_AUTHORIZED_ERROR);

        book.get().update(
                addBookRequest.name(),
                addBookRequest.genre(),
                addBookRequest.location(),
                addBookRequest.number(),
                user.getPlace()
        );

        return new AddBookResponse(book.get().getId());
    }

    public void deleteBook(UserDetails userDetails, Long bookId) {
        User user = userFindService.findByUserDetails(userDetails);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty())
            throw new BaseException(BaseResponseStatus.BOOK_NOT_FOUND_ERROR);

        if (!book.get().getPlace().equals(user.getPlace()))
            throw new BaseException(BaseResponseStatus.BOOK_NOT_AUTHORIZED_ERROR);

        bookRepository.deleteById(bookId);
    }

    public GetBookDetailsResponse getBookDetails(UserDetails userDetails, Long bookId) {
        User user = userFindService.findByUserDetails(userDetails);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty())
            throw new BaseException(BaseResponseStatus.BOOK_NOT_FOUND_ERROR);

        if (!book.get().getPlace().equals(user.getPlace()))
            throw new BaseException(BaseResponseStatus.BOOK_NOT_AUTHORIZED_ERROR);

        return GetBookDetailsResponse.builder()
                .name(book.get().getName())
                .genre(book.get().getGenre())
                .location(book.get().getLocation())
                .number(book.get().getNumber())
                .place(book.get().getPlace().getKey())
                .build();
    }

    public List<GetBookListResponse> getBookList(UserDetails userDetails, String type, String keyword) {
        User user = userFindService.findByUserDetails(userDetails);
        List<Book> targetBookList = getTargetBookList(user, type, keyword);
        List<GetBookListResponse> getBookListResponseList = new ArrayList<>();
        for (Book book: targetBookList) {
            getBookListResponseList.add(
                    GetBookListResponse.builder()
                            .id(book.getId())
                            .name(book.getName())
                            .genre(book.getGenre())
                            .location(book.getLocation())
                            .number(book.getNumber())
                    .build());
        }
        return getBookListResponseList;
    }

    private List<Book> getTargetBookList(User user, String type, String keyword) {
        switch (type) {
            case "도서명":
                return bookRepository.findAllByNameContainsAndPlaceOrderByNameAsc(keyword, user.getPlace());
            case "장르":
                return bookRepository.findAllByGenreAndPlaceOrderByNameAsc(keyword, user.getPlace());
        }
        throw new BaseException(BaseResponseStatus.SEARCH_TYPE_NOT_FOUND_ERROR);
    }
}
