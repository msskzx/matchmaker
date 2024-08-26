package matchmaker;

import java.io.IOException;

public class ServerMain {

	public static void main(String[] args) {
		try {
			MatchmakerServer server = new MatchmakerServer(8080);
			server.start();
		} catch (IOException e) {
			System.err.println("Failed to start the server: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
