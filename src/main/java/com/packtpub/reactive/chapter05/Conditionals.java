package com.packtpub.reactive.chapter05;

import static com.packtpub.reactive.common.Helpers.blockingSubscribePrint;
import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;

import com.packtpub.reactive.common.Program;

/**
 * Demonstration of using the {@link Observable#amb}, {@link Observable#takeWhile}, {@link Observable#takeUntil},
 * {@link Observable#skipUntil} and {@link Observable#defaultIfEmpty}.
 * 
 * @author meddle
 */
public class Conditionals implements Program {

	@Override
	public String name() {
		return "Some examples of using conditionals";
	}

	@Override
	public int chapter() {
		return 5;
	}

	@Override
	public void run() {
		Observable<String> words = Observable.just("Some", "Other");
		Observable<Long> interval = Observable
				.interval(500L, TimeUnit.MILLISECONDS).take(2);

		Observable<? extends Object> amb = Observable.amb(words, interval);
		blockingSubscribePrint(amb, "Amb 1");

		Random r = new Random();
		Observable<String> source1 = Observable.just("data from source 1")
				.delay(r.nextInt(1000), TimeUnit.MILLISECONDS);
		Observable<String> source2 = Observable.just("data from source 2")
				.delay(r.nextInt(1000), TimeUnit.MILLISECONDS);
		blockingSubscribePrint(Observable.amb(source1, source2), "Amb 2");
		
		words = Observable.just("one", "way", "or", "another", "I'll", "learn", "RxJava")
				.zipWith(Observable.interval(200L, TimeUnit.MILLISECONDS), (x, y) -> x);
		
		blockingSubscribePrint(words.takeUntil(interval).delay(1L, TimeUnit.SECONDS), "takeUntil");
		blockingSubscribePrint(words.delay(800L, TimeUnit.MILLISECONDS).takeWhile(
				word -> word.length() > 2), "takeWhile");


		blockingSubscribePrint(words.skipUntil(interval), "skipUntil");
		
		
		Observable<Object> test = Observable.empty().defaultIfEmpty(5);
		subscribePrint(test, "defaultIfEmpty");
	}

	public static void main(String[] args) {
		new Conditionals().run();
	}
}
