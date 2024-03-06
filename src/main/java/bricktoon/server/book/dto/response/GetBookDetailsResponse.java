package bricktoon.server.book.dto.response;

import lombok.Builder;

@Builder
public record GetBookDetailsResponse(
        String name,
        String genre,
        String location,
        Integer number,
        String place
) {
}
