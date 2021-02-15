package com.learning.examples.syncaids;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Exchanger;

public class ExcahngerDemo {

	public static void main(String[] args) {
		Exchanger<String> exchanger = new Exchanger<String>();
		
		Runnable producer = () -> {
			try {
				for (int i = 0; i < 3; i++) {
					exchanger.exchange("produced "+i);
					Thread.sleep(1000);
				}
	        } catch (InterruptedException e) {
	            throw new RuntimeException(e);
	        }
		};
		
		Runnable consumer = () -> {
			try {
				for (int i = 0; i < 3; i++) {
					System.out.println(exchanger.exchange("consumed "));
				}
	        } catch (InterruptedException e) {
	            throw new RuntimeException(e);
	        }
		};
		
		CompletableFuture.allOf(
				CompletableFuture.runAsync(producer), CompletableFuture.runAsync(consumer)).join();
	}
}
