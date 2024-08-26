package matchmaker;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class Game {

    /*
     * Game is responsible for managing a game between two players
     * managing turns, notifying players about their turn, updating
     * state using incoming player move if it is his turn
     */

    private final Matchmaker matchmaker;
    private final AtomicReference<String> state;
    private final int id;
    private final int playerId1;
    private final int playerId2;
    private final AtomicInteger round;
    private final AtomicInteger turn;

    private static final int MAX_ROUNDS = 20; // 10 for each player

    Game(int id, Matchmaker matchmaker, int playerId1, int playerId2) {
        this.id = id;
        this.matchmaker = matchmaker;
        this.playerId1 = playerId1;
        this.playerId2 = playerId2;
        this.state = new AtomicReference<>("");
        this.round = new AtomicInteger(1);
        this.turn = new AtomicInteger(playerId1);
    }

    void play(int playerId, String move) {
        if (this.isGameOver()) {
            this.gameOver();
            return;
        }

        if (this.canPlay(playerId)) {
            this.setState(move);
            this.swapTurn(playerId);
            this.round.incrementAndGet();
            this.notifyTurn(move);
        }
    }

    void notifyTurn(String prevMove) {
        this.matchmaker.notifyTurn(this.turn.get(), prevMove);
    }

    void gameOver() {
        this.matchmaker.notifyGameOver(this.getId(), playerId1);
        this.matchmaker.notifyGameOver(this.getId(), playerId2);
    }

    boolean canPlay(int player) {
        return player == this.turn.get();
    }

    boolean isGameOver() {
        return this.round.get() > MAX_ROUNDS;
    }

    String getState() {
        return this.state.get();
    }

    int getTurn() {
        return this.turn.get();
    }

    int getPlayerId1() {
        return this.playerId1;
    }

    int getPlayerId2() {
        return this.playerId2;
    }

    int getId() {
        return this.id;
    }

    int getRound() {
        return this.round.get();
    }

    private void swapTurn(int playerId) {
        this.turn.set(this.getPlayerId1() != playerId ? this.getPlayerId1() : this.getPlayerId2());
    }

    private void setState(String state) {
        this.state.set(state);
    }
}
