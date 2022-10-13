package pcd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.OutputStreamWriter;

public class Server {

	public int PORT;
	private int idClient;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private HashMap<Integer, ObjectOutputStream> clients = new HashMap<>();
	private HashMap<Integer, Barrier> barreiras = new HashMap<>();
	private BlockingQueue tarefas = new BlockingQueue(100);
	private List<News> news = new ArrayList<News>();

	public Server(int PORT) {
		this.PORT = PORT;
		
	}

	public synchronized void startServing() throws IOException {
		File[] files = new File("news").listFiles();
		for(File file : files){
			news.add(readFile(file));
		}
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Lan�ou ServerSocket: " + s);

		while (true) {

			try {
				Socket socket = s.accept();
				System.out.println("Conex�o aceite: " + socket);
				doConnections(socket);
				String connected = (String) in.readObject();

				if (connected.equals("Cliente")) {
					System.out.println("connected");

					clients.put(idClient, out);
					out.writeObject(idClient);

					new DealWithClient(this, idClient, in, out, news).start();
					idClient++;
				}

				if (connected.equals("Worker")) {
					System.out.println("chegou um worker");

					new DealWithWorker(this, in, out).start();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
//		else {
//			
//		}
//		}
//			if(socket.isClosed())
//			socket.close();

	private void doConnections(Socket socket) throws IOException {
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
	}

	public HashMap<Integer, ObjectOutputStream> getClients() {
		return clients;
	}

	public static void main(String [] args) {
		try {
			new Server(8080).startServing();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, Barrier> getBarreiras() {
		return barreiras;
	}

	public BlockingQueue getTarefas() {
		return tarefas;
	}
	
	private static News readFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader( new FileInputStream(file), "UTF-8"));

	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");
	    String title= "";
	    int counter = 0;
	    try {
	        while((line = reader.readLine()) != null) {
	        	if (counter == 0){
	        		title = line;
	        		counter = -1;
	        	}
	        	else{
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        	}
	        }

	        News n = new News(title,stringBuilder.toString());
	        return n;
	    } finally {
	        reader.close();
	    }
	}
}
