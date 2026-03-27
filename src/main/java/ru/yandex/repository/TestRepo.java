package ru.yandex.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestRepo {

    private final JdbcTemplate jdbc;

    public TestRepo(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void test() {
        jdbc.execute("CREATE TABLE TEST(id INT PRIMARY KEY, name VARCHAR(50))");
        jdbc.update("INSERT INTO TEST VALUES(1, 'Hello H2')");

        String name = jdbc.queryForObject(
            "SELECT name FROM test WHERE id = 1",
            String.class
        );

        System.out.println(name);
    }
}
