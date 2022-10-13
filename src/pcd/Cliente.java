package pcd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Cliente {

	JFrame window;
	Container container;
	JTextArea textarea = new JTextArea("                         ");
	private Socket socket;
	private boolean windowClosed;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private static final int PORT = 8080;
	private int id;
	private DefaultListModel<String> news;
	JList<String> listofnews;
	private List<Result> results;

	public Cliente() {
		gui();
		run();

	}

	public void run() {
		try {
			connectToServer();

			try {
				while (true) {
					Object o = in.readObject();
					System.out.print("Chegaram os resultados");
					results = (List<Result>) o;
					for (Result r : results) {
						if (r.getnOcor() != 0){
							news.addElement(r.getString());	
					}
					}
					listofnews.updateUI();
				}

			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (windowClosed) {
				try {
					socket.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ObjectInputStream getIn() {
		return in;
	}

	private void connectToServer() throws IOException {
		System.out.println("passeiaqui");
		InetAddress endereco = InetAddress.getByName(null);
		socket = new Socket(endereco, PORT);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		out.writeObject("Cliente");
		out.flush();
		try {
			id = (Integer) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void gui() {
		//Criar Janela
		window = new JFrame("SearchNews");
		container = window.getContentPane();
		container.setLayout(new BorderLayout(8, 6));
		window.setSize(1920, 1080);
		window.setLocation(200, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Painel da Esquerda (Lista de Resultados)
		JPanel left = new JPanel();
		news = new DefaultListModel<String>();
		listofnews = new JList<>(news);
		JScrollPane scroll = new JScrollPane(listofnews);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(700, 700));
		left.add(scroll);
		listofnews.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedValue = listofnews.getSelectedIndex();
					System.out.println(selectedValue);
					try{
					textarea.setText(results.get(selectedValue).getNews().getContent());
					}catch(IndexOutOfBoundsException e1){
					}
					}
			}	
		});
		container.add(left, BorderLayout.WEST);
		
		//Painel da direita
		JPanel right = new JPanel(new FlowLayout());
		textarea.setPreferredSize(new Dimension(800, 700));
		textarea.setLineWrap(true);
		textarea.setEditable(false);
		right.add(textarea);
		container.add(right,BorderLayout.EAST);
		
		//Painel de topo
		JPanel top = new JPanel();
		JButton search = new JButton("Search");
		JTextField searchinput = new JTextField(10);
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String input = searchinput.getText();
					news.clear();
					textarea.setText("                         ");
					Request r = new Request(id,input);
					out.writeObject(r);
					out.reset();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		top.setBorder(new LineBorder(Color.BLACK, 3));
		top.setLayout(new FlowLayout(2));
		top.add(searchinput);
		top.add(search);
		container.add(top, BorderLayout.NORTH);
		
		execute();

	}

	public void execute() {
		window.setVisible(true);

	}

	public static void main(String[] args) {
		Cliente lf = new Cliente();

	}
}
