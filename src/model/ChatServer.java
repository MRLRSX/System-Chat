package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {

	List<PrintWriter> escritores = new ArrayList<>();

	public ChatServer() {
		ServerSocket serverSocket;

		try {
			serverSocket = new ServerSocket(5000);
			while (true) {
				Socket socket = serverSocket.accept();
				new Thread(new MonitoraCliente(socket)).start();
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				escritores.add(writer);
			}
		} catch (IOException error) {
			error.printStackTrace();
		}
	}

	private void encaminharParaTodos(String texto) {
		for (PrintWriter w : escritores) {
			try {
				w.println(texto);
				w.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class MonitoraCliente implements Runnable {
		Scanner leitor;

		public MonitoraCliente(Socket socket) {
			try {
				leitor = new Scanner(socket.getInputStream());

			} catch (IOException error) {
				error.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				String texto;
				while ((texto = leitor.nextLine()) != null) {
					System.out.println("RECEBEU " + texto);
					encaminharParaTodos(texto);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		new ChatServer();
	}
}
