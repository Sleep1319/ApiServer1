package com.api_board.restapiboard.controller;

import com.api_board.restapiboard.exception.AccessDeniedException;
import com.api_board.restapiboard.exception.AuthenticationEntryPointException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ExceptionController {
    @GetMapping("/exception/entry-point")
    public void entryPoint() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/exception/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException();
    }
}
