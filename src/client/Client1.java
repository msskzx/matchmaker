package client;

public class Client1 {

	public static void main(String[] args) {
		String serverAddress = "localhost";
		int serverPort = 12345;

		Client client = new Client(serverAddress, serverPort, 1);
		String response = "";
		for (int i = 1; i <= 20; i += 2) {
			client.send(response + i + " ");
			response = client.receive();
		}
	}
}
