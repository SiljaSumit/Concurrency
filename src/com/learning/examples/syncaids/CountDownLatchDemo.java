package com.learning.examples.syncaids;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(3);
		
		Runnable traveller = () -> {
			try {
				System.out.println(Thread.currentThread().getName() + " has seated.");
				latch.countDown();
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		};
		new Thread(traveller,"First person").start();
		new Thread(traveller,"Second person").start();
		new Thread(traveller,"Third person").start();

		try {
			latch.await();
			System.out.println("All travellers have seated");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}

