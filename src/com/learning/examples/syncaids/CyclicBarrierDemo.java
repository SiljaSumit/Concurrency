package com.learning.examples.syncaids;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
	public static void main(String[] args) {
		CyclicBarrier checkPoint = new CyclicBarrier(3, new Runnable() {
			@Override
			public void run() {
				System.out.println("All travellers are ready...");
			}
		});
		
		Runnable traveller = () -> {
			try {
				System.out.println("Check-in is completed for " +Thread.currentThread().getName());
				checkPoint.await();
				System.out.println("Security screening is completed for " +Thread.currentThread().getName());
				checkPoint.await();
				System.out.println("Boarding is completed for " +Thread.currentThread().getName());
				checkPoint.await();
				System.out.println(Thread.currentThread().getName()+ " is seated");
				checkPoint.await();
			} catch (InterruptedException | BrokenBarrierException exception) {
				exception.printStackTrace();
			}
		};
		new Thread(traveller, "Traveller1").start();
		new Thread(traveller, "Traveller2").start();
		new Thread(traveller, "Traveller3").start();
	}	
}


