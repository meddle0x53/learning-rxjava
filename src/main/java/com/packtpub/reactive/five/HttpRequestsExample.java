package com.packtpub.reactive.five;

import static com.packtpub.reactive.common.Helpers.subscribePrint;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;

import rx.Observable;
import rx.apache.http.ObservableHttp;

import com.google.gson.Gson;
import com.packtpub.reactive.common.CreateObservable;
import com.packtpub.reactive.common.Program;

public class HttpRequestsExample implements Program {

	@Override
	public String name() {
		return "Example of doing HTTP requests and handling responses with Observables.";
	}

	@Override
	public int chapter() {
		return 5;
	}
	
	private Map<String, Set<Map<String, Object>>> cache = new HashMap<>();
	
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
			
			subscribePrint(resp.map(json -> json.get("name") + "(" + json.get("language") + ")"), "Json");

			try {
				Thread.sleep(6000L);
			} catch (InterruptedException e) {}

			resp = githubUserInfoRequest(client, username);
			
			Observable<String> starGazers = resp
				.map(json -> json.get("stargazers_url"))
				.cast(String.class)
				.flatMap(stargazersUrl -> requestJson(client, stargazersUrl))
				.filter(json -> json.containsKey("login"))
				.map(json -> json.get("login"))
				.cast(String.class)
				.filter(user -> !user.equals(username))
				.distinct()
				.take(5)
				.flatMap(supporter -> githubUserInfoRequest(client, supporter).take(3))
				.filter(json -> json.containsKey("full_name"))
				.map(json -> json.get("full_name"))
				.cast(String.class);
			
			subscribePrint(starGazers, "Star Gazers");


			try {
				Thread.sleep(6000L);
			} catch (InterruptedException e) {}
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

		Observable<Map> response = CreateObservable.requestJson(client, url);
		return response
				.filter(json -> ((Map) json).containsKey("private"))
				.filter(json -> ((Map) json).get("private").equals(false))
				.filter(json -> ((Map) json).get("fork").equals(false));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Observable<Map> requestJson(HttpAsyncClient client,
			String url) {
		Observable<String> rawResponse = ObservableHttp
						.createGet(url, client)
						.toObservable()
						.flatMap(resp -> resp.getContent()
								.map(bytes -> new String(bytes)))
								.retry(5)
								.cast(String.class)
								.map(String::trim)
								.doOnNext(resp -> getCache(url).clear());

		Observable<String> objects = rawResponse
			.filter(data -> data.startsWith("{"))
			.map(data -> "[" + data + "]");
		
		Observable<String> arrays = rawResponse
						.filter(data -> data.startsWith("["));
		
		Observable<Map> response = arrays.concatWith(objects)
						.map(data -> {
							return new Gson().fromJson(data, List.class);
						}).flatMapIterable(list -> list)
						.cast(Map.class)
						.doOnNext(json -> getCache(url).add((Map) json));

		return Observable.amb(fromCache(url), response);
	}

}
