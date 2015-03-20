package com.packtpub.reactive.chapter06;

import static com.packtpub.reactive.common.Helpers.debug;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

/**
 * More information of {@link Observable#interval} and its default {@link Scheduler}.
 * 
 * @author meddle
 */
public class IntervalAndSchedulers implements Program {

	@Override
	public String name() {
		return "Observable.interval and Schedulers";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@Override
	public void run() {
		CountDownLatch latch = new CountDownLatch(1);
		
		Observable.range(5, 5).doOnEach(debug("Test")).subscribe();
		
		Observable
			.interval(500L, TimeUnit.MILLISECONDS)
			.take(5)
			.doOnEach(debug("Default interval"))
			.doOnCompleted(() -> latch.countDown())
			.subscribe();
		
		try {
			latch.await();
		} catch (InterruptedException e) {}

		Observable
		.interval(500L, TimeUnit.MILLISECONDS, Schedulers.immediate())
		.take(5)
		.doOnEach(debug("Imediate interval"))
		.subscribe();
	}
	
	public static void main(String[] args) {
		new IntervalAndSchedulers().run();
	}

}
