package bricktoon.server.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus implements BaseResponseStatusImpl {
    /**
     * 100 : 진행 정보
     */

    /**
     * 200 : 요청 성공
     */
    SUCCESS(HttpStatus.OK, "SUCCESS", "요청에 성공했습니다."),
    CREATED(HttpStatus.CREATED, "CREATED", "요청에 성공했으며 리소스가 정상적으로 생성되었습니다."),
    ACCEPTED(HttpStatus.ACCEPTED, "ACCEPTED", "요청에 성공했으나 처리가 완료되지 않았습니다."),
    DELETED(HttpStatus.NO_CONTENT, "DELETED", "요청에 성공했으며 더 이상 응답할 내용이 존재하지 않습니다."),

    /**
     * 300 : 리다이렉션
     */
    SEE_OTHER(HttpStatus.SEE_OTHER, "REDIRECT_001", "다른 주소로 요청해주세요."),
    RETRY_REQUEST(HttpStatus.FOUND, "REDIRECT_002", "재발급된 AccessToken / RefreshToken 으로 재시도 해주세요."),

    /**
     * 400 : 요청 실패
     */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "REQUEST_ERROR_001", "잘못된 요청입니다."),
    INVALID_INPUT_DTO(HttpStatus.BAD_REQUEST, "REQUEST_ERROR_002", "잘못된 DTO 형식입니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "REQUEST_ERROR_003", "로그인 후 이용해주세요."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "REQUEST_ERROR_004", "잘못된 File 형식입니다."),
    INVALID_AUTHORIZATION(HttpStatus.FORBIDDEN, "REQUEST_ERROR_005", "비정상적인 접근입니다."),
    INVALID_ENUM(HttpStatus.BAD_REQUEST, "REQUEST_ERROR_006", "변경할 수 없는 ENUM type 입니다."),
    TOO_MANY_REQUEST_ERROR(HttpStatus.TOO_MANY_REQUESTS, "REQUEST_ERROR_007", "요청이 너무 잦습니다. 잠시 후 다시 시도해주세요."),

    // User
    NOT_FOUND_PLACE(HttpStatus.NOT_FOUND, "USER_ERROR_01", "입력한 지점을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "USER_ERROR_02", "사용자를 찾을 수 없습니다."),
    USERNAME_ALREADY_EXISTS_ERROR(HttpStatus.BAD_REQUEST, "USER_ERROR_03", "중복된 사용자 ID입니다."),

    // Book
    BOOK_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "BOOK_ERROR_01", "책을 찾을 수 없습니다."),
    BOOK_NOT_AUTHORIZED_ERROR(HttpStatus.NOT_FOUND, "BOOK_ERROR_02", "해당 지점의 책이 아닙니다."),
    SEARCH_TYPE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "BOOK_ERROR_03", "책 검색기준을 찾을 수 없습니다."),

    /**
     * 500 : 응답 실패
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RESPONSE_ERROR_001", "서버와의 연결에 실패했습니다."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "RESPONSE_ERROR_002", "다른 서버로부터 잘못된 응답이 수신되었습니다."),
    INSUFFICIENT_STORAGE(HttpStatus.INSUFFICIENT_STORAGE, "RESPONSE_ERROR_003", "서버의 용량이 부족해 요청에 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
