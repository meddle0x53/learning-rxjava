package com.packtpub.reactive.chapter06;

import static com.packtpub.reactive.common.Helpers.debug;

import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

/**
 * Demonstrates using subscribeOn and observeOn with {@link Schedulers} and {@link Observable}s.
 *
 * @author meddle
 */
public class SubscribeOnAndObserveOn implements Program {

	@Override
	public String name() {
		return "A few examples of observeOn and subscribeOn";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@Override
	public void run() {
		CountDownLatch latch = new CountDownLatch(1);

		Observable<Integer> range = Observable
				.range(20, 5)
				.flatMap(n -> Observable
						.range(n, 3)
						.subscribeOn(Schedulers.computation())
						.doOnEach(debug("Source"))
						);
		
		
		Observable<Character> chars = range
				.observeOn(Schedulers.newThread())
				.map(n -> n + 48)
				.doOnEach(debug("+48 ", "    "))
				.observeOn(Schedulers.computation())
				.map(n -> Character.toChars(n))
				.map(c -> c[0])
				.doOnEach(debug("Chars ", "    "))
				.finallyDo(() -> latch.countDown());
		
		chars.subscribe();

		System.out.println("Hey!");
		
		try {
			latch.await();
		} catch (InterruptedException e) {}
	}

	public static void main(String[] args) {
		new SubscribeOnAndObserveOn().run();
	}
}
