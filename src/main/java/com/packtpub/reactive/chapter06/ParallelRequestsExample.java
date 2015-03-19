package com.packtpub.reactive.chapter06;

import java.io.IOException;
import java.util.Map;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;


public class ParallelRequestsExample implements Program {

	@Override
	public String name() {
		return "Demonstrates parallel requests.";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void run() {
		try(CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
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
			.reduce(0.0, (sum, cur) -> sum + 1)
			.subscribe(sum -> System.out.println("meddle0x53 : " + sum));

			try {
				Thread.sleep(6000L);
			} catch (InterruptedException e) {}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
