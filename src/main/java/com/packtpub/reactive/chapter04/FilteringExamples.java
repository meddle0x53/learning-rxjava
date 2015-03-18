package com.packtpub.reactive.chapter04;

import java.util.Arrays;

import rx.Observable;

import com.packtpub.reactive.common.Program;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

/**
 * Demonstrates the filter, takeLast, last, takeLastBuffer, lastOrDefault,
 * skipLast, skip, first, elementAt, distinct, distinctUntilChanged and ofType operators.
 * 
 * @author meddle
 */
public class FilteringExamples implements Program {

	@Override
	public String name() {
		return "Various examples of using filtering operators";
	}

	@Override
	public int chapter() {
		return 4;
	}

	@Override
	public void run() {
		Observable<Integer> numbers = Observable.just(1, 13, 32, 45, 21, 8, 98,
				103, 55);

		Observable<Integer> filter = numbers.filter(n -> n % 2 == 0);
		subscribePrint(filter, "Filter");

		subscribePrint(numbers.takeLast(4), "Last 4");
		subscribePrint(numbers.last(), "Last");
		subscribePrint(numbers.takeLastBuffer(4), "Last buffer");

		subscribePrint(numbers.lastOrDefault(200), "Last or default");
		subscribePrint(Observable.empty().lastOrDefault(200), "Last or default");

		subscribePrint(numbers.skipLast(4), "Skip last 4");
		subscribePrint(numbers.skip(4), "Skip 4");

		subscribePrint(numbers.take(4), "First 4");

		subscribePrint(numbers.first(), "First");

		subscribePrint(numbers.elementAt(5), "At 5");

		Observable<String> words = Observable.just("One", "of", "the", "few",
				"of", "the", "crew", "crew");
		subscribePrint(words.distinct(), "Distinct");
		subscribePrint(words.distinctUntilChanged(), "Distinct until changed");

		Observable<?> various = Observable.from(Arrays.asList("1", 2, 3.0, 4,
				5L));

		subscribePrint(various.ofType(Integer.class), "Only integers");
	}

	public static void main(String[] args) {
		new FilteringExamples().run();
	}

}
