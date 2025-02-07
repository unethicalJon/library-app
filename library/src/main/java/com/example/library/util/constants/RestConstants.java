package com.example.library.util.constants;

public interface RestConstants {

    String ROOT = "/api";

    String ID = "id";

    String ID_PATH = "/{"+ ID + "}";

    String BEARER_TOKEN = "Bearer";

    String AUTHORIZATION_HEADER = "Authorization";

    interface UserController {

        String BASE = ROOT + "/user";

        String SIGN_UP = "/sign-up";

        String UPDATE = "/update";

        String PASSWORD_CHANGE = "/password";

        String ACTIVATE_USER = "/activate";

        String SIGNUP_FULL_PATH = BASE + SIGN_UP;

    }

    interface BookController {

        String BASE = ROOT + "/book";

        String ADD_BOOK = "/add";
    }

    interface AuthController {

        String BASE = ROOT + "/auth";

        String LOG_IN = "/log-in";

        String LOGIN_FULL_PATH = BASE + LOG_IN;
    }

    interface LibraryController {

        String BASE = ROOT + "/library";

        String ADD = "/add";

        String UPDATE = "/update";

        String DELETE = "/delete";

    }


}
