package com.api_board.restapiboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//@EnableJpaAuditing: EntityDate 추상클래스의 기능 활성화를 위해 선언
@EnableJpaAuditing
@SpringBootApplication
public class RestApiBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiBoardApplication.class, args);
    }

}
