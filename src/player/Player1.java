package player;

public class Player1 {

	public static void main(String[] args) {
		Player player = new Player(1, "localhost", 8080);
		player.queue();
		for (int i = 1; i <= 20;) {
			if (player.canPlay()) {
				String prevMove = player.getPrevMove();
				player.play(prevMove + " " + i);
				i += 2;
			}
		}
	}

}
