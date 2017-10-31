

Allan Gray coding assignment - Andrew Robinson - 30/10/2017
===============================================


To run:
-------

a)

mvn clean package

java -jar target/twitter-1.0-SNAPSHOT.jar user.txt tweet.txt

b) 

Or in an IDE, run the main method in the Main class (supplying command line options)


A note about how I wrote this:
------------------------------

I wrote my initial solution inside an existing project on my work machine after work.
I then mailed this code across to my home laptop and added version control and a pom.
Then I thought about test coverage so refactored a bit to remove file access from things.
Hopefully I haven't prematurely optimised! 
I was starting to think about how decomposed / distributed this processing might be in the real Twitter.

I do often write test-first code though.

Here is a coding assignment I wrote recently where doing it test-first 
allowed me to solve it better, faster and more reliably.
Without testing, for that problem, I would probably have struggled.

https://github.com/andrewrobinson/poker

 
TODO - tolerance of input files
