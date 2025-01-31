package com.example.library.util.constants;

public interface RestConstants {

    String ROOT = "/api";

    String ID = "id";

    String ID_PATH = "/{"+ ID + "}";

    interface UserController {

        String BASE = ROOT + "/user";

        String ADD_USER = "/add-user";

        String AUTHENTICATE_USER = "/login-user";
    }

    interface BookController {

        String BASE = ROOT + "/book";

        String ADD_USER = "/add-book";
    }
}
