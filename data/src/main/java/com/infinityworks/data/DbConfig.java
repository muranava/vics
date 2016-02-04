package com.infinityworks.data;

public interface DbConfig {
    String getDbUrl();
    String getDbUser();
    String getDbPassword();

    class Default implements DbConfig {
        String DB_URL = "jdbc:postgresql://localhost:5432/canvass";
        String DB_USER = "postgres";
        String DB_PASSWORD = "postgres";

        @Override
        public String getDbUrl() {
            return DB_URL;
        }

        @Override
        public String getDbUser() {
            return DB_USER;
        }

        @Override
        public String getDbPassword() {
            return DB_PASSWORD;
        }
    }
}
