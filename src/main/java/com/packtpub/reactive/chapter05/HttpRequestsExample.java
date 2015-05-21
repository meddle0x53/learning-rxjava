package com.packtpub.reactive.chapter05;

import static com.packtpub.reactive.common.Helpers.blockingSubscribePrint;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;

import rx.Observable;
import rx.apache.http.ObservableHttp;

import com.google.gson.Gson;
import com.packtpub.reactive.common.Program;

/**
 * Using multiple {@link Observable} operators in order to handle and augment an HTTP response
 * from Github.
 * 
 * @author meddle
 */
public class HttpRequestsExample implements Program {

	@Override
	public String name() {
		return "Example of doing HTTP requests and handling responses with Observables";
	}

	@Override
	public int chapter() {
		return 5;
	}
	
	private Map<String, Set<Map<String, Object>>> cache = new ConcurrentHashMap<>();
	
	public Observable<Map<String, Object>> fromCache(String url) {
		return Observable.from(getCache(url)).defaultIfEmpty(null)
				.flatMap(json -> (json == null) ? Observable.never() : Observable.just(json))
				.doOnNext(json -> json.put("json_cached", true));
	}

	public Set<Map<String, Object>> getCache(String url) {
		if (!cache.containsKey(url)) {
			cache.put(url, new HashSet<Map<String,Object>>());
		}
		return cache.get(url);
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void run() {
		try(CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {

			client.start();

			String username = "meddle0x53";

			Observable<Map> resp = githubUserInfoRequest(client, username);
			
			blockingSubscribePrint(resp
					.map(json -> json.get("name") + "(" + json.get("language") + ")")
					, "Json");
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private Observable<Map> githubUserInfoRequest(
			HttpAsyncClient client, String githubUser) {
		if (githubUser == null) {
			return Observable.<Map>error(new NullPointerException("Github user must not be null!"));
		}

		String url = "https://api.github.com/users/" + githubUser + "/repos";

		Observable<Map> response = requestJson(client, url);
		return response
				.filter(json -> json.containsKey("git_url"))
				.filter(json -> json.get("fork").equals(false));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Observable<Map> requestJson(HttpAsyncClient client,
			String url) {
		Observable<String> rawResponse = ObservableHttp
						.createGet(url, client)
						.toObservable()
						.flatMap(resp -> resp.getContent()
								.map(bytes -> new String(bytes, java.nio.charset.StandardCharsets.UTF_8)))
								.retry(5)
								.cast(String.class)
								.map(String::trim)
								.doOnNext(resp -> getCache(url).clear());

		Observable<String> objects = rawResponse
			.filter(data -> data.startsWith("{"))
			.map(data -> "[" + data + "]");
		
		Observable<String> arrays = rawResponse
						.filter(data -> data.startsWith("["));
		
		Observable<Map> response = arrays.ambWith(objects)
						.map(data -> {
							return new Gson().fromJson(data, List.class);
						}).flatMapIterable(list -> list)
						.cast(Map.class)
						.doOnNext(json -> getCache(url).add((Map<String, Object>) json));

		return Observable.amb(fromCache(url), response);
	}

	public static void main(String[] args) {
		new HttpRequestsExample().run();
	}
}
