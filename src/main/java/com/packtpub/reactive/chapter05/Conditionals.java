package com.packtpub.reactive.chapter05;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.concurrent.CountDownLatch;
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
		CountDownLatch latch = new CountDownLatch(5);

		Observable<String> words = Observable.just("Some", "Other");
		Observable<Long> interval = Observable
				.interval(500L, TimeUnit.MILLISECONDS).take(2);

		Observable<? extends Object> amb = Observable.amb(words, interval)
				.doOnCompleted(() -> latch.countDown());
		subscribePrint(amb, "Amb 1");

		amb = Observable.amb(words.zipWith(interval, (x, y) -> x), interval)
				.doOnCompleted(() -> latch.countDown());
		subscribePrint(amb, "Amb 2");
		

		amb = Observable.amb(interval, words.zipWith(interval, (x, y) -> x))
				.doOnCompleted(() -> latch.countDown());
		subscribePrint(amb, "Amb 3");
		
		
		words = Observable.just("one", "way", "or", "another", "I'll", "learn", "RxJava")
				.zipWith(Observable.interval(200L, TimeUnit.MILLISECONDS), (x, y) -> x);
		
		subscribePrint(words.takeUntil(interval).delay(1L, TimeUnit.SECONDS)
				.doOnCompleted(() -> latch.countDown()), "takeUntil");
		subscribePrint(words.delay(800L, TimeUnit.MILLISECONDS).takeWhile(word -> word.length() > 2)
				.doOnCompleted(() -> latch.countDown()), "takeWhile");

		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
		}

		subscribePrint(words.skipUntil(interval), "skipUntil");
		
		
		Observable<Object> test = Observable.empty().defaultIfEmpty(5);
		subscribePrint(test, "defaultIfEmpty");
		
		try {
			latch.await();
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) {
		new Conditionals().run();
	}
}
