package com.learning.examples.syncaids;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

	public static void main(String[] args) {

		Queue<Traveller> travellersQueue = new ConcurrentLinkedQueue<>();
		ExecutorService pool = Executors.newCachedThreadPool();
		final Semaphore sp = new Semaphore(2);

		travellersQueue.add(new Traveller("travller1"));
		travellersQueue.add(new Traveller("travller2"));
		travellersQueue.add(new Traveller("travller3"));
		travellersQueue.add(new Traveller("travller4"));
		travellersQueue.add(new Traveller("travller5"));
		travellersQueue.add(new Traveller("travller6"));

		for (int i = 0; i < 6; i++) {
			pool.execute(() -> {
				try {
					sp.acquire();
					Traveller currentTraveller = travellersQueue.poll();
					System.out.println(currentTraveller.getName() + " acquired the lock.");
					currentTraveller.setBaggageCheckedIn(true);
					currentTraveller.setSecurityCleared(true);
					currentTraveller.setBoardingDone(true);
					currentTraveller.setSeated(true);
					Thread.sleep(2000);
					System.out.println(currentTraveller.getName() + " has completed all the process");
					sp.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		pool.shutdown();
	} 
}

class Traveller {
	String name;
	boolean isBaggageCheckedIn;
	boolean isSecurityCleared;
	boolean isBoardingDone;
	boolean isSeated;

	public Traveller(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isBaggageCheckedIn() {
		return isBaggageCheckedIn;
	}

	public void setBaggageCheckedIn(boolean isBaggageCheckedIn) {
		this.isBaggageCheckedIn = isBaggageCheckedIn;
	}

	public boolean isSecurityCleared() {
		return isSecurityCleared;
	}

	public void setSecurityCleared(boolean isSecurityCleared) {
		this.isSecurityCleared = isSecurityCleared;
	}

	public boolean isBoardingDone() {
		return isBoardingDone;
	}

	public void setBoardingDone(boolean isBoardingDone) {
		this.isBoardingDone = isBoardingDone;
	}

	public boolean isSeated() {
		return isSeated;
	}

	public void setSeated(boolean isSeated) {
		this.isSeated = isSeated;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "check-in:"+isBaggageCheckedIn+", security cleared:"+isSecurityCleared+
				", boarding done:"+isBoardingDone+", is seated:"+isSeated;
	}
}
