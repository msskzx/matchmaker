package player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class PlayerClient extends Thread {
	
	/*
	 * PlayerClient is responsible for forwarding communication between player and server
	 */

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Player player;
    private boolean init;
    
    PlayerClient(String serverAddress, int serverPort, Player player) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.player = player;
        this.outputStream.flush();
        this.init();
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                String message = (String) inputStream.readObject();
                if (message == null) {
                    break;
                }
                processServerMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
        	cleanup();
        }
    }
    
    void init() {
    	System.out.println("Started player Client " + this.player.getId());
    	try {
			this.outputStream.writeInt(this.player.getId());
	        this.outputStream.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
			this.init = false;
		}
    	this.init = true;
    }

    void queuePlayer() {
        try {
            System.out.println("Queueing");
            outputStream.writeObject("Queue");
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void play(String move) {
        try {
            outputStream.writeObject(move);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    boolean isInit() {
    	return this.init;
    }

    private void processServerMessage(String message) throws IOException, ClassNotFoundException {
        if (message.equals("Game Started!")) {
            boolean turn = inputStream.readBoolean();
            System.out.println("Game Started!");
            System.out.println(turn? "You go first!" : "Opponent goes first!");
            player.notifyGameStarted(turn);
        } else if (message.equals("Game Over!")) {
            player.notifyGameOver();
        } else {
        	System.out.println("Received:" + message);
            player.notifyTurn(message);
        }
    }

    private void cleanup() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
