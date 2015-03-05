package com.packtpub.reactive.six;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Helpers;
import com.packtpub.reactive.common.Program;

public class BufferingExamples implements Program {

	@Override
	public String name() {
		return "Examples demonstrating buffering.";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@Override
	public void run() {

		
		Path path = Paths.get("src", "main", "resources");
		
		Observable<?> data = CreateObservable.listFolder(path, "*")
		.flatMap(file -> {
			if (!Files.isDirectory(file)) {
				return CreateObservable.from(file).subscribeOn(Schedulers.io());
			}
			
			return Observable.empty();
		});
		
		Helpers.subscribePrint(
				data.sample(Observable.interval(100L, TimeUnit.MILLISECONDS)),
				"sample(Observable)");

		Helpers.subscribePrint(
				data.throttleLast(100L, TimeUnit.MILLISECONDS),
				"throttleLast(long, TimeUnit)");
		
		Observable<Object> sampler = Observable.create(subscriber -> {
			subscriber.onNext(0);
			
			try {
				Thread.sleep(10L);

				subscriber.onNext(10);

				Thread.sleep(20L);

				subscriber.onNext(20);
				
				Thread.sleep(15L);
				subscriber.onCompleted();
			} catch (Exception e) {
				subscriber.onError(e);
			}
		}).repeat().subscribeOn(Schedulers.computation());
		

		Helpers.subscribePrint(
				data.sample(sampler).debounce(15L, TimeUnit.MILLISECONDS),
				"sample(Observable).debounce(long, TimeUnit)");
		

		Helpers.subscribePrint(
				data.buffer(2, 1500),
				"buffer(int, int)");
		

		Helpers.subscribePrint(
				data.window(3L, 200L, TimeUnit.MILLISECONDS).flatMap(o -> o),
				"window(long, long, TimeUnit)");
		
		
		Helpers.subscribePrint(
				data.onBackpressureBuffer(10000),
				"onBackpressureBuffer(int)");

		Helpers.subscribePrint(
				data.onBackpressureDrop(),
				"onBackpressureDrop");

		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
		}
	}

}
