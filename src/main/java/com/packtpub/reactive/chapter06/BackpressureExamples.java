package com.packtpub.reactive.chapter06;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Helpers;
import com.packtpub.reactive.common.Program;

/**
 * Demonstrates using the {@link Observable#sample}, {@link Observable#buffer}, {@link Observable#window}
 * {@link Observable#throttleLast}, {@link Observable#debounce}, {@link Observable#onBackpressureDrop} and
 * {@link Observable#onBackpressureBuffer} operators.
 * 
 * @author meddle
 */
public class BackpressureExamples implements Program {

	@Override
	public String name() {
		return "Examples demonstrating backpressure and buffering operators";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@Override
	public void run() {

		CountDownLatch latch = new CountDownLatch(7);
		
		Path path = Paths.get("src", "main", "resources");
		
		Observable<?> data = CreateObservable.listFolderViaUsing(path, "*")
		.flatMap(file -> {
			if (!Files.isDirectory(file)) {
				return CreateObservable.fromViaUsing(file).subscribeOn(Schedulers.io());
			}
			
			return Observable.empty();
		});
		
		Helpers.subscribePrint(
				data.sample(Observable.interval(
						100L, TimeUnit.MILLISECONDS).take(10)
						.concatWith(Observable.interval(
								200L, TimeUnit.MILLISECONDS)))
				.doOnCompleted(() -> latch.countDown()),
				"sample(Observable)");

		Helpers.subscribePrint(
				data.throttleLast(100L, TimeUnit.MILLISECONDS)
				.doOnCompleted(() -> latch.countDown()),
				"throttleLast(long, TimeUnit)");
		Observable<Object> sampler = Observable.create(subscriber -> {
			subscriber.onNext(0);
			
			try {
				Thread.sleep(100L);

				subscriber.onNext(10);

				Thread.sleep(200L);

				subscriber.onNext(200);
				
				Thread.sleep(150L);
				subscriber.onCompleted();
			} catch (Exception e) {
				subscriber.onError(e);
			}
		}).repeat().subscribeOn(Schedulers.computation());
		

		Helpers.subscribePrint(
				data.sample(sampler).debounce(150L, TimeUnit.MILLISECONDS)
				.doOnCompleted(() -> latch.countDown()),
				"sample(Observable).debounce(long, TimeUnit)");
		

		Helpers.subscribePrint(
				data.buffer(2, 1500)
				.doOnCompleted(() -> latch.countDown()),
				"buffer(int, int)");
		

		Helpers.subscribePrint(
				data.window(3L, 200L, TimeUnit.MILLISECONDS).flatMap(o -> o)
				.doOnCompleted(() -> latch.countDown()),
				"window(long, long, TimeUnit)");
		
		
		Helpers.subscribePrint(
				data.onBackpressureBuffer(10000)
				.doOnCompleted(() -> latch.countDown()),
				"onBackpressureBuffer(int)");

		Helpers.subscribePrint(
				data.onBackpressureDrop()
				.doOnCompleted(() -> latch.countDown()),
				"onBackpressureDrop");

		try {
			latch.await(15L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
	}
	
	public static void main(String[] args) {
		new BackpressureExamples().run();
	}

}
