package com.packtpub.reactive.chapter03;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

/**
 * Show case of the Observable.create method.
 * Contains a simple implementation of the Observable.from(Iterable) method, using Observable.create.
 * 
 * @author meddle
 */
public class ObservableCreateExample implements Program {

	@Override
	public String name() {
		return "Demonstration of the Observable.create method";
	}

	@Override
	public int chapter() {
		return 3;
	}

	public static <T> Observable<T> fromIterable(final Iterable<T> iterable) {
		return Observable.create(new OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> subscriber) {
				try {
					Iterator<T> iterator = iterable.iterator();

					while (iterator.hasNext()) {
						if (subscriber.isUnsubscribed()) {
							return;
						}
						subscriber.onNext(iterator.next());
					}

					if (!subscriber.isUnsubscribed()) {
						subscriber.onCompleted();
					}
				} catch (Exception e) {
					if (!subscriber.isUnsubscribed()) {
						subscriber.onError(e);
					}
				}

			}
		});
	}

	@Override
	public void run() {
		subscribePrint(fromIterable(Arrays.asList(1, 3, 5)), "List1");
		subscribePrint(fromIterable(Arrays.asList(2, 4, 6)), "List2");

		try {
			Path path = Paths.get("src", "main", "resources", "lorem_big.txt");
			List<String> data = Files.readAllLines(path);

			Observable<String> observable = fromIterable(data).subscribeOn(
					Schedulers.computation());

			Subscription subscription = subscribePrint(observable, "File");
			System.out.println("Before unsubscribe!");
			System.out.println("-------------------");

			subscription.unsubscribe();

			System.out.println("-------------------");
			System.out.println("After unsubscribe!");

			Thread.sleep(100L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ObservableCreateExample().run();
	}

}
