package server;

import java.io.*;
import java.net.*;

public class Server {
	
	/*
	 * Server is responsible for accepting new connection and forwarding communication between clients
	 */

    private Socket client1Socket;
    private ObjectOutputStream client1Out;
    private ObjectInputStream client1In;

    private Socket client2Socket;
    private ObjectOutputStream client2Out;
    private ObjectInputStream client2In;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is waiting for connections...");

            client1Socket = serverSocket.accept();
            client1Out = new ObjectOutputStream(client1Socket.getOutputStream());
            client1In = new ObjectInputStream(client1Socket.getInputStream());
            System.out.println("Client 1 connected.");

            client2Socket = serverSocket.accept();
            client2Out = new ObjectOutputStream(client2Socket.getOutputStream());
            client2In = new ObjectInputStream(client2Socket.getInputStream());
            System.out.println("Client 2 connected.");

            forwardMessages();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error starting server");
        } finally {
            cleanup();
        }
    }

    private void forwardMessages() throws IOException, ClassNotFoundException {
        for (int i = 1; i <= 20; i++) {
            if (i % 2 == 1) {
                String message = (String) client1In.readObject();
                client2Out.writeObject(message);
                client2Out.flush();
            } else {
                String message = (String) client2In.readObject();
                client1Out.writeObject(message);
                client1Out.flush();
            }
        }
    }

    private void cleanup() {
        try {
            if (client1In != null) {
                client1In.close();
            }
            if (client1Out != null) {
                client1Out.close();
            }
            if (client1Socket != null && !client1Socket.isClosed()) {
                client1Socket.close();
            }

            if (client2In != null) {
                client2In.close();
            }
            if (client2Out != null) {
                client2Out.close();
            }
            if (client2Socket != null && !client2Socket.isClosed()) {
                client2Socket.close();
            }

        } catch (IOException e) {
        	System.out.println("Error stopping server");
        }
    }
}
