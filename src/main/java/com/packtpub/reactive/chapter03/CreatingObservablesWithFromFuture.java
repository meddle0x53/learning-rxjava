package com.packtpub.reactive.chapter03;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

/**
 * Demonstrates creating Observables from Future objects.
 * 
 * @author meddle
 */
public class CreatingObservablesWithFromFuture implements Program {

	@Override
	public String name() {
		return "Using Observable.from with Future";
	}

	@Override
	public int chapter() {
		return 3;
	}

	@Override
	public void run() {
		CountDownLatch latch = new CountDownLatch(1);

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		Path path = Paths.get("src", "main", "resources", "lorem.txt");

		AsynchronousFileChannel asyncChannel = null;
		try {
			asyncChannel = AsynchronousFileChannel.open(path);

			Observable
					.from(asyncChannel.read(buffer, 0))
					.subscribeOn(Schedulers.io())
					.subscribe(
							(b) -> buffer.flip(),
							System.err::println,
							() -> {
								System.out.println(StandardCharsets.UTF_8.decode(buffer));
								latch.countDown();
							});


			System.out.println("Before Lorem");
			System.out.println("We can do other things!");

		} catch (IOException e) {
		} finally {
			try {
				if (asyncChannel != null) {
					asyncChannel.close();
				}
			} catch (IOException e) {}
		}

		try {
			System.out.println("Waiting for the thread to end...");
			System.out.println("--------------------------------");
			
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new CreatingObservablesWithFromFuture().run();
	}

}
