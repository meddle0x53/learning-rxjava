package com.packtpub.reactive.four;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Paths;

import rx.Observable;
import rx.schedulers.TimeInterval;

import com.packtpub.reactive.common.Program;

public class TailExample implements Program {

	@Override
	public String name() {
		return "A reactive tail program.";
	}

	@Override
	public int chapter() {
		return 4;
	}

	public Observable<String> tailFile(File fileToTail, int n) {
		return Observable
				.create(subscriber -> {

					long pos = 0;
					try (RandomAccessFile file = new RandomAccessFile(
							fileToTail, "r")) {
						pos = file.length() - Math.min(n, file.length());

						file.seek(pos);

						for (;; Thread.sleep(1000L)) {

							int l = (int) (file.length() - pos);
							if (l <= 0) {
								continue;
							}

							byte[] buf = new byte[l];
							file.read(buf, 0, l);
							String out = new String(buf, 0, l);

							pos = pos + l;
							subscriber.onNext(out);
						}

					} catch (Exception e) {
						subscriber.onError(e);
					}

				});
	}

	public String paddedVal(TimeInterval<String> val) {
		StringBuilder builder = new StringBuilder();
		long time = val.getIntervalInMilliseconds();

		while (time > 1000L) {
			builder.append("-");
			time -= 1000L;
		}
		builder.append("> ");
		builder.append(val.getValue());

		return builder.toString();
	}

	@Override
	public void run() {
		File path = Paths.get("src", "main", "resources", "test.log").toFile();
		tailFile(path, 1000).flatMap(v -> Observable.from(v.split("\n")))
				.timeInterval().map(this::paddedVal)
				.filter(s -> s.contains("vim"))
				.subscribe(System.out::println, System.err::println);

	}

	public static void main(String[] args) {
		new TailExample().run();
	}

}
