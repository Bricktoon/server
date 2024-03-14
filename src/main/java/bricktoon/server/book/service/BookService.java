package bricktoon.server.book.service;

import bricktoon.server.book.domain.Book;
import bricktoon.server.book.dto.request.AddBookRequest;
import bricktoon.server.book.dto.response.AddAllBookResponse;
import bricktoon.server.book.dto.response.AddBookResponse;
import bricktoon.server.book.dto.response.GetBookDetailsResponse;
import bricktoon.server.book.dto.response.GetBookListResponse;
import bricktoon.server.book.repository.BookRepository;
import bricktoon.server.global.response.BaseException;
import bricktoon.server.global.response.BaseResponseStatus;
import bricktoon.server.user.domain.User;
import bricktoon.server.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

    public AddAllBookResponse addAllBook(UserDetails userDetails, MultipartFile file) throws IOException {
        User user = userFindService.findByUserDetails(userDetails);

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        List<Book> bookList = new ArrayList<>();
        Long completeRow = -1L;
        Row row = null;

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            if (row.getCell(0) == null || row.getCell(1) == null
                    || row.getCell(2) == null || row.getCell(4) == null)
                break;

            Book book = Book.builder()
                    .name(row.getCell(0).getStringCellValue())
                    .genre(row.getCell(1).getStringCellValue())
                    .location(row.getCell(2).getStringCellValue())
                    .number((int) row.getCell(4).getNumericCellValue())
                    .place(user.getPlace())
                    .build();

            if (completeRow == -1L)
                completeRow = (long) row.getRowNum();
            completeRow++;

            if (bookRepository.existsByName(book.getName())) {
                bookRepository.findByName(book.getName()).get().update(
                        row.getCell(0).getStringCellValue(),
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(),
                        (int) row.getCell(4).getNumericCellValue(),
                        user.getPlace()
                );
                continue;
            }
            bookList.add(book);
        }

        bookRepository.saveAll(bookList);
        return new AddAllBookResponse(completeRow);
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

    public List<GetBookListResponse> getAllBook(UserDetails userDetails) {
        User user = userFindService.findByUserDetails(userDetails);
        List<Book> targetBookList = bookRepository.findAllByPlaceOrderByNameAsc(user.getPlace());
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
