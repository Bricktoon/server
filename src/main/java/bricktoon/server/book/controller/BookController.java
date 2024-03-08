package bricktoon.server.book.controller;

import bricktoon.server.book.dto.request.AddBookRequest;
import bricktoon.server.book.service.BookService;
import bricktoon.server.global.response.BaseResponse;
import bricktoon.server.global.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse> addBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddBookRequest addBookRequest
            ) {
        return BaseResponse.toResponseEntityContainsStatusAndResult(
                BaseResponseStatus.CREATED,
                bookService.addBook(userDetails, addBookRequest)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/all")
    public ResponseEntity<BaseResponse> addAllBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file
            ) throws IOException {
        return BaseResponse.toResponseEntityContainsStatusAndResult(
                BaseResponseStatus.CREATED,
                bookService.addAllBook(userDetails, file)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{bookId}")
    public ResponseEntity<BaseResponse> updateBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddBookRequest addBookRequest,
            @PathVariable("bookId") Long bookId
    ) {
        return BaseResponse.toResponseEntityContainsResult(
                bookService.updateBook(userDetails, bookId, addBookRequest)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<BaseResponse> deleteBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("bookId") Long bookId
    ) {
        bookService.deleteBook(userDetails, bookId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.SUCCESS);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{bookId}")
    public ResponseEntity<BaseResponse> getBookDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("bookId") Long bookId
    ) {
        return BaseResponse.toResponseEntityContainsResult(
                bookService.getBookDetails(userDetails, bookId)
        );
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<BaseResponse> getBookList(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("type") String type,
            @RequestParam("keyword") String keyword
    ) {
        return BaseResponse.toResponseEntityContainsResult(
                bookService.getBookList(userDetails, type, keyword)
        );
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllBook(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return BaseResponse.toResponseEntityContainsResult(
                bookService.getAllBook(userDetails)
        );
    }

}
