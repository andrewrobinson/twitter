

Allan Gray coding assignment - Andrew Robinson - 01/11/2017
===============================================

To run:
-------

mvn clean package

java -jar target/twitter-1.0-SNAPSHOT.jar user.txt tweet.txt

Or, in an IDE, run the main method in the Main class (supplying command line options)


Notes
-----

I've tried to find a balance between "sufficient quality to run on a production system" and prematurely optimising.

Obvious thoughts going further are

"What if the files were larger?"

"Could this program run over long periods of time, consuming from these files as they grew?"

"How could you decompose this algorithm further and even distribute it if necessary?"

I might experiment further but feel I need to wrap this assignment up now.

My getTweetFeedForDisplay method would need to return a Stream and all code within it would probably need to be chained.

The instructions say:
"Where there is more than one entry for a user, consider the union of all these entries to determine the users they follow."

This gives you no mechanism for ever allowing someone to "unfollow" someone, assuming this was an ever-growing file.


How I wrote this
----------------

I wrote this code initially in a main method, just inspecting stdout, but then extracted methods / redesigned it so as to be
more testable. I also wrote bits at work, after hours, and then transferred files over to my home laptop and back so don't expect
a proper git history. (I couldn't easily access github from my work machine)

The testing phase allowed me to catch the fact that I was producing extra line breaks after the last tweet displayed.
For the feature that I added where the comma separated list of users could handle optional spaces,
I wrote a test to fail first and then implemented the feature.

I did a coding assignment for another company recently and in that one I did strict test-first development.
For this one it didn't feel as essential.



 
