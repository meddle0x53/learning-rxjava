package com.packtpub.reactive.chapter03;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;

import com.packtpub.reactive.common.Program;

/**
 * Demonstrates how to create and use ConnectableObservables.
 * 
 * @author meddle
 */
public class UsingConnectableObservables implements Program {

	@Override
	public String name() {
		return "A ConnectableObservable demonstration";
	}

	@Override
	public int chapter() {
		return 3;
	}

	@Override
	public void run() {
		Observable<Long> interval = Observable.interval(100L,
				TimeUnit.MILLISECONDS);
		ConnectableObservable<Long> published = interval.publish();

		Subscription sub1 = subscribePrint(published, "First");
		Subscription sub2 = subscribePrint(published, "Second");

		published.connect();

		Subscription sub3 = null;
		try {
			Thread.sleep(300L);

			sub3 = subscribePrint(published, "Third");
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		sub1.unsubscribe();
		sub2.unsubscribe();
		sub3.unsubscribe();

		System.out.println("-----------------------------------");

		Observable<Long> refCount = interval.share(); // publish().refCount();

		sub1 = subscribePrint(refCount, "First");
		sub2 = subscribePrint(refCount, "Second");

		sub3 = null;
		try {
			Thread.sleep(300L);

			sub3 = subscribePrint(refCount, "Third");
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		sub1.unsubscribe();
		sub2.unsubscribe();
		sub3.unsubscribe();

		Subscription sub4 = subscribePrint(refCount, "Fourth");

		try {
			Thread.sleep(300L);
		} catch (InterruptedException e) {
		}
		sub4.unsubscribe();
	}
	
	public static void main(String[] args) {
		new UsingConnectableObservables().run();
	}
}
