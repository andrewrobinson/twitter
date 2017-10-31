package com.andrew;

public class Main {

    public static void main(String[] args) {

        if (args.length != 2) {
            throw new RuntimeException("exactly two commandline params expected, 1:users Filepath and 2:tweets Filepath ");
        }

        TwitterParser twitter = new TwitterParser(args[0], args[1]);
        twitter.showTweets();


    }

}
