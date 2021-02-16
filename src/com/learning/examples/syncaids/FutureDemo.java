package com.learning.examples.syncaids;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;

public class FutureDemo {
	static UnaryOperator<Traveller> baggageCheckin = traveller -> {
		traveller.setBaggageCheckedIn(true);
		sleep();
		return traveller;
	};

	static UnaryOperator<Traveller> securityClearance = traveller -> {
		traveller.setSecurityCleared(true);
		sleep();
		return traveller;
	};

	static UnaryOperator<Traveller> boarding = traveller -> {
		traveller.setBoardingDone(true);
		sleep();
		return traveller;
	};

	static UnaryOperator<Traveller> seating = traveller -> {
		traveller.setSeated(true);
		sleep();
		return traveller;
	};

	private static void sleep() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Queue<Traveller> travellersQueue = new ConcurrentLinkedQueue<>();
		travellersQueue.add(new Traveller("traveller1"));
		travellersQueue.add(new Traveller("traveller2"));
		travellersQueue.add(new Traveller("traveller3"));
		travellersQueue.add(new Traveller("traveller4"));
		travellersQueue.add(new Traveller("traveller5"));
		travellersQueue.add(new Traveller("traveller6"));

		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(4);

		for (int i = 0; i < 6; i++) {
			Traveller currentTraveller = travellersQueue.poll();
			CompletableFuture<Traveller> checkinStage = CompletableFuture.supplyAsync(() -> {
				System.out.println("From thread: " + Thread.currentThread().getName() + " - Check-in is completed for "
						+ currentTraveller.getName());
				return baggageCheckin.apply(currentTraveller);
			}, newFixedThreadPool);

			CompletableFuture<Traveller> screeningStage = checkinStage.thenApplyAsync(traveller -> {
				System.out.println("From thread: " + Thread.currentThread().getName() + " - Screening is completed for "
						+ traveller.getName());
				return securityClearance.apply(traveller);
			}, newFixedThreadPool);

			CompletableFuture<Traveller> boardingStage = screeningStage.thenApplyAsync(traveller -> {
				System.out.println("From thread: " + Thread.currentThread().getName() + " - Boarding is completed for "
						+ traveller.getName());
				return boarding.apply(traveller);
			}, newFixedThreadPool);

			CompletableFuture<Traveller> seatingStage = boardingStage.thenApplyAsync(traveller -> {
				System.out.println("From thread: " + Thread.currentThread().getName() + " - " + traveller.getName()
						+ " is seated");
				return seating.apply(traveller);
			}, newFixedThreadPool);

			seatingStage.thenAcceptAsync(traveller -> System.out.println("From thread: "
					+ Thread.currentThread().getName() + " - All process is completed for " + traveller.getName()))
					.join();
		}
		newFixedThreadPool.shutdown();
	}
}
