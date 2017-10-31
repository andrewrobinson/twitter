package com.andrew;


import org.junit.Test;

import java.util.stream.Stream;

public class TestTwitterParser {

    @Test
    public void testShowTweetsForNullInputs() {

        Stream<String> users = null;
        Stream<String> allTweets = null;

        TwitterParser.showTweets(users, allTweets);

    }
}
