package com.api_board.restapiboard.exception;

public class MemberEmailAlreadyExistsException  extends RuntimeException{
    public MemberEmailAlreadyExistsException(String message) {
        super(message);
    }
}
