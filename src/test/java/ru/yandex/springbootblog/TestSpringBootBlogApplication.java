package ru.yandex.springbootblog;

import org.springframework.boot.SpringApplication;

public class TestSpringBootBlogApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringBootBlogApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
