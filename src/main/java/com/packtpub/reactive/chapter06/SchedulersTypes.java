package com.packtpub.reactive.chapter06;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import com.packtpub.reactive.common.Program;

/**
 * A collection of examples of using the different {@link Scheduler}s.
 * 
 * @author meddle
 */
public class SchedulersTypes implements Program {
	


	@Override
	public String name() {
		return "Demonstration of the different Schedulers types";
	}

	@Override
	public int chapter() {
		return 6;
	}
	
	
	public void schedule(Scheduler scheduler, int numberOfSubTasks, boolean onTheSameWorker) {
		List<Integer> list = new ArrayList<>(0);
		AtomicInteger current = new AtomicInteger(0);
		
		Random random = new Random();
		Worker worker = scheduler.createWorker();

		Action0 addWork = () -> {

			synchronized (list) {
				System.out.println("  Add : " + Thread.currentThread().getName() + " " + current.get());
				list.add(random.nextInt(current.get()));
				System.out.println("  End add : " + Thread.currentThread().getName() + " " + current.get());
			}

			
		};

		Action0 removeWork = () -> {

			synchronized (list) {
				if (!list.isEmpty()) {
					System.out.println("  Remove : " + Thread.currentThread().getName());
					list.remove(0);
					System.out.println("  End remove : " + Thread.currentThread().getName());

				}
			}

		};


		Action0 work = () -> {
			System.out.println(Thread.currentThread().getName());

			for (int i = 1; i <= numberOfSubTasks; i++) {
				current.set(i);
				
				System.out.println("Begin add!");
				if (onTheSameWorker) {
					worker.schedule(addWork);
				} else {
					scheduler.createWorker().schedule(addWork);
				}
				System.out.println("End add!");
			}
			
			while (!list.isEmpty()) {
				System.out.println("Begin remove!");

				if (onTheSameWorker) {
					worker.schedule(removeWork);
				} else {
					scheduler.createWorker().schedule(removeWork);
				}

				System.out.println("End remove!");
			}
		};
		
		worker.schedule(work);
	}

	@Override
	public void run() {

		System.out.println("Immediate");
		schedule(Schedulers.immediate(), 2, true);
		System.out.println("Spawn!");
		schedule(Schedulers.immediate(), 2, false);
		try { Thread.sleep(1000L); } catch (InterruptedException e) {}
		
		System.out.println("------");

		System.out.println("Trampoline");
		schedule(Schedulers.trampoline(), 2, true);
		System.out.println("Spawn!");
		schedule(Schedulers.trampoline(), 2, false);
		try { Thread.sleep(1000L); } catch (InterruptedException e) {}

		System.out.println("------");

		System.out.println("New thread");
		schedule(Schedulers.newThread(), 2, true);
		try { Thread.sleep(500L); } catch (InterruptedException e) {}

		System.out.println("------");

		System.out.println("Spawn!");
		schedule(Schedulers.newThread(), 2, false);
		try { Thread.sleep(500L); } catch (InterruptedException e) {}

		System.out.println("------");

		System.out.println("Computation thread");
		schedule(Schedulers.computation(), 5, true);
		try { Thread.sleep(500L); } catch (InterruptedException e) {}

		System.out.println("------");

		System.out.println("Spawn!");
		schedule(Schedulers.computation(), 5, false);
		try { Thread.sleep(500L); } catch (InterruptedException e) {}

		System.out.println("------");

		System.out.println("IO thread");
		schedule(Schedulers.io(), 2, true);
		try { Thread.sleep(500L); } catch (InterruptedException e) {}

		System.out.println("------");

		System.out.println("Spawn!");
		schedule(Schedulers.io(), 2, false);
		try { Thread.sleep(500L); } catch (InterruptedException e) {}
		
		
	}
	
	public static void main(String[] args) {
		new SchedulersTypes().run();
	}

}
