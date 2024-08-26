package matchmaker;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class Matchmaker {
    /*
     * Matchmaker is responsible for:
     * 
     * maintaining a queue of players and adding new players to the queue
     * creating games for players when enough players are available
     * notifying players when a new game is created for them
     * could also keep track of running games here or in a GameManager class, but it's out of scope
     */

    private MatchmakerServer matchmakerServer;
    private final Queue<Integer> queue;
    private final ConcurrentHashMap<Integer, Game> games;
    private final ConcurrentHashMap<Integer, Integer> playerGame;
    private final AtomicInteger idSequence;

    Matchmaker(MatchmakerServer matchmakerServer) {
        queue = new LinkedList<>();
        games = new ConcurrentHashMap<>();
        playerGame = new ConcurrentHashMap<>();
        idSequence = new AtomicInteger(0);
        this.matchmakerServer = matchmakerServer;
    }

    synchronized void queuePlayer(int playerId) {
        queue.add(playerId);
        System.out.printf("Added player %d to queue%n", playerId);
        this.checkQueue();
    }

    synchronized void checkQueue() {
    	System.out.println("Queue size: " + queue.size());
        if (queue.size() >= 2) {
            int playerId1 = queue.poll();
            int playerId2 = queue.poll();
            this.createGame(playerId1, playerId2);
        }
    }

    void notifyGameOver(int gameId, int playerId) {
        deleteGame(gameId);
        matchmakerServer.notifyGameOver(playerId);
    }

    void createGame(int player1, int player2) {
    	System.out.println("Creating a new game");
        int gameId = idSequence.incrementAndGet();
        Game game = new Game(gameId, this, player1, player2);
        games.put(gameId, game);
        playerGame.put(player1, gameId);
        playerGame.put(player2, gameId);
        
        System.out.printf("Created a new Game for player %d and %d%n", player1, player2);
        matchmakerServer.notifyGameStarted(player1, true);
        matchmakerServer.notifyGameStarted(player2, false);
    }

    void deleteGame(int gameId) {
        games.remove(gameId);
        playerGame.entrySet().removeIf(entry -> entry.getValue() == gameId);
    }

    Game getGameFromPlayerId(int playerId) {
        Integer gameId = playerGame.get(playerId);
        return (gameId != null) ? games.get(gameId) : null;
    }

    void play(int playerId, String move) {
        Game game = getGameFromPlayerId(playerId);
        if (game != null) {
            game.play(playerId, move);
        } else {
            System.err.println("Game not found for playerId: " + playerId);
        }
    }

    void notifyTurn(int playerId, String prevMove) {
        if (matchmakerServer != null) {
            matchmakerServer.notifyTurn(playerId, prevMove);
        }
    }
}
