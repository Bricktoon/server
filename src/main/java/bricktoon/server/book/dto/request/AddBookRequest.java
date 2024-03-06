package bricktoon.server.book.dto.request;

import lombok.Builder;

@Builder
public record AddBookRequest(
        String name,
        String genre,
        String location,
        Integer number
) {
}
