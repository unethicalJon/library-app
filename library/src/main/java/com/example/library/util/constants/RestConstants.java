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

        String PROFILE = "/profile";

        String UPDATE = "/profile-update";

        String PASSWORD_CHANGE = "/change-password";

        String ACTIVATE_USER = "/activate";

        String SIGNUP_FULL_PATH = BASE + SIGN_UP;

    }

    interface BookController {

        String BASE = ROOT + "/book";

        String ADD_USER = "/add-book";
    }

    interface AuthController {

        String BASE = ROOT + "/auth";

        String LOG_IN = "/log-in";

        String LOGIN_FULL_PATH = BASE + LOG_IN;
    }


}
