package com.andrew;

public class Tweet {

    String person;
    String message;

    public Tweet(String person, String message) {
        this.person = person;
        this.message = message;
    }


    public String getPerson() {
        return person;
    }

    public String getMessage() {
        return message;
    }

}
