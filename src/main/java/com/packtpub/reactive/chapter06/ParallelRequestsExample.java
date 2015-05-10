package com.packtpub.reactive.chapter06;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;


/**
 * Demonstrates parallelism by executing a number of requests in parallel.
 * 
 * @author meddle
 */
public class ParallelRequestsExample implements Program {

	@Override
	public String name() {
		return "Demonstraton of parallelism";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void run() {
		CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
		CountDownLatch latch = new CountDownLatch(1);
		
		try {
			client.start();
			
			Observable<Map> response = CreateObservable.requestJson(client, "https://api.github.com/users/meddle0x53/followers");
			
			
			response
			.map(followerJson -> followerJson.get("url"))
			.cast(String.class)
			.flatMap(profileUrl -> CreateObservable
						.requestJson(client, profileUrl)
						.subscribeOn(Schedulers.io())
						.filter(res -> res.containsKey("followers"))
						.map(json -> json.get("login") +  " : " + json.get("followers"))
			)
			.doOnNext(follower -> System.out.println(follower))
			.count()
			.doOnCompleted(() -> latch.countDown())
			.subscribe(sum -> System.out.println("meddle0x53 : " + sum));

			try {
				latch.await();
			} catch (InterruptedException e) {}

		} finally {
			try {
				client.close();
			} catch (IOException e) {}
		}
	}
	
	public static void main(String[] args) {
		new ParallelRequestsExample().run();
	}

}
