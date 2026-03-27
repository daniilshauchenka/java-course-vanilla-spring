package ru.yandex;

import java.sql.SQLException;
import org.h2.tools.Server;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.yandex.config.AppConfig;

public class Main {

    public static void main(String[] args) throws SQLException {
        Server.createWebServer("-webPort", "8082").start();
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println();
    }
}