package com.example.ticketing;

public class Constants {
    //public final static String URL = "jdbc:mysql://host.docker.internal:3308/ticketing_service";
    public final static String URL = "jdbc:mysql://localhost:3306/ticketing_service";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "*";

    private Constants() {
        throw new AssertionError("Cannot instantiate the Constants class");
    }

}
