package com.packtpub.reactive.chapter07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;

import com.packtpub.reactive.common.CreateObservable;

/**
 * Demonstrates testing the same thing using different methods.
 * 
 * @author meddle
 */
public class SortedObservableTest {
	
	private Observable<String> tested;
	private List<String> expected;

	@Before
	public void before() {
		tested = CreateObservable.<String>sorted(
				(a, b) -> a.compareTo(b),
				"Star", "Bar", "Car", "War", "Far", "Jar");
		expected = Arrays.asList("Bar", "Car", "Far", "Jar", "Star", "War");
	}

	@After
	public void after() {
		tested = null;
		expected = null;
	}
	
	@Test
	public void testUsingNormalSubscription() {
		AtomicBoolean hasError = new AtomicBoolean(false);
		AtomicBoolean isCompleted = new AtomicBoolean(false);
		List<String> result = new ArrayList<String>(6);
		
		tested.subscribe(
				(v) -> result.add(v),
				(e) -> hasError.set(true),
				() -> isCompleted.set(true));
		
		Assert.assertTrue(isCompleted.get());
		Assert.assertFalse(hasError.get());
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testUsingBlockingObservable() {
		List<String> result = tested.toList().toBlocking().single();
		
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testUsingTestSubscriber() {
		TestSubscriber<String> subscriber = new TestSubscriber<String>();
		tested.subscribe(subscriber);
		
		Assert.assertEquals(expected, subscriber.getOnNextEvents());
		Assert.assertSame(1, subscriber.getOnCompletedEvents().size());
		Assert.assertTrue(subscriber.getOnErrorEvents().isEmpty());
		Assert.assertTrue(subscriber.isUnsubscribed());
		Assert.assertSame(Thread.currentThread(), subscriber.getLastSeenThread());
	}
	
	@Test
	public void testUsingTestSubscriberAssertions() {
		TestSubscriber<String> subscriber = new TestSubscriber<String>();
		tested.subscribe(subscriber);
		
		subscriber.assertReceivedOnNext(expected);
		subscriber.assertTerminalEvent();
		subscriber.assertNoErrors();
		subscriber.assertUnsubscribed();
	}
}
