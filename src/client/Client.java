package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	
	/*
	 * Client is responsible for communicating with the server to send and receive messages 
	 */

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int id;

	Client(String serverAddress, int serverPort, int id) {
		this.id = id;
		try {
			this.socket = new Socket(serverAddress, serverPort);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Client " + this.id + " connected to server.");
	}

	void send(String message) {
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	String receive() {
		String response = "";
		try {
			response = (String) in.readObject();
            System.out.println("Received: " + response);
		} catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
		return response;
	}

}
