package com.andrew;

public class Tweet {

    String user;
    String message;

    public Tweet(String person, String message) {
        this.user = person;
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

}
