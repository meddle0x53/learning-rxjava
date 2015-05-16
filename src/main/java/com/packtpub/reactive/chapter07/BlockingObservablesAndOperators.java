package com.packtpub.reactive.chapter07;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observables.BlockingObservable;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;

/**
 * Examples of using {@link BlockingObservable} and their operators -
 * {@link BlockingObservable#forEach}, {@link BlockingObservable#first}, {@link BlockingObservable#next},
 * {@link BlockingObservable#last} and {@link BlockingObservable#single}.
 * Includes examples of {@link Observable#count} and {@link Observable#toList} combined with the {@link Observable#toBlocking}.
 * 
 * @author meddle
 */
public class BlockingObservablesAndOperators implements Program {

	@Override
	public String name() {
		return "A demonstration of using Blocking Observables";
	}

	@Override
	public int chapter() {
		return 7;
	}

	@Override
	public void run() {

		
		Observable
			.interval(100L, TimeUnit.MILLISECONDS)
			.take(5)
			.toBlocking()
			.forEach(System.out::println);
		System.out.println("END");
		
		Integer first = Observable.range(3, 13).toBlocking().first();
		System.out.println(first);
		Integer last = Observable.range(3, 13).toBlocking().last();
		System.out.println(last);
		
		Iterable<Long> next = Observable
				.interval(100L, TimeUnit.MILLISECONDS)
				.toBlocking()
				.next();
		Iterator<Long> iterator = next.iterator();
		System.out.println(iterator.next());
		System.out.println(iterator.next());
		System.out.println(iterator.next());
		
		Iterable<Long> latest = Observable
				.interval(1000L, TimeUnit.MILLISECONDS)
				.toBlocking()
				.latest();
		iterator = latest.iterator();
		System.out.println(iterator.next());
		
		try {
			Thread.sleep(5500L);
		} catch (InterruptedException e) {}
		System.out.println(iterator.next());
		System.out.println(iterator.next());

		
		List<Integer> single = Observable
				.range(5, 15)
				.toList()
				.toBlocking().single();
		System.out.println(single);
		
		Observable.range(10, 100).count().subscribe(System.out::println);
		
		
		StringReader reader = new StringReader("One\nTwo\nThree");
		Observable<String> observable = CreateObservable.from(reader);

		System.out.println(observable.count().toBlocking().first());
		System.out.println(observable.toList().toBlocking().first());
	}
	
	public static void main(String[] args) {
		new BlockingObservablesAndOperators().run();
	}

}
