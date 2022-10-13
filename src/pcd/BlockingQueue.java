package pcd;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue {

	private Queue<Task> queue = new LinkedList<>();
	private int capacidade;

	public int getCapacidade() {
		return capacidade;
	}

	public BlockingQueue() {
		capacidade = -1;
	}

	public BlockingQueue(int capacidade) {
		if (capacidade > 0)
			this.capacidade = capacidade;
		else
			throw new IllegalArgumentException("Capacidade tem que ser um número positivo");
	}

	public synchronized void offer(Task element) {
		if (capacidade == -1)
			queue.offer(element);
		while (size() == capacidade) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();
		queue.add(element);
	}

	public synchronized Task poll() {

		while (size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (capacidade > 0)
			notifyAll();
		return queue.poll();
	}

	public int size() {
		return queue.size();
	}

	public synchronized void clear() {
		queue.clear();
		notifyAll();
	}

}