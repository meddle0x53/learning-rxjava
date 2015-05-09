package com.packtpub.reactive.common;

import static com.packtpub.reactive.common.checked.Uncheck.unchecked;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;

import rx.Observable;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.apache.http.ObservableHttp;
import rx.apache.http.ObservableHttpResponse;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Contains a set of methods for creating custom {@link Observable}s.
 * 
 * @author meddle
 */
public class CreateObservable {

	public static Observable<String> fromViaUsing(final Path path) {
		return Observable.<String, BufferedReader> using(
			unchecked(() -> Files.newBufferedReader(path)),
			reader -> from(reader).refCount(),
			unchecked(reader -> reader.close()));
	}

	public static Observable<String> from(final Path path) {
		return Observable.<String>create(subscriber -> {
			try {
				BufferedReader reader = Files.newBufferedReader(path);
				subscriber.add(Subscriptions.create(() -> {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));
				
				String line = null;
				while ((line = reader.readLine()) != null && !subscriber.isUnsubscribed()) {
					subscriber.onNext(line);
				}
				if (!subscriber.isUnsubscribed()) {
					subscriber.onCompleted();
				}
			} catch (IOException ioe) {
				if (!subscriber.isUnsubscribed()) {
					subscriber.onError(ioe);
				}
			}
		});
	}

	public static Observable<String> from(final Path path, Scheduler scheduler) {
		return fromViaUsing(path).subscribeOn(scheduler);
	}

	public static ConnectableObservable<String> from(final InputStream stream) {
		return from(new BufferedReader(new InputStreamReader(stream)));
	}

	public static Observable<String> from(final Reader reader) {
		return Observable.defer(() -> {
			return from(new BufferedReader(reader)).refCount();
		}).cache();
	}

	public static ConnectableObservable<String> from(final BufferedReader reader) {
		return Observable.create((Subscriber<? super String> subscriber) -> {
			try {
				String line;

				if (subscriber.isUnsubscribed()) {
					return;
				}

				while (!subscriber.isUnsubscribed() && (line = reader.readLine()) != null) {
					if (line.equals("exit")) {
						break;
					}

					subscriber.onNext(line);
				}
			} catch (IOException e) {
				subscriber.onError(e);
			}

			if (!subscriber.isUnsubscribed()) {
				subscriber.onCompleted();
			}
		}).publish();
	}

	public static Observable<Path> listFolder(Path dir, String glob) {
		return Observable.<Path>create(subscriber -> {
			try {
				DirectoryStream<Path> stream =
						Files.newDirectoryStream(dir, glob);
				
				subscriber.add(Subscriptions.create(() -> {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));
				Observable.<Path>from(stream).subscribe(subscriber);
			} catch (DirectoryIteratorException ex) {
				subscriber.onError(ex);
			} catch (IOException ioe) {
				subscriber.onError(ioe);
			}
		});
	}

	public static Observable<Path> listFolderViaUsing(Path dir, String glob) {
		return Observable.<Path, DirectoryStream<Path>> using(
			unchecked(() -> Files.newDirectoryStream(dir, glob)),
			dirStream -> Observable.from(dirStream),
			unchecked(dirStream -> dirStream.close()));
	}

	public static final Path CACHE_DIR = Paths.get("src", "main", "resources", "cache");
	
	private static Map<String, Cache> cache = new HashMap<>();
	
	@SuppressWarnings("rawtypes")
	public static Observable<Map> fromCache(String url) {
		return getCache(url).get();
	}

	public static Cache getCache(String url) {
		if (!cache.containsKey(url)) {
			cache.put(url, new Cache(url));
		}

		return cache.get(url);
	}
	
	@SuppressWarnings("rawtypes")
	public static class Cache {
		private final Path filePath;
		
		public Cache(String url) {
			byte[] urlBytes = Base64.encodeBase64(url.getBytes());
			Path file = Paths.get(new String(urlBytes));
			this.filePath = CACHE_DIR.resolve(file);
		}
		
		public Observable<Map> get() {
			return read()
					.filter(line -> !line.equals("<><><><><>"))
					.map(line -> new Gson().fromJson(line, Map.class))
					.cast(Map.class);
		}
		
		public Observable<String> read() {
			return fromViaUsing(this.filePath)
					.onErrorResumeNext(Helpers::createFileOnNotFound);
		}
		
		public void clear() {
			try {
				BasicFileAttributes targetAttrs = Files.readAttributes(this.filePath, BasicFileAttributes.class);
				FileTime targetTime = targetAttrs.lastModifiedTime();
				FileTime hour = FileTime.fromMillis(System.currentTimeMillis() - (60L * 60L * 1000L));

				if (targetTime.compareTo(hour) < 0) {
					synchronized (this.filePath) {
						Files.delete(this.filePath);
					}
				}
			} catch (IOException e) {
			}
		}
		
		public Observable<String> add(Map json) {
			return Observable.just(json)
			.map(jsonMap -> new GsonBuilder().create().toJson(jsonMap))
			.map(jsonString -> ("<><><><><>\n" + jsonString + "\n"))
			.flatMap(jsonString -> {
				try {
					synchronized (this.filePath) {
						Files.write(
								this.filePath,
								jsonString.getBytes(),
								StandardOpenOption.APPEND,
								StandardOpenOption.CREATE);
					}
					return Observable.just(jsonString);
				} catch (Exception e) {
					return Observable.error(e);
				}
			});
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Observable<Map> requestJson(HttpAsyncClient client,
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
						.doOnNext(json -> getCache(url).add((Map) json).subscribe());

		return Observable.amb(fromCache(url), response);
	}
	
	@SafeVarargs
	public static <T> Observable<T> sorted(Comparator<? super T> comparator, T... data) {
		List<T> listData = Arrays.asList(data);
		listData.sort(comparator);
		
		return Observable.from(listData);
	}
	
	public static Observable<Long> interval(Long... gaps) {
		return interval(Arrays.asList(gaps));
	}
	
	public static Observable<Long> interval(List<Long> gaps) {
		return interval(gaps, TimeUnit.MILLISECONDS);
	}
	
	public static Observable<Long> interval(List<Long> gaps, TimeUnit unit) {
		return interval(gaps, unit, Schedulers.computation());
	}
	
	public static Observable<Long> interval(List<Long> gaps, TimeUnit unit, Scheduler scheduler) {
		if (gaps == null || gaps.isEmpty()) {
			throw new IllegalArgumentException("Provide one or more interval gaps!");
		}
		
		return Observable.<Long>create(subscriber -> {
			int size = gaps.size();

			Worker worker = scheduler.createWorker();
			subscriber.add(worker);
			
			final Action0 action = new Action0() {

				long current = 0;
				@Override
				public void call() {
					subscriber.onNext(current++);

					long currentGap = gaps.get((int) current % size);
					worker.schedule(this, currentGap, unit);
				}
			};
			
			worker.schedule(action, gaps.get(0), unit);
		});
	}

	public static Observable<ObservableHttpResponse> request(String url) {
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Observable<Map> requestJson(String url) {
		Observable<String> rawResponse = request(url)
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
						.doOnNext(json -> getCache(url).add((Map) json).subscribe());

		return Observable.amb(fromCache(url), response);
	}
}
