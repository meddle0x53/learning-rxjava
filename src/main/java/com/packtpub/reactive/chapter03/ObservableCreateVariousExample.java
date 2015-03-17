package com.packtpub.reactive.chapter03;

import java.util.concurrent.TimeUnit;

import rx.Observable;

import com.packtpub.reactive.common.Program;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

public class ObservableCreateVariousExample implements Program {

	@Override
	public String name() {
		return "A few factory methods for creating Observables";
	}

	@Override
	public int chapter() {
		return 3;
	}

	@Override
	public void run() {
		subscribePrint(Observable.interval(500L, TimeUnit.MILLISECONDS),
				"Interval Observable");
		subscribePrint(Observable.timer(0L, 1L, TimeUnit.SECONDS),
				"Timed Interval Observable");
		subscribePrint(Observable.timer(1L, TimeUnit.SECONDS),
				"Timer Observable");
		subscribePrint(Observable.error(new Exception("Test Error!")),
				"Error Observable");
		subscribePrint(Observable.range(1, 10), "Range Observable");
		subscribePrint(Observable.empty(), "Empty Observable");
		subscribePrint(Observable.never(), "Never Observable");

		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
		}

	}

}
