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

### 01. Iterator VS Observable (Chapter 1, pages 5)
This example is used in the book in the 'Comparing the Iterator pattern and the RxJava Observable' of the first chapter.
It demonstrates the difference between RxJava's Observables and the Iterators, by iterating over a list of strings.
The `Observable.from` method is introduced here for the first time, as well as subscribing.

The example can be found here [ObservableVSIterator](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter01/ObservableVSIterator.java)

### 02. Reactive Sum, version 1 (Chapter 1, pages 10)
This is example demonstrates a reactive sum, which is updated on change of any of its collectors. It is demonstrates
many of the features of RxJava, like Observers, Schedulers Observable transformations, filtering and combining.

The example can be found here [ReactiveSumV1](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter01/ReactiveSumV1.java)

### 03. Introduction to the new syntax and semantics (Chapter 2, pages 17-22)
Demonstrates creating and using lambdas, passing them to methods, that receive Functional Interfaces as parameters and references
to existing methods.

The example can be found here [Java8LambdasSyntaxIntroduction](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/Java8LambdasSyntaxIntroduction.java)

### 04. Reactive Sum, version 2 (with lambdas) (Chapter 2, pages 22-24)
Another implementation of the 'Reactive Sum', similar to the on of the first chapter, but it uses the new Java 8 syntax with lambdas.

The example can be found here [ReactiveSumV2](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/ReactiveSumV2.java)
 
### 05. Pure and higher functions (Chapter 2, pages 26-29)
Demonstrates pure and higher order functions. Applies higher order functions to other functions.

The example can be found here [PureAndHigherOrderFunctions](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/PureAndHigherOrderFunctions.java)

### 06. Introduction to monads (Chapter 2, pages 30-33)
Shows implementation and uses of a monad. The Optional monad.

The example can be found here [Monads](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/Monads.java)
 
### 07. Observables and monads (Chapter 2, pages 34)
Shows that Observables are not true monads. They are monad-like structures though and benefit from that.

The example can be found here [ObservablesAndMonads](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/ObservablesAndMonads.java)
 
### 08. Creating Observables with Observable.from (Chapter 3, pages 39-40)
A set of examples of using the Observable.from method for creating Observables from collections, arrays and Iterables.

The example can be found here [CreatingObservablesWithFrom](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/CreatingObservablesWithFrom.java)

### 09. Using Observable.from with Future (Chapter 3, pages 40-42)
Demonstrates creating Observables using the Observable.from(Future) method.

The example can be found here [CreatingObservablesWithFromFuture](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/CreatingObservablesWithFromFuture.java)

### 10. Using the Observable.just method to create Observables (Chapter 3, pages 42-43)
Demonstrates creating Observables using the Observable.just method.

The example can be found here [CreatingObservablesUsingJust](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/CreatingObservablesUsingJust.java)

### 11. A few factory methods for creating Observables (Chapter 3, pages 43-46)
Demonstrates using Observable.interval, Observable.timer, Observable.error,
Observable.never, Observable.empty and Observable.range for Obsevable creation.

The example can be found here [CreatingObservablesUsingVariousFactoryMethods](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/CreatingObservablesUsingVariousFactoryMethods.java)

### 12. Demonstration of the Observable.create method (Chapter 3, pages 46-50)
Demonstrates using Observable.create for creating custom Observables.
Contains unsubscribing and implementing unsubscribing logic in Observable.create.

The example can be found here [ObservableCreateExample](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/ObservableCreateExample.java)