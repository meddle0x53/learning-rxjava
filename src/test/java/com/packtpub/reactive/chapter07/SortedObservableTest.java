package com.packtpub.reactive.chapter07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	private class TestData {
		private Throwable error = null;
		private boolean completed = false;
		private List<String> result = new ArrayList<String>();
		
		public void setError(Throwable error) {
			this.error = error;
		}

		public boolean isCompleted() {
			return completed;
		}

		public void setCompleted(boolean completed) {
			this.completed = completed;
		}

		public List<String> getResult() {
			return result;
		}

		public Throwable getError() {
			return error;
		}
	}
	
	@Test
	public void testUsingNormalSubscription() {
		TestData data = new TestData();
		
		tested.subscribe(
				(v) -> data.getResult().add(v),
				(e) -> data.setError(e),
				() -> data.setCompleted(true));
		
		Assert.assertTrue(data.isCompleted());
		Assert.assertNull(data.getError());
		Assert.assertEquals(expected, data.getResult());
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
