package com.learning.examples.syncaids;

import java.util.concurrent.Phaser;

/*
 * The Phaser is a barrier on which the dynamic number of threads need to 
 * wait before continuing execution. 
 */
public class PhaserDemo {
	public static void main(String[] args) {

		// Creates a new phaser with 1 registered unArrived parties and initial phase
		// number is 0
		Phaser phaser = new Phaser(1) {
			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				System.out.println("onAdvance() method, current phase=" + phase);
				// return true after completing phase-3 or if number of registeredParties become
				// 0
				if (phase == 3 || registeredParties == 0) {
					System.out.println("onAdvance() method, returning true, hence phaser will terminate");
					return true;
				} else {
					System.out.println("onAdvance() method, returning false, hence phaser will continue");
					return false;
				}
			}
		};
		System.out
				.println("new phaser is created with 1 registered unArrived party");

		// Create 3 threads
		Thread thread1 = new Thread(new Traveller(phaser, "travller1"), "traveller-1");
		Thread thread2 = new Thread(new Traveller(phaser, "travller2"), "traveller-2");
		Thread thread3 = new Thread(new Traveller(phaser, "travller3"), "traveller-3");

		System.out.println("\n--------Phaser has started---------------");
		// Start 3 threads
		thread1.start();
		thread2.start();
		thread3.start();
		int currentPhase;
		while (!phaser.isTerminated()) {
			// get current phase
			currentPhase = phaser.getPhase();
			// arriveAndAwaitAdvance() will cause thread to wait until current phase has
			// been completed i.e. until all registered threads call arriveAndAwaitAdvance()
			phaser.arriveAndAwaitAdvance();
			System.out.println("------Phase-" + currentPhase + " has been COMPLETED----------");
		}
	}

	static class Traveller implements Runnable {

		Phaser phaser;

		Traveller(Phaser phaser, String name) {
			this.phaser = phaser;
			this.phaser.register();
			System.out.println(name + " has " + "been registered with phaser");
		}

		@Override
		public void run() {
			try {
				System.out.println("Check-in is completed for " + Thread.currentThread().getName());
				phaser.arriveAndAwaitAdvance();
				Thread.sleep(100);
				System.out.println("Security screening is completed for " + Thread.currentThread().getName());
				phaser.arriveAndAwaitAdvance();
				Thread.sleep(100);
				System.out.println("Boarding is completed for " + Thread.currentThread().getName());
				phaser.arriveAndAwaitAdvance();
				Thread.sleep(100);
				System.out.println(Thread.currentThread().getName() + " is seated");
				phaser.arriveAndAwaitAdvance();
				Thread.sleep(100);

				phaser.arriveAndDeregister();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

}
