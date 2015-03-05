package com.packtpub.reactive.four;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

import com.packtpub.reactive.common.Program;

public class OtherTransformationExample implements Program {

	@Override
	public String name() {
		return "Demonstrates working with the cast and timestamp operators.";
	}

	@Override
	public int chapter() {
		return 4;
	}

	@Override
	public void run() {
		List<Number> list = Arrays.asList(1, 2, 3);
		Observable<Integer> iObs = Observable.from(list).cast(Integer.class);

		subscribePrint(iObs, "Integers");

		Observable<Timestamped<Number>> timestamp = Observable.from(list)
				.timestamp();

		subscribePrint(timestamp, "Timestamps");

		Observable<TimeInterval<Long>> timeInterval = Observable.timer(0L,
				150L, TimeUnit.MILLISECONDS).timeInterval();

		Subscription sub = subscribePrint(timeInterval, "Time intervals");

		Observable<Notification<Number>> materialized = Observable.from(list)
				.materialize();
		subscribePrint(materialized, "Materialized");

		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
		}
		sub.unsubscribe();
	}

}
