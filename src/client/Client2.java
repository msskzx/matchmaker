package client;

public class Client2 {

	public static void main(String[] args) {
		String serverAddress = "localhost";
		int serverPort = 12345;

		Client client = new Client(serverAddress, serverPort, 2);
		String response = "";
		for (int i = 2; i <= 20; i += 2) {
			response = client.receive();
			client.send(response + i + " ");
		}
	}
}
