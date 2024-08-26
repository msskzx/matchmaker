package matchmaker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

class MatchmakerServer {

    /*
     * MatchmakerServer is responsible for accepting incoming connections and forwarding to matchmaker
     */

	private Matchmaker matchmaker;
	private ConcurrentHashMap<Integer, ClientHandler> clientHandlers;
	private int port;

	MatchmakerServer(int port) {
		this.port = port;
		this.matchmaker = new Matchmaker(this);
		this.clientHandlers = new ConcurrentHashMap<>();
	}

	void start() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				System.out.println("Listening for connections...");
				Socket clientSocket = serverSocket.accept();
				ClientHandler handler = new ClientHandler(clientSocket);
				handler.start();
			}
		}
	}

	void notifyGameStarted(int playerId, boolean turn) {
		ClientHandler handler = this.clientHandlers.get(playerId);
		if (handler != null) {
			handler.notifyGameStarted(turn);
		} else {
			System.out.println("Handler not found!");
		}
	}

	void notifyGameOver(int playerId) {
		ClientHandler handler = this.clientHandlers.get(playerId);
		if (handler != null) {
			handler.notifyGameOver();
			this.clientHandlers.remove(playerId);
		}
	}

	void notifyTurn(int playerId, String previousMove) {
		ClientHandler handler = this.clientHandlers.get(playerId);
		if (handler != null) {
			handler.notifyTurn(previousMove);
		}
	}

	private class ClientHandler extends Thread {
		private Socket clientSocket;
		private int playerId;
		private ObjectOutputStream outputStream;
		private ObjectInputStream inputStream;

		ClientHandler(Socket clientSocket) throws IOException {
			this.clientSocket = clientSocket;
			this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
			this.outputStream.flush();
		}

		@Override
		public void run() {
			try {
				System.out.println("Waiting for player ID...");
				this.playerId = inputStream.readInt();
				System.out.println("New player connected with ID: " + this.playerId);
				clientHandlers.put(playerId, this);
				
				while (true) {
					String message = (String) inputStream.readObject();
					if (message.startsWith("Queue")) {
						matchmaker.queuePlayer(playerId);
					} else {
						matchmaker.play(playerId, message);
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				cleanup();
			}
		}

		void notifyGameStarted(boolean turn) {
			System.out.println("Game Started!");
			try {
				outputStream.writeObject("Game Started!");
				outputStream.writeBoolean(turn);
				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		void notifyGameOver() {
			System.out.println("Game Over!");
			try {
				outputStream.writeObject("Game Over!");
				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// TODO do not disconnect if game over
				cleanup();
			}
		}

		void notifyTurn(String previousMove) {
			try {
				outputStream.writeObject(previousMove);
				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void cleanup() {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
				if (clientSocket != null && !clientSocket.isClosed()) {
					clientSocket.close();
				}
				clientHandlers.remove(playerId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
