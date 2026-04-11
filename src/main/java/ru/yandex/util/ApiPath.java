package ru.yandex.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ApiPath {

    public static final String API = "/api";


    public static final class Posts {
        public static final String BASE = API + "/posts";
    }

    public static final class Comments {
        public static final String BASE = Posts.BASE + "/{postId}/comments";
    }


}
