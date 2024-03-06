package bricktoon.server.book.dto.response;

import lombok.Builder;

@Builder
public record GetBookListResponse(
        String name,
        String genre,
        String location,
        Integer number
) {
}
