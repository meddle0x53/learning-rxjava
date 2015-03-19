package com.packtpub.reactive.chapter06;

import static com.packtpub.reactive.common.Helpers.debug;
import rx.Observable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

public class SchedulersExamples implements Program {

	@Override
	public String name() {
		return "A few examples of how Schedulers work.";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@Override
	public void run() {
		Object monitor = new Object();

		Action0 free = () -> {
			synchronized (monitor) {
				monitor.notify();
			}
		};

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
				.doOnCompleted(free);
		
		chars.subscribe();

		System.out.println("Hey!");
		
		synchronized (monitor) {
			try {
				monitor.wait(5000L);
			} catch (InterruptedException e) {}
		}
	}

}
