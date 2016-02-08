package com.infinityworks.accounts;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class AccountGenerator {
    public static void main(String... args) {
        System.out.println(BCrypt.hashpw("user", BCrypt.gensalt()));
    }
}
