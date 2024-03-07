package bricktoon.server.book.dto.response;

import lombok.Builder;

@Builder
public record GetBookListResponse(
        Long id,
        String name,
        String genre,
        String location,
        Integer number
) {
}
