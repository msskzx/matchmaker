package player;

public class Player2 {

	public static void main(String[] args) {
		Player player = new Player(2, "localhost", 8080);
		player.queue();
		for (int i = 2; i <= 20;) {
			if (player.canPlay()) {
				String prevMove = player.getPrevMove();
				player.play(prevMove + " " + i);
				i += 2;
			}
		}
	}

}
