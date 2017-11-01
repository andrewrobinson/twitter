

Allan Gray coding assignment - Andrew Robinson - 01/11/2017
===============================================

TODO
----------

 users and followers not Person and followers

 unit tests. no space after comma etc
 
 stream out / stringbuilder? / test it with large files

To run:
-------

mvn clean package

java -jar target/twitter-1.0-SNAPSHOT.jar user.txt tweet.txt

Or, in an IDE, run the main method in the Main class (supplying command line options)


What I haven't catered for:
---------------------------

I assumed the user and tweet files would be formatted exactly like those supplied and didn't try and
make the parser resilient to things like no spaces after the comma or the >


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

 
