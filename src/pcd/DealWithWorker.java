package pcd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DealWithWorker extends Thread {

	private Server server;
	private ObjectInputStream in;
	private ObjectOutputStream out;


	public DealWithWorker(Server server, ObjectInputStream in, ObjectOutputStream out) {
		this.in = in;
		this.out = out;
		this.server = server;
	}

	@Override
	public void run() {

		while (true) {
			try {
				Object o = in.readObject();
				Task task = null;
				if (o instanceof String) {
					String info = (String) o;
					if (info.equals("Connected")) {
						System.out.println("Connected");
						task = server.getTarefas().poll();
						out.writeObject(task);
						System.out.println("Request sent");
					}
				}
				Object ob = in.readObject();

				if (ob instanceof Result) {
					Result result = (Result) ob;
					server.getBarreiras().get(task.getId()).putResults(result);
					System.out.println("Resultado enviado");

				}
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
				e.printStackTrace();

			}
			
		}
	}
}
