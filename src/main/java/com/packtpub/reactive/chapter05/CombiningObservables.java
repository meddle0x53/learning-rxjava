package com.packtpub.reactive.chapter05;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;

import com.packtpub.reactive.common.Program;

/**
 * Combining {@link Observable}s using {@link Observable#zip}, {@link Observable#merge} and {@link Observable#concat}.
 * 
 * @author meddle
 */
public class CombiningObservables implements Program {

	@Override
	public String name() {
		return "Examples of combining Observables";
	}

	@Override
	public int chapter() {
		return 5;
	}

	public <T, R> T onlyFirstArg(T arg1, R arg2) {
		return arg1;
	}

	@Override
	public void run() {
		CountDownLatch latch = new CountDownLatch(5);

		Observable<Integer> zip = Observable.zip(Observable.just(1, 3, 4),
				Observable.just(5, 2, 6), (a, b) -> a + b);
		subscribePrint(zip, "Simple zip");

		Observable<String> timedZip = Observable.zip(
				Observable.from(Arrays.asList("Z", "I", "P", "P")),
				Observable.interval(300L, TimeUnit.MILLISECONDS),
				this::onlyFirstArg).doOnCompleted(() -> latch.countDown());
		subscribePrint(timedZip, "Timed zip");

		Observable<String> greetings = Observable.just("Hello", "Hi", "Howdy",
				"Zdravei", "Yo", "Good to see ya").zipWith(
				Observable.interval(1L, TimeUnit.SECONDS), this::onlyFirstArg);

		Observable<String> names = Observable.just("Meddle", "Tanya", "Dali",
				"Joshua").zipWith(
				Observable.interval(1500L, TimeUnit.MILLISECONDS),
				this::onlyFirstArg);

		Observable<String> punctuation = Observable.just(".", "?", "!", "!!!",
				"...").zipWith(
				Observable.interval(1100L, TimeUnit.MILLISECONDS),
				this::onlyFirstArg);

		Observable<String> combined = Observable.combineLatest(greetings,
				names, punctuation, (greeting, name, puntuation) -> greeting
						+ " " + name + puntuation).doOnCompleted(() -> latch.countDown());

		subscribePrint(combined, "Sentences");

		Observable<String> merged = Observable.merge(greetings, names,
				punctuation).delay(7L, TimeUnit.SECONDS).doOnCompleted(() -> latch.countDown());
		subscribePrint(merged, "Words");

		Observable<String> concat = Observable.concat(greetings, names,
				punctuation).delay(13L, TimeUnit.SECONDS).doOnCompleted(() -> latch.countDown());
		subscribePrint(concat, "Concat");

		subscribePrint(
				punctuation.startWith(names).startWith(greetings)
						.delay(30L, TimeUnit.SECONDS).doOnCompleted(() -> latch.countDown()), "SW");
		
		try {
			latch.await();
		} catch (InterruptedException e) {}
	}
	
	public static void main(String[] args) {
		new CombiningObservables().run();
	}

}
