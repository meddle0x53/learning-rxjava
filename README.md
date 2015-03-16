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
     gradle build
   ```
   
 * This will download and install all the dependencies needed by the project and will compile it.
 * You can open the project with Eclipse and run the examples. You'll need the gradle plugin for Eclipse.
 * All the examples can be run through a single command line application:
 
  ```
    gradle rxrun
  ```
 
## Available commands of the command line application

### list
The list command lists the examples with their numbers. These numbers can be used to run an example.
It has two forms:
 
 * `list`/`list all` - Lists all the examples, grouped by chapters.
 * `list chapter <chapter_number1> <chapter_number2> ...` - Lists the examples only for the given chapters.
 
### help
Prints help for the available commands.

### exit
Exits from the application.

### run
Runs an example using its number (the number can be retrieved by running `list`).

## Examples
Here are the descriptions of all the examples in the book.

### 1. Iterator VS Observable (Chapter 1, page 5)
This example is used in the book in the 'Comparing the Iterator pattern and the RxJava Observable' of the first chapter.
It demonstrates the difference between RxJava's Observables and the Iterators, by iterating over a list of strings.
The `Observable.from` method is introduced here for the first time, as well as subscribing.

The example can be found here [ObservableVSIterator](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter01/ObservableVSIterator.java)

### 2. Reactive Sum, version 1 (Chapter 1, page 10)
This is example demonstrates a reactive sum, which is updated on change of any of its collectors. It is demonstrates
many of the features of RxJava, like Observers, Schedulers Observable transformations, filtering and combining.

The example can be found here [ReactiveSumV1](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter01/ReactiveSumV1.java)

 