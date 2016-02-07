package com.infinityworks.accounts;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AccountGenerator {
    public static void main(String... args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("user"));
    }
}
