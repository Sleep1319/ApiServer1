package com.api_board.restapiboard.advice;

import com.api_board.restapiboard.dto.rsponse.Response;
import com.api_board.restapiboard.exception.*;
import com.api_board.restapiboard.handler.ResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdvice {
    private final ResponseHandler responseHandler;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        log.error("e = {}", e.getMessage());
        return getFailureResponse(ExceptionType.EXCEPTION);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response accessDeniedException() {
        return getFailureResponse(ExceptionType.ACCESS_DENIED_EXCEPTION);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        return getFailureResponse(ExceptionType.BIND_EXCEPTION, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return getFailureResponse(ExceptionType.LOGIN_FAILURE_EXCEPTION);
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return getFailureResponse(ExceptionType.MEMBER_EMAIL_ALREADY_EXISTS_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return getFailureResponse(ExceptionType.MEMBER_NICKNAME_ALREADY_EXISTS_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() {
        return getFailureResponse(ExceptionType.MEMBER_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException() {
        return getFailureResponse(ExceptionType.ROLE_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response missingRequestHeaderException(MissingRequestHeaderException e) {
        return getFailureResponse(ExceptionType.MISSING_REQUEST_HEADER_EXCEPTION, e.getHeaderName());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException() {
        return getFailureResponse(ExceptionType.CATEGORY_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(CannotConvertNestedStructureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response cannotConvertNestedStructureException(CannotConvertNestedStructureException e) {
        log.error("e = {}", e.getMessage());
        return getFailureResponse(ExceptionType.CANNOT_CONVERT_NESTED_STRUCTURE_EXCEPTION);
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response postNotFoundException() {
        return getFailureResponse(ExceptionType.POST_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response unsupportedImageFormatException() {
        return getFailureResponse(ExceptionType.UNSUPPORTED_IMAGE_FORMAT_EXCEPTION);
    }

    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        log.error("e = {}", e.getMessage());
        return getFailureResponse(ExceptionType.FILE_UPLOAD_FAILURE_EXCEPTION);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response commentNotFoundException() {
        return getFailureResponse(ExceptionType.COMMENT_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response messageNotFoundException() {
        return getFailureResponse(ExceptionType.MESSAGE_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(RefreshTokenFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response refreshTokenFailureException() {
        return getFailureResponse(ExceptionType.REFRESH_TOKEN_FAILURE_EXCEPTION);
    }

    private Response getFailureResponse(ExceptionType exceptionType) {
        return responseHandler.getFailureResponse(exceptionType);
    }

    private Response getFailureResponse(ExceptionType exceptionType, Object... args) {
        return responseHandler.getFailureResponse(exceptionType, args);
    }
}
