package bricktoon.server.global.response;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BaseExceptionHandler {
    /**
     * @valid  유효성 체크에 통과하지 못하면 MethodArgumentNotValidException이 발생한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String validExceptionMessage = getValidExceptionMessage(e.getBindingResult());
        return BaseResponse.toResponseEntityContainsCustomMessage(BaseResponseStatus.INVALID_INPUT_VALUE, validExceptionMessage);
    }

    /**
     * @validated  유효성 체크에 통과하지 못하면 MethodArgumentNotValidException이 발생한다.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> constraintViolationException(ConstraintViolationException e) {
        String validExceptionMessage = e.getMessage().split(": ")[1];
        return BaseResponse.toResponseEntityContainsCustomMessage(BaseResponseStatus.INVALID_INPUT_VALUE, validExceptionMessage);
    }

    /**
     * @PreAuthorize  권한 인가에 통과하지 못하면 AccessDeniedException이 발생한다.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> accessDeniedException(AccessDeniedException e) {
        String preAuthorizeExceptionMessage = e.getMessage();
        return BaseResponse.toResponseEntityContainsCustomMessage(BaseResponseStatus.INVALID_AUTHORIZATION, preAuthorizeExceptionMessage);
    }

    /**
     *   BaseException은 그대로 처리한다.
     */
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<BaseResponse> handleBaseException(BaseException e) {
        return BaseResponse.toResponseEntityContainsStatus(e.errorStatus);
    }

    private String getValidExceptionMessage(BindingResult bindingResult) {
        return bindingResult.getFieldError().getDefaultMessage();
    }
}
