# Learning Reactive Programming With Java 8 Example Runner
This project contains the examples of the 'Learning Reactive Programming With Java 8' book.

## Installing and running this program.
 * Of course you'll need Git :).
 * To run these examples you need Java 8, if you don't have it, navigate to Oracle's site and download/install it.
 * Now you can clone this project by running :
 
   ```
   git clone https://github.com/meddle0x53/learning-rxjava.git
   ```
   
 * Navigate to the root of the project (`cd learning-rxjava`) and run :
 
   ```
     ./gradlew build
   ```
   
 * This will download and install all the dependencies needed by the project and will compile it.
 * You can open the project with Eclipse and run the examples. You'll need the Gradle plugin for Eclipse.
 
### Running example from console

 ```bashgra
 ./gradlew execute -Pchapter=1 -Pexample=ReactiveSumV1
 ```
 
## Examples
Here are the descriptions of all the examples in the book.

#### 01. Iterator VS Observable
This example is used in the book in the 'Comparing the Iterator pattern and the RxJava Observable' of the first chapter.
It demonstrates the difference between RxJava's Observables and the Iterators, by iterating over a list of strings.
The `Observable.from` method is introduced here for the first time, as well as subscribing.

The example can be found here [ObservableVSIterator](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter01/ObservableVSIterator.java)

#### 02. Reactive Sum, version 1
This is example demonstrates a reactive sum, which is updated on change of any of its collectors. It is demonstrates
many of the features of RxJava, like Observers, Schedulers Observable transformations, filtering and combining.

The example can be found here [ReactiveSumV1](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter01/ReactiveSumV1.java)

#### 03. Introduction to the new syntax and semantics
Demonstrates creating and using lambdas, passing them to methods, that receive Functional Interfaces as parameters and references
to existing methods.

The example can be found here [Java8LambdasSyntaxIntroduction](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/Java8LambdasSyntaxIntroduction.java)

#### 04. Reactive Sum, version 2 (with lambdas)
Another implementation of the 'Reactive Sum', similar to the on of the first chapter, but it uses the new Java 8 syntax with lambdas.

The example can be found here [ReactiveSumV2](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/ReactiveSumV2.java)
 
#### 05. Pure and higher functions
Demonstrates pure and higher order functions. Applies higher order functions to other functions.

The example can be found here [PureAndHigherOrderFunctions](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/PureAndHigherOrderFunctions.java)

#### 06. Introduction to monads
Shows implementation and uses of a monad. The Optional monad.

The example can be found here [Monads](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/Monads.java)
 
#### 07. Observables and monads
Shows that Observables are not true monads. They are monad-like structures though and benefit from that.

The example can be found here [ObservablesAndMonads](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter02/ObservablesAndMonads.java)
 
#### 08. Creating Observables with Observable.from
A set of examples of using the Observable.from method for creating Observables from collections, arrays and Iterables.

The example can be found here [CreatingObservablesWithFrom](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/CreatingObservablesWithFrom.java)

#### 09. Using Observable.from with Future
Demonstrates creating Observables using the Observable.from(Future) method.

The example can be found here [CreatingObservablesWithFromFuture](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/CreatingObservablesWithFromFuture.java)

#### 10. Using the Observable.just method to create Observables
Demonstrates creating Observables using the Observable.just method.

The example can be found here [CreatingObservablesUsingJust](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/CreatingObservablesUsingJust.java)

#### 11. A few factory methods for creating Observables (Chapter 3, pages 43-46)
Demonstrates using Observable.interval, Observable.timer, Observable.error,
Observable.never, Observable.empty and Observable.range for Obsevable creation.

The example can be found here [CreatingObservablesUsingVariousFactoryMethods](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/CreatingObservablesUsingVariousFactoryMethods.java)

#### 12. Demonstration of the Observable.create method (Chapter 3, pages 46-50)
Demonstrates using Observable.create for creating custom Observables.
Contains unsubscribing and implementing unsubscribing logic in Observable.create.

The example can be found here [ObservableCreateExample](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/ObservableCreateExample.java)

#### 13. A ConnectableObservable demonstration (Chapter 3, pages 51-53)
Demonstration of ConnectableObservables and the methods realted to them - publish, refCount, share.

The example can be found here [UsingConnectableObservables](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/UsingConnectableObservables.java)

#### 14. Subjects demonstration (Chapter 3, pages 53-54)
Demonstrates using a Subject to subscribe to an Observables, propagating its notifications to multiple Subscribers.

The example can be found here [SubjectsDemonstration](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/SubjectsDemonstration.java)

#### 15. Reactive Sum, version 3 (with Subjects) (Chapter 3, pages 55-57)
The 'Reactive Sum' is implemented through reactive properties, which are in fact BehaviorSubjects.

The example can be found here [ReactiveSumV3](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter03/ReactiveSumV3.java)

#### 16. Examples of using Observable transformations (Chapter 4, pages 59-66)
Demonstration of using map, flatMap, flatMapIterable and switchMap.

The example can be found here [MappingExamples](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter04/MappingExamples.java)

#### 17. Working with files using flatMap (Chapter 4, pages 60-62)
Demonstration of using flatMap with an Observable created by directory stream,
reading all the files from it, using Observables.

The example can be found here [FlatMapAndFiles](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter04/FlatMapAndFiles.java)

#### 18. Demonstration of using the Observable#groupBy operator (Chapter 4, pages 67-69)
Demonstrates how the groupBy operator can be used.

The example can be found here [UsingGroupBy](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter04/UsingGroupBy.java)

#### 19. Demonstration of various transforming operators (Chapter 4, pages 69-71)
Demonstration of working with the cast, materialize, timestamp and timeInterval operators.

The example can be found here [VariousTransformationsDemonstration](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter04/VariousTransformationsDemonstration.java)

#### 20. Various examples of using filtering operators (Chapter 4, pages 71-75)
Demonstrates the filter, takeLast, last, takeLastBuffer, lastOrDefault,
skipLast, skip, first, elementAt, distinct, distinctUntilChanged and ofType operators.

The example can be found here [FilteringExamples](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter04/FilteringExamples.java)

#### 21. Demonstration of using Observable#scan and more (Chapter 4, pages 76-78)
Demonstrates the scan operator and contains an example of working with data using the majority of the operators learned through the chapter.

The example can be found here [ScanAndUsingMultipleOperators](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter04/ScanAndUsingMultipleOperators.java)

#### 22. Examples of combining Observables (Chapter 5, pages 82-88)
Demonstrates combining Observables using Observable.zip, Observable.merge and Observable.concat.

The example can be found here [CombiningObservables](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter05/CombiningObservables.java)

#### 23. Some examples of using conditionals (Chapter 5, pages 88-91)
Demonstration of using the Observable.amb, Observable.takeWhile, Observable.takeUntil,
Observable.skipUntil and Observable.defaultIfEmpty.

The example can be found here [Conditionals](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter05/Conditionals.java)

#### 24. Examples of handling errors (Chapter 5, pages 92-95)
A demonstrates working with Observable.onErrorReturn, Observable.onErrorResumeNext and Observable.onExceptionResumeNext
as well as retrying with Observable.retry and Observable.retryWhen.

The example can be found here [HandlingErrors](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter05/HandlingErrors.java)

#### 25. Example of doing HTTP requests and handling responses with Observables (Chapter 5, pages 95-99)
Using multiple Observable operators in order to handle and augment an HTTP response from Github.

The example can be found here [HttpRequestsExample](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter05/HttpRequestsExample.java)

#### 26. Observable.interval and Schedulers (Chapter 6, pages 103-105)
More information of Observable.interval and its default Scheduler. Contains an example of debugging information of the emitted items and the current Thread.

The example can be found here [IntervalAndSchedulers](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter06/IntervalAndSchedulers.java)

#### 27. Demonstration of the different Schedulers types (Chapter 6, pages 106-114)
A collection of examples of using the different Schedulers.

The example can be found here [SchedulersTypes](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter06/SchedulersTypes.java)

#### 28. A few examples of observeOn and subscribeOn (Chapter 6, pages 115-119)
Demonstrates using subscribeOn and observeOn with Schedulers and Observables.

The example can be found here [SubscribeOnAndObserveOn](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter06/SubscribeOnAndObserveOn.java)

#### 29. Demonstraton of parallelism (Chapter 6, pages 121-122)
Demonstrates parallelism by executing a number of requests in parallel.

The example can be found here [ParallelRequestsExample](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter06/ParallelRequestsExample.java)

#### 30. Examples demonstrating backpressure and buffering operators (Chapter 6, pages 122-127)
Demonstrates using the Observable#sample, Observable#buffer, Observable#window
Observable#throttleLast, Observable#debounce, Observable#onBackpressureDrop and
Observable#onBackpressureBuffer operators

The example can be found here [BackpressureExamples](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter06/BackpressureExamples.java)

#### 31. A demonstration of using Blocking Observables (Chapter 7, pages 133-136)
Examples of using BlockingObservable and their operators -
BlockingObservable#forEach, BlockingObservable#first, BlockingObservable#next,
BlockingObservable#last and BlockingObservable#single.
Includes examples of Observable#count and Observable#toList combined with the Observable#toBlocking operator.

The example can be found here [BlockingObservablesAndOperators](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter07/BlockingObservablesAndOperators.java)

#### 32. Unit test demonstrating different ways of testing Observables (Chapter 7, pages 131-133, 136-138)
Includes simple subscription test, test with BlockingObservable and test with TestSubscriber.

The example can be found here [SortedObservableTest](https://github.com/meddle0x53/learning-rxjava/blob/master/src/test/java/com/packtpub/reactive/chapter07/SortedObservableTest.java)

#### 33. Example of testing asynchronous Observables (Chapter 7, pages 139-140)
A unit test testing the custom reateObservable#interval method.

The example can be found here [CreateObservableIntervalTest](https://github.com/meddle0x53/learning-rxjava/blob/master/src/test/java/com/packtpub/reactive/chapter07/CreateObservableIntervalTest.java)

#### 34. Resource management demonstration (Chapter 8, pages 142-148)
Demonstration of custom resource management with Observable#using.

The example can be found here [ResourceManagement](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter08/ResourceManagement.java)

#### 35. Example of using Observable#lift for executing custom operators (Chapter 8, pages 148-152)
Demonstrates implementing values with indices using lift and the custom operator Indexed.

The example can be found here [Lift](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter08/Lift.java)

#### 36. Unit test for the Indexed operator (Chapter 8, pages 152-153)

The example can be found here [IndexedTest](https://github.com/meddle0x53/learning-rxjava/blob/master/src/test/java/com/packtpub/reactive/chapter08/IndexedTest.java)

#### 37. Demonstration of the Observable.compose operator (Chapter 8, pages 153)
Example of implementing a Transformer and passing it to Observable#compose.

The example can be found here [Compose](https://github.com/meddle0x53/learning-rxjava/blob/master/src/main/java/com/packtpub/reactive/chapter08/Compose.java)

#### 38. Unit test for the OddFilter Transformer. (Chapter 8, pages 154)

The example can be found here [OddFilterTest](https://github.com/meddle0x53/learning-rxjava/blob/master/src/test/java/com/packtpub/reactive/chapter08/OddFilterTest.java)
