package pcd;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;

public class DealWithClient extends Thread {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Server server;
	private int id;
	private List<News> news;
	private Barrier barrier;
	//private barrier;

	public DealWithClient(Server server, int id, ObjectInputStream in, ObjectOutputStream out, List<News> news) {
		this.server = server;
		this.in = in;
		this.out = out;
		this.id = id;
		this.news = news;
	}

	@Override
	public void run() {

		while (true) {

			try {

				int tasks = 0;
				Request r = (Request) in.readObject();

				System.out.println("DealWithClient lançado");
				for (News n : news) {
					tasks++;

				}
				Barrier barrier = new Barrier(tasks);
				server.getBarreiras().put(id, barrier);

				for (News n : news) {
					Task t = new Task(r.getIdCliente(), n, r.getInput(), n.getTitle());
					server.getTarefas().offer(t);
				}

				barrier.await();
				List<Result> results = barrier.getResults();
				for (Result result: results){
					System.out.println(result.getName());
				}
				Collections.sort(results);
				out.writeObject(results);
				barrier.getResults().clear();

			} catch (ClassNotFoundException | IOException | InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
