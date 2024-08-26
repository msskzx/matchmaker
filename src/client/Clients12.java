package client;

public class Clients12 {

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 12345;
		Client client1 = new Client(serverAddress, serverPort, 1);
		Client client2 = new Client(serverAddress, serverPort, 2);
        String response = "";
        for (int i = 1; i <= 20; i++) {
			client1.send(response + i + " ");
			response = client2.receive();
            i++;
            client2.send(response + i + " ");
			response = client1.receive();
        }
    }
}
