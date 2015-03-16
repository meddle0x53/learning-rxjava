# Learning Reactive Programming With Java 8 Example Runner
This project contains the examples of the 'Learning Reactive Programming With Java 8' book.
It is a command line application, contained in one jar, written using RxJava that is able to run every example.

## Installing and running this program.
 * Of course you need Git.
 * To run this program you need Java 8, if you don't have it, navigate to Oracle's site and download/install it.
 * The other necessary thing is Gradle 2.x, you can download it and from here - [https://gradle.org/downloads](https://gradle.org/downloads).
 * Now you can clone this project by running :
 
   ```
   git clone https://github.com/meddle0x53/learning-rxjava.git
   ```
   
 * Navigate to the root of the project (`cd learning-rxjava`) and run :
 
   ```
     gradle rxrun
   ```
   
This will download and install all the dependencies needed by the project, will compile it and run it for you.
 