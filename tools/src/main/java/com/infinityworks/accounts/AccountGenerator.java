package com.infinityworks.accounts;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AccountGenerator {
    public static void main(String... args) {
        String raw = "barking";
        String hashed1 = BCrypt.hashpw(raw, BCrypt.gensalt());
        System.out.println(hashed1);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed2 = encoder.encode(raw);

        System.out.println(BCrypt.checkpw(raw, hashed1));
        System.out.println(BCrypt.checkpw(raw, hashed2));
    }
}
