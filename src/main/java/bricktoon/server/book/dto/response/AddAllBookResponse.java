package bricktoon.server.book.dto.response;

import lombok.Builder;

@Builder
public record AddAllBookResponse(
        Long row
) {
}
