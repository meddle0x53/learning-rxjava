package com.packtpub.reactive.chapter05;

import static com.packtpub.reactive.common.Helpers.subscribePrint;
import static com.packtpub.reactive.common.Helpers.blockingSubscribePrint;

import java.util.Arrays;
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

	public <T> Observable<T> slowDown(Observable<T> o, long ms) {
		return o.zipWith(Observable.interval(ms, TimeUnit.MILLISECONDS), (elem, i) -> elem);
	}

	@Override
	public void run() {
		Observable<Integer> zip = Observable.zip(
				Observable.just(1, 3, 4),
				Observable.just(5, 2, 6),
				(a, b) -> a + b);
		subscribePrint(zip, "Simple zip");

		Observable<String> timedZip =
				slowDown(Observable.from(Arrays.asList("Z", "I", "P", "P")), 300);
		blockingSubscribePrint(timedZip, "Timed zip");

		Observable<String> greetings =
				slowDown(Observable.just("Hello", "Hi", "Howdy", "Zdravei", "Yo", "Good to see ya"), 1000);

		Observable<String> names =
				slowDown(Observable.just("Meddle", "Tanya", "Dali", "Joshua"), 1500L);

		Observable<String> punctuation =
				slowDown(Observable.just(".", "?", "!", "!!!", "..."), 1100L);

		Observable<String> combined = Observable.combineLatest(
				greetings, names, punctuation,
				(greeting, name, puntuation) -> greeting + " " + name + puntuation);

		blockingSubscribePrint(combined, "Sentences");

		Observable<String> merged = Observable.merge(greetings, names, punctuation);
		blockingSubscribePrint(merged, "Words");

		Observable<String> concat = Observable.concat(greetings, names, punctuation);
		blockingSubscribePrint(concat, "Concat");

		blockingSubscribePrint(punctuation.startWith(names).startWith(greetings), "SW");
	}
	
	public static void main(String[] args) {
		new CombiningObservables().run();
	}

}
