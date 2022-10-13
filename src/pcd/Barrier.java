package pcd;

import java.util.LinkedList;
import java.util.List;

public class Barrier {

	private int unlock;
	private int waiters = 0;
	private List<Result> results = new LinkedList<>();

	public Barrier(int unlock) {
		this.unlock = unlock;
	}

	public synchronized void await() throws InterruptedException {

		while (waiters < unlock) {
			wait();
		}
		waiters = 0;
		notifyAll();
	}

	public synchronized void putResults(Result r) throws InterruptedException {
		while (waiters >= unlock)
			wait();
		results.add(r);
		waiters++;
		notifyAll();
	}

	public List<Result> getResults() {
		return results;
	}

}
