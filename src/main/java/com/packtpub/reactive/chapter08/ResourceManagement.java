package com.packtpub.reactive.chapter08;

import java.io.IOException;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;

import rx.Observable;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import com.packtpub.reactive.common.Program;

/**
 * Demonstration of custom resource management with {@link Observable#using}.
 * 
 * @author meddle
 */
public class ResourceManagement implements Program {

	@Override
	public String name() {
		return "Resource management demonstration";
	}

	@Override
	public int chapter() {
		return 8;
	}
	
	public Observable<ObservableHttpResponse> request(String url) {
		Func0<CloseableHttpAsyncClient> resourceFactory = () -> {
			CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
			client.start();
			
			System.out.println(Thread.currentThread().getName() + " : Created and started the client.");
			return client;
		};
		
		Func1<HttpAsyncClient, Observable<ObservableHttpResponse>> observableFactory = (client) -> {
			System.out.println(Thread.currentThread().getName() + " : About to create Observable.");
			return ObservableHttp.createGet(url, client).toObservable();
		};

		Action1<CloseableHttpAsyncClient> disposeAction = (client) -> {
			try {
				System.out.println(Thread.currentThread().getName() + " : Closing the client.");
				client.close();
			} catch (IOException e) {
			}
		};

		return Observable.using(
				resourceFactory,
				observableFactory,
				disposeAction);
	}

	@Override
	public void run() {
		
		String url = "https://api.github.com/orgs/ReactiveX/repos";
		Observable<ObservableHttpResponse> response = request(url);
		
		System.out.println("Not yet subscribed.");
		Observable<String> stringResponse = response
				.<String>flatMap(resp -> resp.getContent()
				.map(bytes -> new String(bytes, java.nio.charset.StandardCharsets.UTF_8)))
				.retry(5)
				.map(String::trim)
				.cache();

		System.out.println("Subscribe 1:");
		System.out.println(stringResponse.toBlocking().first());

		System.out.println("Subscribe 2:");
		System.out.println(stringResponse.toBlocking().first());
		
	}
	
	public static void main(String[] args) {
		new ResourceManagement().run();
	}


}
