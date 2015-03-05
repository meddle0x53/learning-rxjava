package com.packtpub.reactive.six;

import static com.packtpub.reactive.common.Helpers.debug;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

public class IntervalAndSchedulers implements Program {

	@Override
	public String name() {
		return "Observable.interval and Schedulers.";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@Override
	public void run() {
		Object monitor = new Object();

		Action0 free = () -> {
			System.out.println("Freeing main Thread");
			synchronized (monitor) {
				monitor.notify();
			}
		};
		
		Observable.range(5, 5).doOnEach(debug("Test")).subscribe();
		
		Observable
			.interval(500L, TimeUnit.MILLISECONDS)
			.take(5)
			.doOnEach(debug("Default interval"))
			.doOnCompleted(free)
			.subscribe();
		
		synchronized (monitor) {
			try {
				monitor.wait(5000L);
			} catch (InterruptedException e) {}
		}
		
		Observable
		.interval(500L, TimeUnit.MILLISECONDS, Schedulers.immediate())
		.take(5)
		.doOnEach(debug("Imediate interval"))
		.doOnCompleted(free)
		.subscribe();
	}

}
