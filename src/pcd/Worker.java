package pcd;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Worker {

	private int porto;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public Worker(int porto) throws ClassNotFoundException {
		this.porto = porto;
		runWorker();

	}

	public void runWorker() throws ClassNotFoundException {

		try {
			connectToServer();
			readTask();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			System.out.println("Closing");

			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void readTask() {

		Task t;

		try {
			while (true) {
				out.writeObject("Connected");
				t = (Task) in.readObject();
				News news = t.getNews();
				String input = t.getInput();
				String name = t.getName();
				Algorithm alg = new Algorithm(input, news);
				alg.verifica();
				Result result = new Result(alg.getnOccur(), news, name);
				System.out.println(result.getName());
				out.writeObject(result);
				System.out.println("ENVIOU");
			}
		} catch (ClassNotFoundException | IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void connectToServer() throws IOException {

		InetAddress endereco = InetAddress.getByName("localhost");
		System.out.println("Endereço: " + endereco);
		socket = new Socket(endereco, porto);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		out.writeObject("Worker");
		out.flush();

	}

	public static void main(String[] args) {
		try {
			Worker w = new Worker(8080);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
